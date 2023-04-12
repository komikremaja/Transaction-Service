package com.paypay.dto.Response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseKursRate {
    @JsonProperty("OrderNo")
    private BigDecimal orderNo;
    @JsonProperty("CurrencyCode")
    private String currencyCode;
    @JsonProperty("CurrencyFontImage")
    private String currencyFontImage;
    @JsonProperty("CurrencyImageUrl")
    private String currencyImageUrl;
    @JsonProperty("CurrencyName")
    private String currencyName;
    @JsonProperty("Rates")
    private List<ResponseRate> rates;
}
