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
  public Iterable<Product> getAllProducts() {
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
}