package com.paypay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypay.dto.Response.Response;
import com.paypay.dto.Response.ResponseKursBca;
import com.paypay.service.impl.KursImpl;

@RestController
@RequestMapping("/kurs-service")
public class KursController {
    Response response;
    
    @Autowired
    private KursImpl kursImpl;

    @PostMapping("/inquiry-kurs")
    public ResponseKursBca inquiryIndikasi() throws Exception {
        ResponseKursBca response = new ResponseKursBca();
        response = kursImpl.inquiryKursBank();
        return response;
    }

    @GetMapping("/inquiry-kurs-specific/{bankName}")
    public Response inquiryKursBank(@PathVariable(name = "bankName") String bankName) throws Exception {
        response = kursImpl.inquiryKursDetailBank(bankName);
        return response;
    }
}
