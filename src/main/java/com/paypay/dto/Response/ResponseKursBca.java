package com.paypay.dto.Response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseKursBca {
    @JsonProperty("Heading")
    private String heading;
    @JsonProperty("TabKurs")
    private String tabKurs;
    @JsonProperty("TabKursForward")
    private String tabKursForward;
    @JsonProperty("Summary")
    private String summary;
    @JsonProperty("TableTitle")
    private String tableTitle;
    @JsonProperty("ColumnCurrency")
    private String columnCurrency;
    @JsonProperty("ColumnSell")
    private String columnSell;
    @JsonProperty("ColumnBuy")
    private String columnBuy;
    @JsonProperty("KursRates")
    private List<ResponseKursRate> kursRates;
}
