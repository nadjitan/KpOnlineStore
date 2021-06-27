package com.tunnelnetwork.KpOnlineStore.Service;

import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
      this.productRepository = productRepository;
    }

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
      return productRepository.save(product);
    }
}