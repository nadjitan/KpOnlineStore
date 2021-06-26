package com.tunnelnetwork.KpOnlineStore.Service;

import java.util.List;

import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
  
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Iterable<Product> list() {
    return productRepository.findAll();
  }

  public Product save(Product product) {
    return productRepository.save(product);
  }

  public void save(List<Product> products) {
    productRepository.saveAll(products);
  }
}
