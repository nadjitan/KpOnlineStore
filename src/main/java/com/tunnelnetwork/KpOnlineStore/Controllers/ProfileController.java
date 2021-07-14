package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;
import com.tunnelnetwork.KpOnlineStore.Models.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfileController extends CommonController{

  @GetMapping("/profile")
  private String profilePage(Model model) {
    getUserRole(model);

    getUserFirstAndLastName(model);

    if (!isThereLoggedInUser()) {
      return "redirect:/login";
    }
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Iterable<Receipt> receiptOfUser = receiptRepository.getReceiptsByName(authentication.getName());

    model.addAttribute("receiptList", receiptOfUser);
    model.addAttribute("wishlist", userService.getUserByEmail(authentication.getName()).get().getWhishlist());
    
    return "profile";
  }

  @PostMapping("/removeFromWishlist")
  public String postMethodName(@RequestParam("productId") long productId) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    User user = userService.getUserByEmail(authentication.getName()).get();

    for (Product product : user.getWhishlist()) {
      if (product.getId() == productId) {
        user.getWhishlist().remove(product);

        break;
      }
    }
      
    userService.saveUser(user);

    return "redirect:/profile";
  }
  
}
