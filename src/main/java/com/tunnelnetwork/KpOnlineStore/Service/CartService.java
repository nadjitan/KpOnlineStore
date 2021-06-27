package com.tunnelnetwork.KpOnlineStore.Service;

import javax.validation.constraints.Min;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;

import org.springframework.validation.annotation.Validated;

@Validated
public interface CartService {

    Cart getCart(@Min(value = 1L, message = "Invalid product ID.") long id);

    Cart save(Cart cart);
}