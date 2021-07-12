package com.tunnelnetwork.KpOnlineStore.User;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import com.tunnelnetwork.KpOnlineStore.Models.User;

@Controller
@AllArgsConstructor
public class AuthController {

  private final UserService userService;

	private final ConfirmationTokenService confirmationTokenService;

  public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

  // private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

  // Make sure /login & /sign-up are only accessed by users without accounts
  @GetMapping("/login")
  private String showLoginPage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "login";
    }

    return "redirect:/";
  }
  @GetMapping("/sign-up")
  public String showSignupPage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "sign-up";
    }

    return "redirect:/";
  }

  @PostMapping("/sign-up")
  private String signup(User user) {

    // // When user fails to fill up form
    // if (username == "" || password == "") {
    //   ra.addFlashAttribute("userNotCreated", "Please fill out form.");
    //   return new ModelAndView("redirect:/sign-up");
    // }

    // // Create user and catch on creation failure
    // try {
    //   inMemoryUserDetailsManager.createUser(User.withUsername(username).password(passwordEncoder().encode(password)).authorities("USER").build());
    // } catch (Exception e) {
    //   ra.addFlashAttribute("userNotCreated", "Invalid inputs or user is already created. Please try again.");
    //   return new ModelAndView("redirect:/sign-up");
    // }

    // // Only runs once there are no failures catched
    // ra.addFlashAttribute("userCreated", "User have been successfully created you may now login.");

    userService.signUpUser(user);
  
    return "redirect:/login";
  }

  @GetMapping("/sign-up/confirm")
	private String confirmMail(@RequestParam("token") String token) {

		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(userService::confirmUser);

		return "/login";
	}

  @GetMapping("/forgot-password")
  private String goToForgotPassword() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "forgot-password";
    }

    return "redirect:/";
  }
  @GetMapping("/change-password")
  private String goToChangePassword() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "change-password";
    }

    return "redirect:/";
  }

  @PostMapping("/forgot-password")
  private String forgotPassword(@RequestParam("email") String email) {

    return "forgot-password";
  }
  @PostMapping("/change-password")
  private String changePassword(@RequestParam("password") String password) {

    return "change-password";
  }
}