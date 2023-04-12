package com.paypay.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypay.model.TransactionThreshold;

@Repository
public interface TransactionThresholdRepo extends JpaRepository<TransactionThreshold, BigDecimal> {
    
    TransactionThreshold findByNic(String nic);
}
