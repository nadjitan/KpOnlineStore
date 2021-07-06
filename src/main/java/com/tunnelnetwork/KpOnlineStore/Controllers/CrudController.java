package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CrudController extends CommonController{

  @GetMapping("/crud")
  private String profilePage(Model model) {
    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }
    
    Iterable<Product> products = productService.getAllProducts();

    model.addAttribute("products", products);
     
    return "crud";
  }

  @PostMapping("/crud/create")
  private String createProduct(
      @ModelAttribute("product") Product product, 
      BindingResult result, 
      ModelMap model,
      RedirectAttributes ra) {

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }
    
    product.setCreatedAt(LocalDateTime.now());
    product.setUpdatedAt(LocalDateTime.now());
    
    productService.save(product);

    ra.addFlashAttribute("crudStatus", "Product \"" + product.getProductName() + "\" have been successfully created!");
     
    return "redirect:/crud";
  }

  @PostMapping("/crud/update")
  private String updateProduct(
      @RequestParam("id") Integer id,
      @ModelAttribute("product") Product product, 
      BindingResult result, 
      ModelMap model,
      RedirectAttributes ra) {

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }
    // if (result.hasErrors()) {
    //   return result.getAllErrors().toString();
    // }

    product.setCreatedAt(productService.getProduct(id).getCreatedAt());
    product.setUpdatedAt(LocalDateTime.now());
    
    productService.save(product);

    ra.addFlashAttribute("crudStatus", "Product \"" + product.getProductName() + "\" was successfully updated!");
     
    return "redirect:/crud";
  }

  @PostMapping("/crud/delete")
  private String deleteProduct(
      @ModelAttribute("product") Product product, 
      BindingResult result, ModelMap model,
      RedirectAttributes ra) {

    if (!isThereLoggedInUser() && hasRole("USER")) {
      return "redirect:/";
    }
    
    ra.addFlashAttribute("crudStatus", "Product is now deleted.");

    productService.delete(product);

    return "redirect:/crud";
  }
}
