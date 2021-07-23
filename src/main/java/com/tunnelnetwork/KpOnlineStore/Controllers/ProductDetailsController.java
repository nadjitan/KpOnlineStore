package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Models.Comment;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;
import com.tunnelnetwork.KpOnlineStore.Models.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductDetailsController extends CommonController{
  
  Integer maxProducts = 3;

  @GetMapping("/product/{id}")
  private String productPage(Model model, @PathVariable("id") long id) {
    List<Product> newProductList = new ArrayList<Product>();
    for (String bandName : productRepository.getProduct(id).getTags()) {
      for (Product product : productRepository.getProductsByBand(bandName)) {
        if (product != productRepository.getById(id) && maxProducts > newProductList.size()) {
          newProductList.add(product);
        }
      }
    }

    getUserRole(model);

    getUserFirstAndLastName(model);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (isThereLoggedInUser()) {
      User user = userService.getUserByEmail(authentication.getName()).get(); // Get name returns email

      for (Product product : user.getWhishlist()) {
        if (product == productRepository.getById(id)) {
          model.addAttribute("inUserWishlist", true);

          break;
        }
        else {
          model.addAttribute("inUserWishlist", false);
        }
      }
    }

    model.addAttribute("maxRating", 5);
    model.addAttribute("product", productRepository.getProduct(id));
    model.addAttribute("productList", newProductList);
    model.addAttribute("didUserBuyProduct", didUserBuyProduct(id));
    return "product-details";
  }

  @PostMapping("/comment")
  private String makeComment(@RequestParam("userComment") String comment, @RequestParam("productId") long id) {
    if (!comment.isBlank()) {
      if (!isThereLoggedInUser()) {
        return "redirect:/";
      }

      if (didUserBuyProduct(id)) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName()).get();;

        Product product = productRepository.getProduct(id);
        Comment newComment = new Comment();
        
        newComment.setCommentUserId(0);
        newComment.setCreatedAt(LocalDateTime.now());
        newComment.setUserComment(comment);
        newComment.setUpdatedAt(LocalDateTime.now());
        newComment.setUserName(user.getFirstName() + " " + user.getLastName());

        commentRepository.saveAndFlush(newComment);

        product.getComments().add(newComment);

        productRepository.saveAndFlush(product);
      }
    }

    return "redirect:/product/" + id;
  }

  @PostMapping("/rate")
  private String rateProduct(@RequestParam("rating") Integer rating, @RequestParam("productId") Integer id) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }

    if (didUserBuyProduct(id)) {
      Product product = productRepository.getProduct(id);

      product.setRating(rating);

      productRepository.saveAndFlush(product);
    }

    return "redirect:/product/" + id;
  }

  @PostMapping("/addToWishlist")
  private String addToWishlist(@RequestParam("productId") long productId) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    User user = userService.getUserByEmail(authentication.getName()).get(); // Get name returns email

    if (!isProductInWishlisht(productId)) {
      user.getWhishlist().add(productRepository.getById(productId));

      userService.saveUser(user);
    }
    else {
      user.getWhishlist().remove(productRepository.getById(productId));

      userService.saveUser(user);
    }
    
    return "redirect:/product/" + productId;
  }

  private boolean didUserBuyProduct(long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Iterable<Receipt> receiptOfUSer = receiptRepository.getReceiptsByName(authentication.getName());
    Product productToCheck = productRepository.getProduct(id);

    if (receiptOfUSer != null) {
      for (Receipt receipt : receiptOfUSer) {
        List<Product> products = receipt.getProductList();

        for (Product product : products) {
          if (productToCheck == product) {
            return true;
          }
        }
      }
    } else {
      return false;
    }

    return false;
  }

  private boolean isProductInWishlisht(long productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    User user = userService.getUserByEmail(authentication.getName()).get(); // Get name returns email

    for (Product product : user.getWhishlist()) {
      if (product.getId() == productId) {
        return true;
      }
    }

    return false;
  }
} 
