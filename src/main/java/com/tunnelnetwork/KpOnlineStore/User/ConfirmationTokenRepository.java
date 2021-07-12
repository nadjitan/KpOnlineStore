package com.tunnelnetwork.KpOnlineStore.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

	Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}