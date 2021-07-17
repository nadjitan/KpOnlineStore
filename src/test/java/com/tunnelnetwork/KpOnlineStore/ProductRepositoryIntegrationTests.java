package com.tunnelnetwork.KpOnlineStore;

import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ProductRepositoryIntegrationTests {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private long productId;
  
  @BeforeEach
  public void setup() {

    Product product = new Product();
    product.setPrice(200);
    product.setProductName("Cream");

    productId = testEntityManager.persistAndFlush(product).getId();
  }

  @Test
  public void whenFindByProductNameIgnoreCase_thenProductShouldBeFound() {

    String productName = "cream";
    List<Product> productList = productRepository.findByProductNameContainingIgnoreCase(productName);

    assertFalse(productList.isEmpty());
  }

  @Test
  public void whenInvalidProductName_thenProductShouldNotBeFound() {

    String productName = "guitar";
    List<Product> productList = productRepository.findByProductNameContainingIgnoreCase(productName);

    assertTrue(productList.isEmpty());
  }

  @Test
  public void whenFindById_thenProductShouldBeFound() {

    Optional<Product> product = productRepository.findById(productId);

    assertTrue(product.isPresent());
  }

  @Test
  public void whenInvalidId_thenProductShouldNotBeFound() {

    long id = -100;
    Optional<Product> product = productRepository.findById(id);

    assertFalse(product.isPresent());
  }
}
