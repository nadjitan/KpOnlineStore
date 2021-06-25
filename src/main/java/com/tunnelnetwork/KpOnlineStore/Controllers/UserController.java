package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

  public UserController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
      this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
  }

  // @PostMapping("/login1")
  // public boolean userExists(@RequestParam("username") String username ) {
  //     return inMemoryUserDetailsManager.userExists(username);
  // }

  @PostMapping("/signup1")
  public String signup1(@RequestParam("username") String username, @RequestParam("password") String password) {
      inMemoryUserDetailsManager.createUser(User.withUsername(username).password(passwordEncoder().encode(password)).roles("USER").build());
      return username + " Created!";
  }
}