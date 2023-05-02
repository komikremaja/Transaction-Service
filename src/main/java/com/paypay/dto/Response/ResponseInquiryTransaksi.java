package com.paypay.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ResponseInquiryTransaksi {
    
    private BigDecimal idTransaction;

    private String customerName;

    private String npwp;

    private String destinationAccount;

    private String currencyPair;

    private BigDecimal amount1;

    private BigDecimal amount2;

    private String typeTransaction;

    private String rateKurs;

    private String bankName;

    private String comments;

    private String nic;

    private String transactionStatus;

    private String vaNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;
}
