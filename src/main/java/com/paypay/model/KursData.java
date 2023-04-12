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
@Table(name = "kurs_data")
public class KursData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kurs")
    private BigDecimal idKurs;

    @Column(name = "id_bank")
    private BigDecimal idBank;
    
    @Column(name = "currency")
    private String currency;

    @Column(name = "kurs_buy")
    private BigDecimal kursBuy;

    @Column(name = "kurs_bi")
    private BigDecimal kursBi;

    @Column(name = "kurs_margin_buy")
    private BigDecimal kursMarginBuy;

    @Column(name = "kurs_margin_sell")
    private BigDecimal kursMarginSell;

    @Column(name = "kurs_sell")
    private BigDecimal kursSell;

    @Column(name = "created_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    
    @Column(name = "last_update")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;
    
    
}
