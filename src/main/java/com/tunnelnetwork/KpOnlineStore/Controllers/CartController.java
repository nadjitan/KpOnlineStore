package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CartController extends CommonController{

  @GetMapping("/cart/home")
  public String cartHomePage(Model model) {
    if (!isThereLoggedInUser() || cartService.getCartOfUser() == null) {
      return "redirect:/";
    }

    model.addAttribute("cart", cartService.getCartOfUser());
    return "cart";
  }

  // Shopping cart buttons logic
  @RequestMapping(value = "/addProduct", method=RequestMethod.POST)
  @ResponseBody
  public ModelAndView addProduct(@RequestParam("productId") long id, @RequestParam("productQuantity") Integer productQuantity) {
    if (!cartService.isProductInCart(id)) {
      cartService.addToCart(productService.getProduct(id), productQuantity);
    }

    return new ModelAndView("redirect:/store");
  }
  @RequestMapping(value="/removeProduct", method=RequestMethod.POST)
  @ResponseBody
  public ModelAndView removeProduct(@RequestParam("productId") long id){
    if (cartService.isProductInCart(id)) {
      cartService.removeProduct(id);
    }

    return new ModelAndView("redirect:/cart/home");
  }
}
