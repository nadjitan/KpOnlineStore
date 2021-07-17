package com.tunnelnetwork.KpOnlineStore;

import com.tunnelnetwork.KpOnlineStore.DAO.VoucherRepository;
import com.tunnelnetwork.KpOnlineStore.Models.User;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class VoucherRepositoryIntegrationTests {

  @Autowired
  private VoucherRepository voucherRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private long voucherId;
  
  @BeforeEach
  public void setup() {
    
    List<String> userList = new ArrayList<String>();

    User user = new User();
    user.setEmail("test@mail.com");
    user.setEnabled(true);
    user.setFirstName("test");
    user.setLastName("test");

    userList.add(user.getEmail());

    Voucher voucher = new Voucher();
    voucher.setDescription("Voucher test");
    voucher.setDiscount(20);
    voucher.setUserList(userList);
    voucher.setVoucherName("Voucher");

    testEntityManager.persistAndFlush(user);
    voucherId = testEntityManager.persistAndFlush(voucher).getId();
  }

  @Test
  public void whenFindByVoucherName_thenVoucherShouldBeFound() {

    String voucherName = "Voucher";
    Voucher voucher = voucherRepository.findByVoucherName(voucherName);

    assertEquals(voucherName, voucher.getVoucherName());
  }

  @Test
  public void whenInvalidVoucherName_thenVoucherShouldNotBeFound() {

    String voucherName = "NonExistentVoucher";
    Voucher voucher = voucherRepository.findByVoucherName(voucherName);

    assertNull(voucher);
  }

  @Test
  public void whenFindById_thenVoucherShouldBeFound() {

    long id = 1;
    Optional<Voucher> voucher = voucherRepository.findById(id);
    System.out.println("VOUCHERS: " + voucherRepository.findAll());
    assertTrue(voucher.isPresent());
  }

  @Test
  public void whenInvalidId_thenVoucherShouldNotBeFound() {

    long id = -100;
    Optional<Voucher> voucher = voucherRepository.findById(id);

    assertFalse(voucher.isPresent());
  }

  @Test
  public void given1DuplicateUserName_whenAddedToVoucher_thenReturnException() {

    // Get exact copy of a name in voucher
    Voucher voucherFromDb = testEntityManager.find(Voucher.class, voucherId);

    String duplicateUserName = voucherFromDb.getUserList().get(0);
    
    assertThrows(DataIntegrityViolationException.class, () -> {
      voucherFromDb.addUserInList(duplicateUserName);
      
      voucherRepository.saveAndFlush(voucherFromDb);
      
      testEntityManager.persistAndFlush(voucherFromDb);
    });
  }
}
