package com.tunnelnetwork.KpOnlineStore.Service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Receipt;

import org.springframework.validation.annotation.Validated;

@Validated
public interface ReceiptService {
  
  Receipt getReceipt(@Min(value = 1L, message = "Invalid receipt ID.") long id);
  
  Iterable<Receipt> getReceiptsByName(@NotNull(message = "The username cannot be null.") @Valid String username);

  Receipt save(@NotNull(message = "The receipt cannot be null.") @Valid Receipt receipt);
}
