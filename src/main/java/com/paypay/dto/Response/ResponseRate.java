package com.paypay.dto.Response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseRate {
    
    @JsonProperty("RateType")
    private Integer rateTypes;
    @JsonProperty("SellRate")
    private BigDecimal sellRate;
    @JsonProperty("BuyRate")
    private BigDecimal buyRate;
}
