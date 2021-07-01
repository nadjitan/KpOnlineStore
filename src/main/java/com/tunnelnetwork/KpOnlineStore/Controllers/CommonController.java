package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;
import com.tunnelnetwork.KpOnlineStore.Service.CartService;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;
import com.tunnelnetwork.KpOnlineStore.Service.VoucherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommonController {

  @Autowired
  private ProductService productService;

  @Autowired
  private CartService cartService;
  
  @Autowired
  private VoucherService voucherService;

  @RequestMapping("/")
  public String products(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    // Get products from database/json file
    model.addAttribute("products", productService.getAllProducts());

    // Get cart but if there is none create new cart for new user
    if (cartService.getCartOfUser() == null) {
      Cart cart = new Cart();
      cart.setCartOwner(authentication.getName());
      cart.setCreatedAt(LocalDateTime.now());
      cart.setUpdatedAt(LocalDateTime.now());

      cartService.save(cart);

      Voucher voucher = new Voucher();
      voucher.setVoucherName("New user discount");
      voucher.setVoucherOwner(authentication.getName());
      voucher.setDescription("20% off on any purchase.");
      voucher.setDiscount(20);
      voucher.setCartTotalPrice(0);
      voucher.setCreatedAt(LocalDateTime.now());
      voucher.setExpiryDate(LocalDateTime.now());

      voucherService.save(voucher);

      model.addAttribute("cart", cartService.getCartOfUser());
      model.addAttribute("voucher", voucherService.getVoucherByName(authentication.getName()));
      
    } else {
      Voucher voucher = voucherService.getVoucherByName(authentication.getName());
      double cartTotalPrice = cartService.getCartOfUser().getTotalOrderPrice();
      voucher.setCartTotalPrice(cartTotalPrice);
      
      model.addAttribute("voucher", voucher);
      model.addAttribute("cart", cartService.getCartOfUser());
    }
     
    return "index";
  }

  // Shopping cart buttons logic
  @RequestMapping(value = "/addproduct", method=RequestMethod.POST)
  @ResponseBody
  public ModelAndView addProduct(@RequestParam("add") long id, @RequestParam("productQuantity") Integer productQuantity) {
    if (!cartService.isProductInCart(id)) {
      cartService.addToCart(productService.getProduct(id), productQuantity);
    }
  
    return new ModelAndView("redirect:/");
  }
  @RequestMapping(value=" ", params={"removeProduct"})
  public String removeProduct(HttpServletRequest req){

    return "index";
  }
}
