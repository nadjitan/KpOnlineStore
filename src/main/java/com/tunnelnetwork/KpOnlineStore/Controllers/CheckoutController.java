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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController extends CommonController {

  @GetMapping("/checkout")
  private String checkoutPage(Model model, @RequestParam("useVoucher") Optional<Integer> check) {

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    if (!isThereLoggedInUser() && cartRepository.getCartOfUser() == null) {
      return "redirect:/login";
    }

    Cart cart = cartRepository.getCartOfUser();
    if (check.isPresent()) {
      cart.setUseVoucher(1);
    } else {
      cart.setUseVoucher(0);
    }

    cartRepository.saveAndFlush(cart);

    model.addAttribute("cart", cart);
    return "checkout";
  }

  @PostMapping("/checkout")
  private String goCheckout(
    @RequestParam("firstName") Optional<String> firstName,
    @RequestParam("lastName") Optional<String> lastName,
    @RequestParam("address") Optional<String> address,
    @RequestParam("region") Optional<String> region,
    @RequestParam("city") Optional<String> city,
    @RequestParam("postalCode") Optional<String> postalCode,
    @RequestParam("phoneNumber") Optional<String> phoneNumber,
    @RequestParam("ccn") Optional<String> ccn,
    @RequestParam("expiration-date-1") Optional<Integer> expirationDate1,
    @RequestParam("expiration-date-2") Optional<Integer> expirationDate2,
    @RequestParam("securityCode") Optional<String> securityCode,
    @RequestParam("useCc") Optional<String> useCc
    ) {

    if (!isThereLoggedInUser() || 
        cartRepository.getCartOfUser() == null || 
        cartRepository.getCartOfUser().getCartProducts().isEmpty()) {
      return "redirect:/";
    }

    makeReceipt();
    
    return "redirect:/profile";
  }

  private void makeReceipt() {
    Cart cart = cartRepository.getCartOfUser();
    Receipt receipt = new Receipt();

    List<Product> cartProductList = cart.getCartProducts();
    List<Product> receiptProductList = new ArrayList<Product>();

    for (Product product : cartProductList) {
      receiptProductList.add(product);

      Product productFromDB = productRepository.getProduct(product.getId());
      productFromDB.setNumberOfSold(productFromDB.getNumberOfSold() + product.getQuantity());

      productRepository.saveAndFlush(productFromDB);
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

      cartRepository.removeVouchers(cart.getCartOwner());

      receipt.setVoucherList(receiptVoucherList);
    }

    cartRepository.removeProducts(cart.getCartOwner());
    receiptRepository.saveAndFlush(receipt);
  }
}
