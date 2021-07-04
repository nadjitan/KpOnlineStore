package com.tunnelnetwork.KpOnlineStore.Service;

import javax.transaction.Transactional;

import com.tunnelnetwork.KpOnlineStore.DAO.ReceiptRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Receipt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

  @Autowired
  private ReceiptRepository receiptRepository;

  @Override
  public Receipt getReceipt(long id) {
    return receiptRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));
  }

  @Override
  public Iterable<Receipt> getReceiptsByName(String username) {
    return receiptRepository.findAllByReceiptOwner(username);
  }

  @Override
  public Receipt save(Receipt receipt) {
    return receiptRepository.saveAndFlush(receipt);
  }
}
