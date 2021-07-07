package com.tunnelnetwork.KpOnlineStore.DAO;

import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findDistinctProductByStatus(String status);

  List<Product> findByProductNameContainingIgnoreCase(String productName);

  List<Product> findByCategoryContainingIgnoreCase(String category);

  List<Product> findDistinctProductByCategory(String category);}
