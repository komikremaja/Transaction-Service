package com.paypay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bank_data")
public class BankData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bank")
    private BigDecimal idBank;
    
    @Column(name = "bank_name")
    private BigDecimal bankName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
}
