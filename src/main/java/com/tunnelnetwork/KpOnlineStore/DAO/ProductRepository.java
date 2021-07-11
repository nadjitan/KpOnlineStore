package com.tunnelnetwork.KpOnlineStore.DAO;

import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findDistinctProductByStatus(String status);

  List<Product> findByProductNameContainingIgnoreCase(String productName);

  List<Product> findByCategoryContainingIgnoreCase(String category);

  List<Product> findDistinctProductByCategory(String category);

  default List<Product> getAllProducts() {
    return this.findAll();
  }

  default Product getProduct(long id) {
    return this
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
  }

  default List<Product> save(List<Product> product) {
    return this.saveAllAndFlush(product);
  }

  default Integer size() {
    return this.findAll().size();
  }

  default void delete(Product product) {
    this.delete(product);
  }

  default List<Product> getProductsByStatus(String status) {
    return this.findDistinctProductByStatus(status);
  }

  default List<Product> getProductsContainingInName(String productName) {
    return this.findByProductNameContainingIgnoreCase(productName);
  }

  default List<Product> getProductsContainingInCategory(String category) {
    return this.findByCategoryContainingIgnoreCase(category);
  }

  default List<Product> getProductsByCategory(String category) {
    return this.findDistinctProductByCategory(category);
  }

  default List<Product> getProductsByBestSeller(List<Product> listToSort) {
    listToSort.sort(new SoldProductsSorter());
    
    List<Product> bestSellers = new ArrayList<Product>();

    for (int i = 0; i < 10; i++) {
      try {
        bestSellers.add(listToSort.get(i));
      } catch (Exception e) {
        break;
      }
    }
    
    return bestSellers;
  }
}
