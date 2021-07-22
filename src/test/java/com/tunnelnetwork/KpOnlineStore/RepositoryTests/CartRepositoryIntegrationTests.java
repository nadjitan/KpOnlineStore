package com.tunnelnetwork.KpOnlineStore.RepositoryTests;

import com.tunnelnetwork.KpOnlineStore.DAO.CartRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.User;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class CartRepositoryIntegrationTests {
  
  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private TestEntityManager testEntityManager;
  
  private long cartId;

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

    testEntityManager.persistAndFlush(user);
    testEntityManager.persistAndFlush(product);
    testEntityManager.persistAndFlush(voucher);
    cartId = testEntityManager.persistAndFlush(cart).getId();
  }

  @Test
  public void whenFindByCartOwner_thenCartShouldBeFound() {
    
    // Given
    String email = "test@mail.com";

    // When
    Cart cart = cartRepository.findByCartOwner(email);  
    
    // Then
    assertEquals(email, cart.getCartOwner());
  }

  @Test
  public void whenInvalidCartOwner_thenCartShouldNotBeFound() {

    // Given
    String email = "notregisteredmail@mail.com";

    // When
    Cart cart = cartRepository.findByCartOwner(email);
    
    // Then
    assertNull(cart);
  }

  @Test
  public void whenFindById_thenCartShouldBeFound() {

    // When
    Optional<Cart> cart = cartRepository.findById(cartId);

    // Then
    assertTrue(cart.isPresent());
  }

  @Test
  public void whenInvalidId_thenCartShouldNotBeFound() {
    
    // Given
    long id = -100;

    //When
    Optional<Cart> cart = cartRepository.findById(id);

    // Then
    assertFalse(cart.isPresent());
  }

  @Test
  public void given1DuplicateProduct_whenAddedToCart_thenReturnException() {

    // Given
    // Get exact copy of a product in cart
    Cart cartFromDb = testEntityManager.find(Cart.class, cartId);
    Product duplicateProduct = cartFromDb.getCartProducts().get(0);

    // When
    cartFromDb.getCartProducts().add(duplicateProduct);
    
    // Then
    assertThrows(DataIntegrityViolationException.class, () -> {
      
      cartRepository.saveAndFlush(cartFromDb);
      
      testEntityManager.persistAndFlush(cartFromDb);
    });
  }
}
