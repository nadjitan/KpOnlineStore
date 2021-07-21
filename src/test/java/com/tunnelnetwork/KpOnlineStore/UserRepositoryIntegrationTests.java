package com.tunnelnetwork.KpOnlineStore;

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

    String email = "test@mail.com";
    User found = userRepository.findByEmail(email).get();

    assertEquals(email, found.getEmail());
  }

  @Test
  public void whenNonExistingEmail_thenUserShouldNotExist() {

    String email = "notregisteredmail@mail.com";
    Optional<User> found = userRepository.findByEmail(email);

    assertFalse(found.isPresent());
  }

  @Test
  public void whenValidId_thenUserShouldBeFound() {

    Optional<User> found = userRepository.findById(userId);

    assertTrue(found.isPresent());
  }

  @Test
  public void whenInvalidId_thenUserShouldNotBeFound() {

    long id = -100;
    Optional<User> found = userRepository.findById(id);

    assertFalse(found.isPresent());
  }

  @Test
  public void given2Users_whenFindAll_thenReturn2Users() {
    
    // 1st user is admin which is created before running the app
    // 2nd is the test user

    List<User> usersInDb = userRepository.findAll();

    assertEquals(2, usersInDb.size());
  }
}
