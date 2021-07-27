package com.tunnelnetwork.KpOnlineStore.RepositoryTests;

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
    product.setPrice((double) 200);
    product.setProductName("Cream");

    productId = testEntityManager.persistAndFlush(product).getId();
  }

  @Test
  public void whenFindByProductNameIgnoreCase_thenProductShouldBeFound() {

    // Given
    String productName = "cream";

    // When
    List<Product> productList = productRepository.findByProductNameContainingIgnoreCase(productName);

    // Then
    assertFalse(productList.isEmpty());
  }

  @Test
  public void whenInvalidProductName_thenProductShouldNotBeFound() {

    // Given
    String productName = "guitar";

    // When
    List<Product> productList = productRepository.findByProductNameContainingIgnoreCase(productName);

    // Then
    assertTrue(productList.isEmpty());
  }

  @Test
  public void whenFindById_thenProductShouldBeFound() {

    // When
    Optional<Product> product = productRepository.findById(productId);

    // Then
    assertTrue(product.isPresent());
  }

  @Test
  public void whenInvalidId_thenProductShouldNotBeFound() {

    // Given
    long id = -100;

    // When
    Optional<Product> product = productRepository.findById(id);

    // Then
    assertFalse(product.isPresent());
  }
}
