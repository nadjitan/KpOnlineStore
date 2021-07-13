package com.tunnelnetwork.KpOnlineStore.DAO;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

  Iterable<Receipt> findAllByReceiptOwner(String username);

  default Receipt getReceipt(long id) {
    return this
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));
  }

  default Iterable<Receipt> getReceiptsByName(String username) {
    return this.findAllByReceiptOwner(username);
  }
}
