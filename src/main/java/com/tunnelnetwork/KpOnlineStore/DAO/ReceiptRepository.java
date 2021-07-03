package com.tunnelnetwork.KpOnlineStore.DAO;

import com.tunnelnetwork.KpOnlineStore.Models.Receipt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

  Receipt findByReceiptOwner(String username);

  Iterable<Receipt> findAllByReceiptOwner(String username);}
