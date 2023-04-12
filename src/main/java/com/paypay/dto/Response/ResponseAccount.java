package com.paypay.dto.Response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ResponseAccount {
    private Integer idAccount;

    private String accountNumber;

    private String accountStatus;
    
    private String bankName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
}
