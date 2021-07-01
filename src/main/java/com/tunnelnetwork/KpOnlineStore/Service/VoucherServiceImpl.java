package com.tunnelnetwork.KpOnlineStore.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.DAO.VoucherRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VoucherServiceImpl implements VoucherService {

  @Autowired
  private VoucherRepository voucherRepository;

  @Override
  public Voucher getVoucher(long id) {
    return voucherRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
  }

  @Override
  public Voucher save(Voucher voucher) {
    return voucherRepository.saveAndFlush(voucher);
  }

  @Override
  public Voucher getVoucherByName(@NotNull(message = "The username cannot be null.") @Valid String username) {
    return voucherRepository.findByVoucherOwner(username);
  }
  
}
