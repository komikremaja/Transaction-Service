package com.paypay.dto.Request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TransactionExchangeRequest {
    
    @NotEmpty(message = "Field must be fill")
    private String customerName;
    @NotEmpty(message = "Field must be fill")
    private String npwp;
    @NotEmpty(message = "Field must be fill")
    private String destinationAccount;
    @NotEmpty(message = "Field must be fill")
    private String currencyPair;
    @NotNull(message = "Field must be fill")
    private BigDecimal amount1;
    @NotNull(message = "Field must be fill")
    private BigDecimal amount2;
    @NotEmpty(message = "Field must be fill")
    private String typeTransaction;
    @NotNull(message = "Field must be fill")
    private BigDecimal rateKurs;
    @NotNull(message = "Field must be fill")
    private String bankName;
    @NotEmpty(message = "Field must be fill")
    private String comments;
    @NotEmpty(message = "Field must be fill")
    private String nic;
    private String transactionStatus;
    private String vaNumber;
    private LocalDateTime createdDate;
    private LocalDate lastUpdate;
}
