package com.tunnelnetwork.KpOnlineStore.Service;

import org.springframework.validation.annotation.Validated;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

@Validated
public interface ProductService {

    @NotNull Iterable<Product> getAllProducts();

    Product getProduct(@Min(value = 1L, message = "Invalid product ID.") long id);

    List<Product> save(@NotNull(message = "The product cannot be null.") @Valid List<Product> product);
}
