package com.paypay.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypay.Exception.BadRequestException;
import com.paypay.constant.VariableConstant;
import com.paypay.dto.Request.InquiryAccountBankRequest;
import com.paypay.dto.Request.ReconInquiryPaymentStatusRequest;
import com.paypay.dto.Request.TransactionExchangeRequest;
import com.paypay.dto.Response.Response;
import com.paypay.dto.Response.ResponseExchange;
import com.paypay.dto.Response.ResponseHistoryTransactionDetail;
import com.paypay.dto.Response.ResponsePaymentStatusRecon;
import com.paypay.model.TransactionData;
import com.paypay.model.TransactionThreshold;
import com.paypay.repository.TransactionRepository;
import com.paypay.repository.TransactionThresholdRepo;

@Service
public class TransactionImpl {
    Response response;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionThresholdRepo transactionThresholdRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private VariableConstant variableConstant;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${limit.transaction.month}")
    private BigDecimal limitThreshold;

    @Autowired
    private ObjectMapper objectMapper;

    public TransactionData inquiryTransaction(String vaNumber) throws Exception {
        TransactionData transactionDb = transactionRepository.findByVaNumber(vaNumber);
        if (transactionDb == null) {
            throw new BadRequestException("Data Transaksi tidak tersedia");
        }
        return transactionDb;
    }

    @Transactional(rollbackOn = Exception.class)
    public Response TransactionExchange(TransactionExchangeRequest transactionExchangeRequest) throws Exception {
        // Checking currency
        LocalDateTime now = LocalDateTime.now();
        String[] currencys = transactionExchangeRequest.getCurrencyPair().split("\\/");
        if (currencys[0].equalsIgnoreCase(currencys[1])) {
            throw new BadRequestException("Currency tidak boleh sama");
        }
        if (currencys[0].equalsIgnoreCase("IDR")) {
            throw new BadRequestException("Currency tidak bisa IDR");
        }
        if (!currencys[1].equalsIgnoreCase("IDR")) {
            throw new BadRequestException("Currency hanya bisa IDR");
        }
        if (transactionExchangeRequest.getTypeTransaction().equalsIgnoreCase("B")) {
            TransactionThreshold thresholdDb = transactionThresholdRepo.findByNic(transactionExchangeRequest.getNic());
            if (thresholdDb == null) {
                thresholdDb = new TransactionThreshold();
                thresholdDb.setNic(transactionExchangeRequest.getNic());
                thresholdDb.setUsedAmount(transactionExchangeRequest.getAmount2());
            } else {
                thresholdDb.setUsedAmount(thresholdDb.getUsedAmount().add(transactionExchangeRequest.getAmount2()));
            }
            thresholdDb.setCreatedDate(now);
            thresholdDb.setLastUpdate(now);
            if (thresholdDb.getUsedAmount().compareTo(limitThreshold) > 0) {
                throw new BadRequestException("Limit Transaksi perbulan sudah melebih batas");
            }
            transactionThresholdRepo.save(thresholdDb);
        }
        String vaNumber = generateVaNumber(transactionExchangeRequest.getBankName());
        TransactionData transactionData = mapper.map(transactionExchangeRequest, TransactionData.class);
        transactionData.setTransactionStatus("1");
        transactionData.setVaNumber(vaNumber);
        transactionData.setCreatedDate(now);
        transactionData.setLastUpdate(now);
        InquiryAccountBankRequest inquiryAccountBankRequest = new InquiryAccountBankRequest();
        inquiryAccountBankRequest.setAccountNumber(transactionData.getDestinationAccount());

        Response inquiryAccount = inquiryAccount(inquiryAccountBankRequest);
        if (inquiryAccount == null) {

        }
        transactionRepository.save(transactionData);
        ResponseExchange resExchange = new ResponseExchange();
        resExchange.setMessage("Success");
        resExchange.setVaNumber(vaNumber);
        return response = new Response(variableConstant.getSTATUS_OK(), "Success", resExchange);
    }

    public String generateVaNumber(String bankName) {
        // Tentukan bank code berdasarkan bank name
        String bankCode = "";
        switch (bankName) {
            case "BCA":
                bankCode = "001";
                break;
            case "BRI":
                bankCode = "002";
                break;
            case "Mandiri":
                bankCode = "003";
                break;
            default:
                throw new IllegalArgumentException("Bank name tidak valid");
        }

        // Tentukan tanggal sekarang
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String dateStr = now.format(dateFormatter);

        // Tentukan sequence number
        int sequenceNumber = getSequenceNumber(bankName, now);

        // Format VA number
        String vaNumber = String.format("7707%s%s%04d", bankCode, dateStr, sequenceNumber);

        return vaNumber;
    }

