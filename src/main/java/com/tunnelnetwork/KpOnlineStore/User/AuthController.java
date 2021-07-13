package com.tunnelnetwork.KpOnlineStore.User;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  private ModelAndView signup(RedirectAttributes ra, User user) {

    if (userService.doesUserExist(user.getEmail())) {
      ra.addFlashAttribute("userNotCreated", "Email is already in use."); 

      return new ModelAndView("redirect:/sign-up");
    }
    else if (user.getEmail().isEmpty() || user.getFirstName().isEmpty() || 
             user.getLastName().isEmpty() || user.getPassword().isEmpty()) {
      ra.addFlashAttribute("userNotCreated", "Please fill out form."); 

      return new ModelAndView("redirect:/sign-up");
    }
    else {
      userService.signUpUser(user);
      
      ra.addFlashAttribute("userCreated", "Email have been sent. Please confirm your registration."); 

      return new ModelAndView("redirect:/login");
    }
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
  @GetMapping("/change-password/confirm")
	private String confirmResetPassword(@RequestParam("token") String token, Model model) {

		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

		if (optionalConfirmationToken.isPresent()) {
      model.addAttribute("token", token);

      return "change-password";
    }
    else {
      return "redirect:/";
    }
	}

  @PostMapping("/forgot-password")
  private ModelAndView forgotPassword(RedirectAttributes ra, @RequestParam("email") String email) {
    if (userService.doesUserExist(email)) {
      userService.sendResetPasswordMail(email);

      ra.addFlashAttribute("passwordStatus", "Reset password confimation have been sent to your email.");

      return new ModelAndView("redirect:/forgot-password");
    }
    else {
      ra.addFlashAttribute("passwordStatus", "User does not exist.");

      return new ModelAndView("redirect:/forgot-password");
    }
  }

  @PostMapping("/change-password/confirm/{token}")
  private ModelAndView changePassword(
    RedirectAttributes ra,
    @PathVariable(value ="token") String token, 
    @RequestParam("password") String password,
    @RequestParam("confirmPassword") String confirmPassword) {

    Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

		if (optionalConfirmationToken.isPresent()) {
      if (password.equals(confirmPassword)) {
        userService.confirmResetPassword(optionalConfirmationToken.get(), password);

        ra.addFlashAttribute("passwordStatus", "Password have been reset. You can now login.");

        return new ModelAndView("redirect:/login");
      }
      else {
        ra.addFlashAttribute("passwordStatus", "Password does not match.");

        return new ModelAndView("redirect:/change-password/confirm?token=" + token);
      }
    }
    else {
      ra.addFlashAttribute("passwordStatus", "Error invalid token.");

      return new ModelAndView("redirect:/change-password");
    }
  }
}