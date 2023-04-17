package com.paypay.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paypay.model.TransactionData;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionData, BigDecimal> {
    @Query("SELECT DISTINCT t FROM TransactionData t WHERE t.bankName=:bankName AND DATE(t.createdDate)=CURDATE() ORDER BY 1 DESC")
    List<TransactionData> findVaNumberSequence(@Param("bankName") String bankName);

    TransactionData findByVaNumber(String vaNumber);

    @Query("SELECT t FROM TransactionData t WHERE t.nic=:nic ORDER BY 1 ASC")
    List<TransactionData> findHistoryTransactionlist(@Param("nic") String nic);
}
