package com.tunnelnetwork.KpOnlineStore.User;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import com.tunnelnetwork.KpOnlineStore.Controllers.CommonController;
import com.tunnelnetwork.KpOnlineStore.Models.User;

@Controller
@AllArgsConstructor
public class AuthController extends CommonController{

  private final UserService userService;

	private final ConfirmationTokenRepository confirmationTokenRepository;

  public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

  @GetMapping("/login")
  private String showLoginPage(Model model) {

    getUserRole(model);

    if (!isThereLoggedInUser()) {
      return "login";
    }

    return "redirect:/";
  }
  @GetMapping("/sign-up")
  public String showSignupPage(Model model) {

    getUserRole(model);

    if (!isThereLoggedInUser()) {
      return "sign-up";
    }

    return "redirect:/";
  }

  @GetMapping("/sign-up/confirm")
	private String confirmMail(@RequestParam("token") String token, Model model) {

    getUserRole(model);

		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(userService::confirmUser);

		return "/login";
	}

  @GetMapping("/forgot-password")
  private String goToForgotPassword(Model model) {

    getUserRole(model);

    if (!isThereLoggedInUser()) {
      return "forgot-password";
    }

    return "redirect:/";
  }
  
  @GetMapping("/change-password/confirm")
	private String confirmResetPassword(@RequestParam("token") String token, Model model) {

    getUserRole(model);
    
		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);

		if (optionalConfirmationToken.isPresent()) {
      model.addAttribute("token", token);

      return "change-password";
    }
    else {
      return "redirect:/";
    }
	}

  @PostMapping("/sign-up")
  private String signup(RedirectAttributes ra, User user) {

    if (userService.doesUserExist(user.getEmail())) {
      ra.addFlashAttribute("userNotCreated", "Email is already in use."); 

      return "redirect:/sign-up";
    }
    else if (user.getEmail().isEmpty() || user.getFirstName().isEmpty() || 
             user.getLastName().isEmpty() || user.getPassword().isEmpty()) {
      ra.addFlashAttribute("userNotCreated", "Please fill out form."); 

      return "redirect:/sign-up";
    }
    else {
      userService.signUpUser(user);
      
      ra.addFlashAttribute("userCreated", "Email have been sent. Please confirm your registration."); 

      return "redirect:/login";
    }
  }

  @PostMapping("/forgot-password")
  private String forgotPassword(RedirectAttributes ra, @RequestParam("email") String email) {

    if (userService.doesUserExist(email)) {
      userService.sendResetPasswordMail(email);

      ra.addFlashAttribute("passwordStatus", "Reset password confimation have been sent to your email.");

      return "redirect:/forgot-password";
    }
    else {
      ra.addFlashAttribute("passwordStatus", "User does not exist.");

      return "redirect:/forgot-password";
    }
  }

  @PostMapping("/change-password/confirm/{token}")
  private String changePassword(
    RedirectAttributes ra,
    @PathVariable(value ="token") String token, 
    @RequestParam("password") String password,
    @RequestParam("confirmPassword") String confirmPassword) {

    Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);

		if (optionalConfirmationToken.isPresent()) {
      if (password.equals(confirmPassword)) {
        userService.confirmResetPassword(optionalConfirmationToken.get(), password);

        ra.addFlashAttribute("passwordStatus", "Password have been reset. You can now login.");

        return "redirect:/login";
      }
      else {
        ra.addFlashAttribute("passwordStatus", "Password does not match.");

        return "redirect:/change-password/confirm?token=" + token;
      }
    }
    else {
      ra.addFlashAttribute("passwordStatus", "Error invalid token.");

      return "redirect:/change-password";
    }
  }
}