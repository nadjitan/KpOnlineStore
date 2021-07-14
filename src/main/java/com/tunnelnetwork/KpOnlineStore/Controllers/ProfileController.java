package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Models.Receipt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController extends CommonController{

  @GetMapping("/profile")
  private String profilePage(Model model) {

    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Iterable<Receipt> receiptOfUser = receiptRepository.getReceiptsByName(authentication.getName());

    model.addAttribute("receiptList", receiptOfUser);
    
    return "profile";
  }
}
