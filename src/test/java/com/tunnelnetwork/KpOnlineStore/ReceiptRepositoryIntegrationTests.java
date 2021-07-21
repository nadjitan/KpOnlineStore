package com.tunnelnetwork.KpOnlineStore;

import com.tunnelnetwork.KpOnlineStore.DAO.ReceiptRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;
import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.User;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class ReceiptRepositoryIntegrationTests {

  @Autowired
  private ReceiptRepository receiptRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private long cartId;
  private long receiptId;

  @BeforeEach
  public void setup() {
    
    List<String> userList = new ArrayList<String>();
    List<Product> productList = new ArrayList<Product>();
    List<Voucher> voucherList = new ArrayList<Voucher>();

    User user = new User();
    user.setEmail("test@mail.com");
    user.setEnabled(true);
    user.setFirstName("test");
    user.setLastName("test");

    userList.add(user.getEmail());

    Product product = new Product();
    product.setPrice(200);
    product.setProductName("Cream");

    productList.add(product);

    Voucher voucher = new Voucher();
    voucher.setDescription("Voucher test");
    voucher.setDiscount(20);
    voucher.setUserList(userList);
    voucher.setVoucherName("Voucher");

    voucherList.add(voucher);

    Cart cart = new Cart();
    cart.setCartOwner(user.getEmail());
    cart.setCartProducts(productList);
    cart.setVouchers(voucherList);

    Receipt receipt = new Receipt();
    receipt.setReceiptOwner(user.getEmail());

    testEntityManager.persistAndFlush(user);
    testEntityManager.persistAndFlush(product);
    testEntityManager.persistAndFlush(voucher);
    this.cartId = testEntityManager.persistAndFlush(cart).getId();
    this.receiptId = testEntityManager.persistAndFlush(receipt).getId();
  }

  @Test
  public void givenUserCheckout_whenCartInfoGoesToReceipt_thenReceiptIsSaved() {
    Receipt receiptFromDb = testEntityManager.find(Receipt.class, receiptId);
    Cart cartFromDb = testEntityManager.find(Cart.class, cartId);

    LocalDateTime localDateTime = LocalDateTime.now();
    List<Product> cartProducts = cartFromDb.getCartProducts();
    String cartOwner = cartFromDb.getCartOwner();
    List<Voucher> cartVouchers = cartFromDb.getVouchers();

    receiptFromDb.setCreatedAt(localDateTime);
    receiptFromDb.setProductList(cartProducts);
    receiptFromDb.setReceiptOwner(cartOwner);
    receiptFromDb.setVoucherList(cartVouchers);

    testEntityManager.detach(cartFromDb);
    Receipt receipt = testEntityManager.persistAndFlush(receiptFromDb);

    assertEquals(localDateTime, receipt.getCreatedAt());
    assertIterableEquals(cartProducts, receipt.getProductList());
    assertEquals(cartOwner, receipt.getReceiptOwner());
    assertIterableEquals(cartVouchers, receipt.getVoucherList());
  }

  @Test
  public void whenFindAllByReceiptOwner_thenReceiptShouldBeFound() {

    String email = "test@mail.com";
    Iterable<Receipt> receipt = receiptRepository.findAllByReceiptOwner(email);  
    
    assertEquals(email, receipt.iterator().next().getReceiptOwner());
  }

  @Test
  public void whenInvalidReceiptOwner_thenReceiptShouldNotBeFound() {

    String email = "notregisteredmail@mail.com";
    Iterable<Receipt> receipt= receiptRepository.findAllByReceiptOwner(email);

    assertEquals(StreamSupport.stream(receipt.spliterator(), false).count(), 0);
  }

  @Test
  public void whenFindById_thenReceiptShouldBeFound() {

    Optional<Receipt> receipt = receiptRepository.findById(this.receiptId);

    assertTrue(receipt.isPresent());
  }

  @Test
  public void whenInvalidId_thenReceiptShouldNotBeFound() {

    long id = -100;
    Optional<Receipt> receipt = receiptRepository.findById(id);

    assertFalse(receipt.isPresent());
  }
}
