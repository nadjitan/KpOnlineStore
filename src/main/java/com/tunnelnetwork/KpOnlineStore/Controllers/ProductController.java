package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
  
  private ProductService productService;

  public ProductController(ProductService productService) {
      this.productService = productService;
  }

  @GetMapping("/list")
  public Iterable<Product> list() {
      return productService.getAllProducts();
  }
}
