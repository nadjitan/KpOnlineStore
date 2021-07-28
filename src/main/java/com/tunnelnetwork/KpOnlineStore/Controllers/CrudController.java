package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CrudController extends CommonController{

  @GetMapping("/crud")
  private String profilePage(Model model) {

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/login";
    }
    
    Iterable<Product> products = productRepository.getAllProducts();

    model.addAttribute("products", products);
     
    return "crud";
  }

  @PostMapping("/crud/create")
  private String createProduct(
      @ModelAttribute("product") Product product, 
      RedirectAttributes ra) {

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }

    try {
      product.setCreatedAt(LocalDateTime.now());
      product.setUpdatedAt(LocalDateTime.now());
      
      productRepository.saveAndFlush(product);

      ra.addFlashAttribute("crudSuccess", "Product \"" + product.getProductName() + "\" have been successfully created!");  
    } catch (Exception e) {
      ra.addFlashAttribute("crudError", "Product not created. Please fill out the product details first."); 
    }
     
    return "redirect:/crud";
  }

  @PostMapping("/crud/update")
  private String updateProduct(
    @RequestParam("id") Integer id, @ModelAttribute("product") Product product,  
    RedirectAttributes ra) {

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }

    try {
      product.setRating(productRepository.getProduct(id).getRating());
      product.setNumberOfSold(productRepository.getProduct(id).getNumberOfSold());
      product.setComments(productRepository.getProduct(id).getComments());
      product.setCreatedAt(productRepository.getProduct(id).getCreatedAt());
      product.setUpdatedAt(LocalDateTime.now());
      
      productRepository.saveAndFlush(product);

      ra.addFlashAttribute("crudSuccess", "Product \"" + product.getProductName() + "\" was successfully updated!");
    } catch (Exception e) {
      ra.addFlashAttribute("crudError", "Product not updated. One field may be empty or a wrong type."); 
    }
     
    return "redirect:/crud";
  }

  @PostMapping("/crud/delete")
  private String deleteProduct(
    @ModelAttribute("product") Product product, 
    RedirectAttributes ra) {

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }
    
    try {
      productRepository.delete(product);

      ra.addFlashAttribute("crudSuccess", "Product is now deleted.");
    } catch (Exception e) {
      ra.addFlashAttribute("crudError", "Error product not deleted."); 
    }

    return "redirect:/crud";
  }
}
