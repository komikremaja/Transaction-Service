package com.paypay.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paypay.Exception.BadRequestException;
import com.paypay.constant.VariableConstant;
import com.paypay.dto.Request.TransactionExchangeRequest;
import com.paypay.dto.Response.Response;
import com.paypay.dto.Response.ResponseExchange;
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

    @Value("${limit.transaction.month}")
    private BigDecimal limitThreshold;

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
            }else{
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
}
