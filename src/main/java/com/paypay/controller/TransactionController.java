package com.paypay.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypay.dto.Request.ReconInquiryPaymentStatusRequest;
import com.paypay.dto.Request.TransactionExchangeRequest;
import com.paypay.dto.Response.Response;
import com.paypay.model.TransactionData;
import com.paypay.service.impl.TransactionImpl;

@RestController
@RequestMapping("/transaction-service")
public class TransactionController {
    Response response;

    @Autowired
    private TransactionImpl transactionImpl;
    
    @PostMapping("/transaction")
    public Response transactionExchange(@Valid @RequestBody TransactionExchangeRequest request) throws Exception {
        response = transactionImpl.TransactionExchange(request);
        return response;
    }
    
    @GetMapping("/inquiry/transaction/{vaNumber}")
    public TransactionData inquiryTransaction(@PathVariable(name = "vaNumber") String vaNumber) throws Exception {
        return transactionImpl.inquiryTransaction(vaNumber);
    }
    
    @PostMapping("/recon/payment-status")
    public Response reconPayment(@RequestBody ReconInquiryPaymentStatusRequest request) throws Exception {
        return transactionImpl.reconPaymentStatus(request);
    }

    @GetMapping("/history-transaction/list/{nic}")
    public Response inquiryHistoryTransaction(@PathVariable(name = "nic") String nic) throws Exception {
        return response = transactionImpl.historyTransactionList(nic);
    }

    @GetMapping("/history-transaction/{vaNumber}")
    public Response inquiryHistoryDetailTransaction(@PathVariable(name = "vaNumber") String vaNumber) throws Exception {
        return response = transactionImpl.historyTransactionSpecific(vaNumber);
    }
    
    
    
}
