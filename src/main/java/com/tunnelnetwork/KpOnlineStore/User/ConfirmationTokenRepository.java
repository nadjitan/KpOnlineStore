package com.tunnelnetwork.KpOnlineStore.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

	Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);

	default void saveConfirmationToken(ConfirmationToken confirmationToken) {
		this.save(confirmationToken);
	}

	default void deleteConfirmationToken(Long id) {
		this.deleteById(id);
	}

	default Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {
		return this.findConfirmationTokenByConfirmationToken(token);
	}
}