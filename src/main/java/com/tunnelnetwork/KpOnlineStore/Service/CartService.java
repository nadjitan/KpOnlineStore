package com.tunnelnetwork.KpOnlineStore.Service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;

import org.springframework.validation.annotation.Validated;

@Validated
public interface CartService {

    @NotNull Iterable<Cart> getAllCarts();

    Cart getCart(@Min(value = 1L, message = "Invalid product ID.") long id);

    Cart findCartOwner(@NotNull(message = "The user details cannot be null.") @Valid String username);

    Cart save(Cart cart);
}