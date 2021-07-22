package com.tunnelnetwork.KpOnlineStore.RepositoryTests;

import com.tunnelnetwork.KpOnlineStore.Models.User;
import com.tunnelnetwork.KpOnlineStore.Models.UserRole;
import com.tunnelnetwork.KpOnlineStore.User.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryIntegrationTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

  private long userId;
  
  @BeforeEach
  public void setup() {

    User user = new User();
    user.setEmail("test@mail.com");
    user.setEnabled(true);
    user.setFirstName("test");
    user.setLastName("test");
    user.setPassword(bCryptPasswordEncoder().encode("test"));
    user.setUserRole(UserRole.USER);

    userId = testEntityManager.persistAndFlush(user).getId();
  }

  @Test
  public void whenFindByEmail_thenUserShouldBeFound() {
    // Given
    String email = "test@mail.com";

    // When
    User found = userRepository.findByEmail(email).get();

    // Then
    assertEquals(email, found.getEmail());
  }

  @Test
  public void whenNonExistingEmail_thenUserShouldNotExist() {
    
    // Given
    String email = "notregisteredmail@mail.com";

    // When
    Optional<User> found = userRepository.findByEmail(email);

    // Then
    assertFalse(found.isPresent());
  }

  @Test
  public void whenValidId_thenUserShouldBeFound() {

    // When
    Optional<User> found = userRepository.findById(userId);

    // Then
    assertTrue(found.isPresent());
  }

  @Test
  public void whenInvalidId_thenUserShouldNotBeFound() {

    // Given
    long id = -100;

    // When
    Optional<User> found = userRepository.findById(id);

    // Then
    assertFalse(found.isPresent());
  }

  @Test
  public void given2Users_whenFindAll_thenReturn2Users() {
    
    // 1st user is admin which is created before running the app
    // 2nd is the test user we just created

    // When
    List<User> usersInDb = userRepository.findAll();

    // Then
    assertEquals(2, usersInDb.size());
  }
}
