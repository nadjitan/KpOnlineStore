package com.tunnelnetwork.KpOnlineStore.DAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
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

  default List<Product> getProductsByStatus(String status) {
    return this.findDistinctProductByStatus(status);
  }

  default List<Product> getProductsContainingInName(String productName) {
    return this.findByProductNameContainingIgnoreCase(productName);
  }

  default List<Product> getProductsContainingInCategory(String category) {
    return this.findByCategoryContainingIgnoreCase(category);
  }

  default List<Product> getProductsContainingInBand(String bandName) {
    List<Product> allProducts = getAllProducts();
    List<Product> productsBasedOnBand = new ArrayList<Product>();

    for (Product product : allProducts) {
      List<String> tags = Arrays.asList(product.getTags());

      for (String tag : tags) {
        if (tag.replaceAll("[^a-zA-Z0-9]", "").toUpperCase().equals(bandName.replaceAll("[^a-zA-Z0-9]", "").toUpperCase())) {
          productsBasedOnBand.add(product);

          break;
        }
      }
    }


    return productsBasedOnBand;
  }

  default List<Product> getProductsByBand(String bandName) {
    List<Product> allProducts = getAllProducts();
    List<Product> productsBasedOnBand = new ArrayList<Product>();

    for (Product product : allProducts) {
      if (Arrays.asList(product.getTags()).contains(bandName)) {
        productsBasedOnBand.add(product);
      }
    }

    return productsBasedOnBand;
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
