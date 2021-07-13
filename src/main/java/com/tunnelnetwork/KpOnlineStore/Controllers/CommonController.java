package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.DAO.CartRepository;
import com.tunnelnetwork.KpOnlineStore.DAO.CommentRepository;
import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.DAO.ReceiptRepository;
import com.tunnelnetwork.KpOnlineStore.DAO.VoucherRepository;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;
import com.tunnelnetwork.KpOnlineStore.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class CommonController {

  @Autowired
  protected ProductRepository productRepository;

  @Autowired
  protected CartRepository cartRepository;
  
  @Autowired
  protected VoucherRepository voucherRepository;

  @Autowired
  protected CommentRepository commentRepository;

  @Autowired
  protected ReceiptRepository receiptRepository;
  
  @Autowired
  protected UserService userService;

  protected void createCartAndVoucher(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Get cart but if there is none create new cart for new user
    if (cartRepository.getCartOfUser() == null) {
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

      voucherRepository.saveAndFlush(voucher);
      cartRepository.saveAndFlush(cart);

      model.addAttribute("cart", cartRepository.getCartOfUser());
      
    } else {
      Cart cart = cartRepository.getCartOfUser();
      Voucher voucher = voucherRepository.getVoucherByName("New user discount");

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

  protected boolean hasRole (String roleName)
  {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
  }
}
