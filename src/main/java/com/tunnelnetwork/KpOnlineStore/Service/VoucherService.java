package com.tunnelnetwork.KpOnlineStore.Service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.springframework.validation.annotation.Validated;

@Validated
public interface VoucherService {
  
  Voucher getVoucher(@Min(value = 1L, message = "Invalid product ID.") long id);
  
  Voucher getVoucherByName(@NotNull(message = "The username cannot be null.") @Valid String name);

  Voucher save(@NotNull(message = "The voucher cannot be null.") @Valid Voucher voucher);
}
