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
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.User;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;
import com.tunnelnetwork.KpOnlineStore.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * Includes all of the repository references and common methods to be used.
 */
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

  /**
   * Check if there is a logged in user.
   * 
   * @return boolean
   */
  protected boolean isThereLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }

    return true;
  }

  /**
   * Compare currently logged in user's role/authority.
   * 
   * @param roleName - expected role/authority of user.
   * 
   * @return boolean
   */
  protected boolean hasRole(String roleName)
  {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
  }

  /**
   * Get currently logged in user's role/authority.
   * 
   * @param model - where to pass the attributes.
   * 
   * @return void
   */
  protected void getUserRole(Model model) {

    if (!isThereLoggedInUser()) {
      model.addAttribute("userRole", "GUEST");
    }
    else {
      model.addAttribute("userRole", SecurityContextHolder.
          getContext().getAuthentication().getAuthorities().toString().replaceAll("[^a-zA-Z0-9]", ""));
    }
  }

    /**
   * Get currently logged in user's first and last name.
   * 
   * @param model - where to pass the attributes.
   * 
   * @return void
   */
  protected void getUserFirstAndLastName(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (isThereLoggedInUser()) {
      User user = userService.getUserByEmail(authentication.getName()).get();
      model.addAttribute("userFirstname", user.getFirstName());
      model.addAttribute("userLastname", user.getLastName());
    }
  }

  /**
   * Total number of products in currently logged in user's cart.
   * 
   * @param model - where to pass the attributes.
   * 
   * @return void
   */
  protected void getNumberOfProductsInCart(Model model) {

    if (isThereLoggedInUser()) {
      Cart cart = cartRepository.getCartOfUser();

      model.addAttribute("numberOfProductsInCart", cart.getNumberOfProducts());
    }
  }

  /**
   * Create a cart and new user voucher.
   * 
   * @param model - where to pass the attributes.
   * 
   * @return void
   */
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
      
    }
    else {

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

  /**
   * Add pages to a view.
   * 
   * @param model - where to pass the attributes.
   * @param maxItems - itmes per page.
   * @param changePage - page to go to.
   * @param newProductList - list divided based on max products.
   * @param productsToTransfer - all products based on filters.
   * 
   * @return void
   */
  protected void pagination(
    Model model, Integer maxItems, Integer changePage, 
    List<Product> newProductList, List<Product> productsToTransfer) {

    int endOfProducts = maxItems * changePage;

    double totalPages = productsToTransfer.size() / maxItems;
    if ((productsToTransfer.size() % maxItems) > 0 ) {
      totalPages = totalPages + 1;
    }
    model.addAttribute("totalPages" , (int) totalPages);

    // When store is at first page
    if (changePage == 1) {
      for (int i = 0; i < maxItems; i++) {
        try {
          newProductList.add(productsToTransfer.get(i));
        } catch (Exception e) {
          break;
        }
      }

      // Avoid showing NEXT when no other pages
      if (totalPages != 1 && newProductList.size() != 0) {
        model.addAttribute("showNext", true);
      }
    }
    // When it is not in first page
    if (changePage > 1) {
      for (int i = endOfProducts - maxItems; i < endOfProducts; i++) {
        try {
          newProductList.add(productsToTransfer.get(i));
        } catch (Exception e) {
          break;
        }
      }

      if (newProductList.size() != 0) {
        model.addAttribute("showBack", true);
      }

      // Avoid showing NEXT when at last page
      if (changePage != totalPages && newProductList.size() != 0) {
        model.addAttribute("showNext", true);
      }
    }
   
    // Get next page after running codes
    model.addAttribute("newPage", changePage + 1);
  }
}
