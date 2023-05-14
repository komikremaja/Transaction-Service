package com.paypay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "transaction_data")
public class TransactionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private BigDecimal idTransaction;

    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "npwp")
    private String npwp;

    @Column(name = "destination_account")
    private String destinationAccount;

    @Column(name = "currency_pair")
    private String currencyPair;

    @Column(name = "amount1")
    private BigDecimal amount1;

    @Column(name = "amount2")
    private BigDecimal amount2;

    @Column(name = "type_transaction")
    private String typeTransaction;

    @Column(name = "rate_kurs")
    private String rateKurs;
    
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "comments")
    private String comments;

    @Column(name = "nic")
    private String nic;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "va_number")
    private String vaNumber;
    
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "last_update")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

}
