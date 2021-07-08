package com.tunnelnetwork.KpOnlineStore.Service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.validation.annotation.Validated;

@Validated
public interface CartService {

    @NotNull Iterable<Cart> getAllCarts();

    Cart getCart(@Min(value = 1L, message = "Invalid product ID.") long id);

    Cart save(@NotNull(message = "The cart cannot be null.") @Valid Cart cart);

    boolean isProductInCart(long id);

    Cart getCartOfUser();

    Cart addToCart(@NotNull(message = "The product cannot be null.") @Valid Product product, @Valid Integer productQuantity);

    void removeProduct(@Min(value = 1L, message = "The id cannot be null.") long id);

    void removeProducts(@NotNull(message = "The username cannot be null.") @Valid String username);

    void removeVouchers(@NotNull(message = "The username cannot be null.") @Valid String username);
}