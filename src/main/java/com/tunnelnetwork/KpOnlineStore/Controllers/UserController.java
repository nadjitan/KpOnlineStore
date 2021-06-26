package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  public ModelAndView signup1(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes ra) {

    if (username == "" || password == "") {
      ra.addFlashAttribute("userNotCreated", "Please fill out form.");
      return new ModelAndView("redirect:/signup");
    }

    try {
      inMemoryUserDetailsManager.createUser(User.withUsername(username).password(passwordEncoder().encode(password)).roles("USER").build());
    } catch (Exception e) {
      ra.addFlashAttribute("userNotCreated", "Invalid inputs or user is already created. Please try again.");
      return new ModelAndView("redirect:/signup");
    }

    ra.addFlashAttribute("userCreated", "User have been successfully created you may now login.");
    return new ModelAndView("redirect:/login");
  }
}