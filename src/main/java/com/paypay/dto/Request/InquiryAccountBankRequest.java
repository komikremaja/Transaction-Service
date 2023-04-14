package com.paypay.dto.Request;

import lombok.Data;

@Data
public class InquiryAccountBankRequest {
    
    private String accountNumber;
    private String bankName;
    private String accountType;
}
