package com.tunnelnetwork.KpOnlineStore.User;

import java.util.Optional;

import com.tunnelnetwork.KpOnlineStore.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}