package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;

import com.tunnelnetwork.KpOnlineStore.Models.Comment;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductDetailsController extends CommonController{
  
  @GetMapping("/product/{id}")
  public String productPage(Model model, @PathVariable("id") long id) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }

    model.addAttribute("maxRating", 5);
    model.addAttribute("product", productService.getProduct(id));
    return "product-details";
  }

  @RequestMapping(value = "/comment", method=RequestMethod.POST)
  @ResponseBody
  public ModelAndView makeComment(@RequestParam("userComment") String comment, @RequestParam("productId") long id) {
    if (!comment.isBlank()) {
      if (!isThereLoggedInUser()) {
        return new ModelAndView("redirect:/");
      }

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      Product product = productService.getProduct(id);
      Comment newComment = new Comment();
      
      newComment.setCommentUserId(0);
      newComment.setCreatedAt(LocalDateTime.now());
      newComment.setUserComment(comment);
      newComment.setUpdatedAt(LocalDateTime.now());
      newComment.setUserName(authentication.getName());

      commentService.save(newComment);

      product.getComments().add(newComment);

      productService.save(product);
    }

    return new ModelAndView("redirect:/product/" + id);
  }

  @RequestMapping(value = "/rate", method=RequestMethod.POST)
  @ResponseBody
  public ModelAndView rateProduct(@RequestParam("rating") Integer rating, @RequestParam("productId") Integer id) {
    if (!isThereLoggedInUser()) {
      return new ModelAndView("redirect:/");
    }

    Product product = productService.getProduct(id);

    product.setRating(rating);

    productService.save(product);

    return new ModelAndView("redirect:/product/" + id);
  }
} 
