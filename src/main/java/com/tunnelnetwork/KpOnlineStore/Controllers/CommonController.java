package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {

  @Autowired
  private ProductService productService;
  
  @GetMapping("/login")
  public String showLoginPage() {
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

  @RequestMapping("/")
  public String products(Model model) {
    model.addAttribute("products", productService.getAllProducts());
     
    return "index";
  }
}
