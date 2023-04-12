package com.paypay.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypay.model.BankData;

@Repository
public interface BankDataRepo extends JpaRepository<BankData, BigDecimal>{
    
}
