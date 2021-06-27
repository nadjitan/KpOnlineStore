package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
  
  @Autowired
  private ProductService productService;

  @GetMapping("/products/list")
  public Iterable<Product> list() {
    return productService.getAllProducts();
  }
}
