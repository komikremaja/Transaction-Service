package com.paypay.Exception;

import lombok.Data;

@Data
public class ErrorMessage {
    private Integer status;
    private String message;
    private Object data;
}
