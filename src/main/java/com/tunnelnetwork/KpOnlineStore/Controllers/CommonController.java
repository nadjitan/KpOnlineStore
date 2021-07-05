package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import com.tunnelnetwork.KpOnlineStore.Service.CartService;
import com.tunnelnetwork.KpOnlineStore.Service.CommentService;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;
import com.tunnelnetwork.KpOnlineStore.Service.ReceiptService;
import com.tunnelnetwork.KpOnlineStore.Service.VoucherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class CommonController {

  @Autowired
  protected ProductService productService;

  @Autowired
  protected CartService cartService;
  
  @Autowired
  protected VoucherService voucherService;

  @Autowired
  protected CommentService commentService;

  @Autowired
  protected ReceiptService receiptService;
  

  protected void createCartAndVoucher(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Get cart but if there is none create new cart for new user
    if (cartService.getCartOfUser() == null) {
      Voucher voucher = new Voucher();
      voucher.setVoucherName("New user discount");
      voucher.addUserInList(authentication.getName());
      voucher.setDescription("20% off on any purchase.");
      voucher.setDiscount(20);
      voucher.setCreatedAt(LocalDateTime.now());
      voucher.setExpiryDate(LocalDateTime.now());

      Cart cart = new Cart();
      cart.setCartOwner(authentication.getName());
      List<Voucher> cartVouchers = new ArrayList<Voucher>();
      cartVouchers.add(voucher);
      cart.setVouchers(cartVouchers);
      cart.setCreatedAt(LocalDateTime.now());
      cart.setUpdatedAt(LocalDateTime.now());

      voucherService.save(voucher);
      cartService.save(cart);

      model.addAttribute("cart", cartService.getCartOfUser());
      
    } else {
      Cart cart = cartService.getCartOfUser();
      Voucher voucher = voucherService.getVoucherByName("New user discount");

      if (voucher.isUserInList(authentication.getName())) {
        List<Voucher> cartVouchers = new ArrayList<Voucher>();
        cartVouchers.add(voucher);
        cart.setVouchers(cartVouchers);
      }
      
      model.addAttribute("cart", cart);
    }
  }

  protected boolean isThereLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }

    return true;
  }
}
