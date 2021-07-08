package com.tunnelnetwork.KpOnlineStore.Service;

import org.springframework.validation.annotation.Validated;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

@Validated
public interface ProductService {

    @NotNull List<Product> getAllProducts();

    Product getProduct(@Min(value = 1L, message = "Invalid product ID.") long id);

    List<Product> getProductsByStatus(@NotNull(message = "The status cannot be null.") @Valid String status);

    List<Product> getProductsByCategory(@NotNull(message = "The category cannot be null.") @Valid String category);

    List<Product> getProductsContainingInName(@NotNull(message = "The name cannot be null.") @Valid String productName);

    List<Product> getProductsContainingInCategory(@NotNull(message = "The category cannot be null.") @Valid String category);

    Product save(@NotNull(message = "The product cannot be null.") @Valid Product product);

    Integer size();

    List<Product> save(@NotNull(message = "The product cannot be null.") @Valid List<Product> product);

    void delete(@NotNull(message = "The product cannot be null.") @Valid Product product);
}
