package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CheckoutController extends CommonController {

  @GetMapping("/checkout")
  private String checkoutPage(Model model, @RequestParam("useVoucher") Optional<Integer> check) {
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

  @RequestMapping(value="/checkout", method=RequestMethod.POST)
  private ModelAndView goCheckout() {
    if (!isThereLoggedInUser() || 
        cartService.getCartOfUser() == null || 
        cartService.getCartOfUser().getCartProducts().isEmpty()) {
      return new ModelAndView("redirect:/");
    }

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
        voucher.removeUserInList(cart.getCartOwner());
        receiptVoucherList.add(voucher);
      }

      cartService.removeVouchers(cart.getCartOwner());

      receipt.setVoucherList(receiptVoucherList);
    }

    cartService.removeProducts(cart.getCartOwner());
    receiptService.save(receipt);
    
    return new ModelAndView("redirect:/profile");
  }
}
