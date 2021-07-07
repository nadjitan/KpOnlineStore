package com.tunnelnetwork.KpOnlineStore.Service;

import java.util.List;

import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
  
  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public Product getProduct(long id) {
    return productRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
  }

  @Override
  public Product save(Product product) {
    return productRepository.saveAndFlush(product);
  }

  @Override
  public List<Product> save(List<Product> product) {
    return productRepository.saveAllAndFlush(product);
  }

  @Override
  public Integer size() {
    return productRepository.findAll().size();
  }

  @Override
  public void delete(Product product) {
    productRepository.delete(product);
  }

  @Override
  public List<Product> getProductsByStatus(String status) {
    return productRepository.findDistinctProductByStatus(status);
  }

  @Override
  public List<Product> getProductsContainingInName(String productName) {
    return productRepository.findByProductNameContainingIgnoreCase(productName);
  }

  @Override
  public List<Product> getProductsContainingInCategory(String category) {
    return productRepository.findByCategoryContainingIgnoreCase(category);
  }

  @Override
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findDistinctProductByCategory(category);
  }
}