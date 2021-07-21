package com.tunnelnetwork.KpOnlineStore.DAO;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

  Voucher findByVoucherName(String username);

  default Voucher getVoucher(long id) {
    return this
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
  }

  default Voucher getVoucherByName(String name) {
    return this.findByVoucherName(name);
  }
}