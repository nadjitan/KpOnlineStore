package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;
import com.tunnelnetwork.KpOnlineStore.Service.CartService;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;
import com.tunnelnetwork.KpOnlineStore.Service.VoucherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
public class CommonController {

  @Autowired
  private ProductService productService;

  @Autowired
  private CartService cartService;
  
  @Autowired
  private VoucherService voucherService;

  @GetMapping("/store")
  public String products(Model model) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }

    // Get products from database/json file
    model.addAttribute("products", productService.getAllProducts());
    // Cart for user
    createCartAndVoucher(model);
     
    return "store";
  }
  @GetMapping("/profile")
  public String profilePage(Model model) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }
    
    return "profile";
  }
  @GetMapping("/product/{id}")
  public String productPage(Model model, @PathVariable("id") long id) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }

    model.addAttribute("product", productService.getProduct(id));
    return "product-details";
  }
  @GetMapping("/cart/home")
  public String cartHomePage(Model model) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }
    
    createCartAndVoucher(model);
    return "cart";
  }
  @GetMapping("/checkout")
  public String checkoutPage(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }
    
    Voucher voucher = voucherService.getVoucherByName(authentication.getName());
    double cartTotalPrice = cartService.getCartOfUser().getTotalOrderPrice();
    voucher.setCartTotalPrice(cartTotalPrice);
    
    model.addAttribute("voucher", voucher);
    model.addAttribute("cart", cartService.getCartOfUser());
    return "checkout";
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

  public void createCartAndVoucher(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Get cart but if there is none create new cart for new user
    if (cartService.getCartOfUser() == null) {
      Voucher voucher = new Voucher();
      voucher.setVoucherName("New user discount");
      voucher.setVoucherOwner(authentication.getName());
      voucher.setDescription("20% off on any purchase.");
      voucher.setDiscount(20);
      voucher.setCartTotalPrice(0);
      voucher.setCreatedAt(LocalDateTime.now());
      voucher.setExpiryDate(LocalDateTime.now());

      Cart cart = new Cart();
      cart.setCartOwner(authentication.getName());
      cart.setCreatedAt(LocalDateTime.now());
      cart.setUpdatedAt(LocalDateTime.now());
      cart.getVouchers().add(voucher);

      voucherService.save(voucher);
      cartService.save(cart);

      model.addAttribute("cart", cartService.getCartOfUser());
      model.addAttribute("voucher", voucherService.getVoucherByName(authentication.getName()));
      
    } else {
      Voucher voucher = voucherService.getVoucherByName(authentication.getName());
      double cartTotalPrice = cartService.getCartOfUser().getTotalOrderPrice();
      voucher.setCartTotalPrice(cartTotalPrice);
      
      model.addAttribute("voucher", voucher);
      model.addAttribute("cart", cartService.getCartOfUser());
    }
  }

  public boolean isThereLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }

    return true;
  }
}
