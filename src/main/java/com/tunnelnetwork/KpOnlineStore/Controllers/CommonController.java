package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Comment;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;
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

  @Autowired
  private ReceiptService receiptService;

  @Autowired
  private CommentService commentService;

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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Iterable<Receipt> receiptOfUser = receiptService.getReceiptsByName(authentication.getName());

    model.addAttribute("receiptList", receiptOfUser);
    
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
    if (!isThereLoggedInUser() || cartService.getCartOfUser() == null) {
      return "redirect:/";
    }

    model.addAttribute("cart", cartService.getCartOfUser());
    return "cart";
  }
  @GetMapping("/checkout")
  public String checkoutPage(Model model, @RequestParam("useVoucher") Optional<Integer> check) {
    if (!isThereLoggedInUser() && cartService.getCartOfUser() == null) {
      return "redirect:/";
    }

    Cart cart = cartService.getCartOfUser();
    if (check.isPresent()) {
      cart.setUseVoucher(1);
    } else {
      cart.setUseVoucher(0);
    }

    cartService.save(cart);

    model.addAttribute("cart", cart);
    return "checkout";
  }

  @RequestMapping(value = "/comment", method=RequestMethod.POST)
  @ResponseBody
  public ModelAndView makeComment(@RequestParam("userComment") String comment, @RequestParam("productId") long id) {
    if (!comment.isBlank()) {
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
  @RequestMapping(value="/checkout", method=RequestMethod.POST)
  public ModelAndView goCheckout() {
    Cart cart = cartService.getCartOfUser();
    Receipt receipt = new Receipt();

    List<Product> cartProductList = cart.getCartProducts();
    List<Product> receiptProductList = new ArrayList<Product>();

    for (Product product : cartProductList) {
      receiptProductList.add(product);
    }
    
    receipt.setCreatedAt(LocalDateTime.now());
    receipt.setReceiptOwner(cart.getCartOwner());
    receipt.setProductList(receiptProductList);
    

    if (cart.getUseVoucher() == 1) {
      List<Voucher> cartVoucherList = cart.getVouchers();
      List<Voucher> receiptVoucherList = new ArrayList<Voucher>();

      for (Voucher voucher : cartVoucherList) {
        receiptVoucherList.add(voucher);
      }

      cartService.removeVouchers(cart.getCartOwner());
      receipt.setVoucherList(receiptVoucherList);
    }

    cartService.removeProducts(cart.getCartOwner());
    receiptService.save(receipt);
    
    return new ModelAndView("redirect:/profile");
  }

  public void createCartAndVoucher(Model model) {
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

  public boolean isThereLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }

    return true;
  }
}
