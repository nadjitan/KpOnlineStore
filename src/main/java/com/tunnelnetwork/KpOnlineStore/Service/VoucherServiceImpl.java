package com.tunnelnetwork.KpOnlineStore.Service;

import javax.transaction.Transactional;

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
  public Voucher getVoucherByName(String name) {
    return voucherRepository.findByVoucherName(name);
  }
  
}
