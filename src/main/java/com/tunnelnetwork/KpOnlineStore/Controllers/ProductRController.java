package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductRController {
  
  @Autowired
  private ProductService productService;

  @GetMapping("/list")
  private Iterable<Product> list() {
    return productService.getAllProducts();
  }

  @GetMapping("/{id}")
  private Product findProduct(@PathVariable("id") long id) {
    return productService.getProduct(id);
  }
}