    public int getSequenceNumber(String bankName, LocalDateTime now) {
        // Ambil sequence number dari database berdasarkan bank name dan tanggal
        // sekarang
        List<TransactionData> vaNumberDb = transactionRepository.findVaNumberSequence(bankName);
        if (vaNumberDb.size() == 0) {
            return 1;
        }
        int sequenceNumber = Integer
                .parseInt(vaNumberDb.get(0).getVaNumber().substring(vaNumberDb.get(0).getVaNumber().length() - 4));

        // Jika tanggal berbeda, sequence number dimulai dari 1
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime lastDate = vaNumberDb.get(0).getCreatedDate(); // Ganti dengan tanggal terakhir VA number
                                                                     // ter-generate di database
        String lastDateFormat = lastDate.format(dateFormatter);
        String nowFormat = now.format(dateFormatter);
        if (!nowFormat.equalsIgnoreCase(lastDateFormat)) {
            return 1;
        }

        // Jika tanggal sama, increment sequence number
        return sequenceNumber + 1;
    }

    public Response inquiryAccount(InquiryAccountBankRequest request) throws Exception {
        String url = "http://localhost:8080/utilities-service/bank/inquiry-rekening";

        HttpEntity<Object> entity = new HttpEntity<Object>(request);
        Response response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class, 1).getBody();
            System.out.println("Response Inquiry Account:" + response);
            return response;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error Inquiry Rekening: " + e.getMessage());
            throw new BadRequestException("Inquiry Rekening gagal");
        }

    }

    public Response reconPaymentStatus(ReconInquiryPaymentStatusRequest request) throws Exception {
        String url = "http://localhost:8484/payment/inquiry/payment-status";

        HttpEntity<Object> entity = new HttpEntity<Object>(request);
        LocalDateTime now = LocalDateTime.now();
        ResponsePaymentStatusRecon responsePaymentStatus = null;
        TransactionData transactionEntity = transactionRepository.findByVaNumber(request.getVaNumber());
        if (request.getVaNumber() == null) {
            throw new BadRequestException("Data Transaksi tidak tersedia");
        }
        try {
            response = restTemplate
                    .exchange(url, HttpMethod.POST, entity, Response.class, 1).getBody();
            String data = objectMapper.writeValueAsString(response.getData());
            responsePaymentStatus = objectMapper.readValue(data, ResponsePaymentStatusRecon.class);
            LocalDateTime dateTransaction = transactionEntity.getCreatedDate();
            Duration duration = Duration.between(dateTransaction, now);
            if (responsePaymentStatus.getPaymentStatus().equalsIgnoreCase("1")) {
                if (duration.toMinutes() > 10) {
                    transactionEntity.setTransactionStatus("2");
                    transactionEntity.setLastUpdate(now);
                }
            } else {
                transactionEntity.setTransactionStatus(responsePaymentStatus.getPaymentStatus());
                transactionEntity.setLastUpdate(now);
            }
            transactionRepository.save(transactionEntity);
            System.out.println("Response Reconpayment Status" + response);
            return response = new Response(variableConstant.getSTATUS_OK(), "Recon berhasil", responsePaymentStatus);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error recon payment status " + e.getMessage());
            throw new BadRequestException("recon gagal");
        }

    }

    public Response historyTransactionList(String nic) throws Exception {
        List<TransactionData> listTransactionDataDb = transactionRepository.findHistoryTransactionlist(nic);
        String responseHistoryTransaction = "";
        if(listTransactionDataDb.size() == 0){
            responseHistoryTransaction = "History transaction kosong";
            response = new Response(variableConstant.getSTATUS_OK(), "Transaction History Kosong", responseHistoryTransaction);
        }else{
            response = new Response(variableConstant.getSTATUS_OK(), "success", listTransactionDataDb);
        }
        return response;
    }

    public Response historyTransactionSpecific(String vaNumber) throws Exception {
        TransactionData transactionDataDb = transactionRepository.findByVaNumber(vaNumber);
        String responseHistoryTransaction = "";
        ResponseHistoryTransactionDetail responseHistoryTransactionDetail = new ResponseHistoryTransactionDetail();
        if(transactionDataDb == null){
            responseHistoryTransaction = "History transaction kosong";
            response = new Response(variableConstant.getSTATUS_OK(), "Transaction History Kosong", responseHistoryTransaction);
        }else{
            responseHistoryTransactionDetail = mapper.map(transactionDataDb, ResponseHistoryTransactionDetail.class);
            responseHistoryTransactionDetail.setExpiredDate(transactionDataDb.getCreatedDate().plusMinutes(10));
            response = new Response(variableConstant.getSTATUS_OK(), "success", responseHistoryTransactionDetail);
        }
        return response;
    }

}
