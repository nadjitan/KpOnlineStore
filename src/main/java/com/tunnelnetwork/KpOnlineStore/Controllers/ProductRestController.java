package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductRestController {

  @Autowired
  private ProductRepository productRepository;

  @GetMapping("/list")
  private Iterable<Product> list() {
    return productRepository.getAllProducts();
  }

  @GetMapping("/{id}")
  private Product findProduct(@PathVariable("id") long id) {
    return productRepository.getProduct(id);
  }
}
