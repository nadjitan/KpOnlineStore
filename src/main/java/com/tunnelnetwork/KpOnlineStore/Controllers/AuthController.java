package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  private InMemoryUserDetailsManager inMemoryUserDetailsManager;

  // Make sure /login & /signup are only accessed by users without accounts
  @GetMapping("/login")
  private String showLoginPage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "login";
    }

    return "redirect:/";
  }
  @GetMapping("/signup")
  public String showSignupPage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "signup";
    }

    return "redirect:/";
  }

  @PostMapping("/signup")
  private ModelAndView signup(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes ra) {

    // When user fails to fill up form
    if (username == "" || password == "") {
      ra.addFlashAttribute("userNotCreated", "Please fill out form.");
      return new ModelAndView("redirect:/signup");
    }

    // Create user and catch on creation failure
    try {
      inMemoryUserDetailsManager.createUser(User.withUsername(username).password(passwordEncoder().encode(password)).authorities("USER").build());
    } catch (Exception e) {
      ra.addFlashAttribute("userNotCreated", "Invalid inputs or user is already created. Please try again.");
      return new ModelAndView("redirect:/signup");
    }

    // Only runs once there are no failures catched
    ra.addFlashAttribute("userCreated", "User have been successfully created you may now login.");
    return new ModelAndView("redirect:/login");
  }
}