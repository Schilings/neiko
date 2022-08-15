package com.schilings.neiko.cloud.gateway.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomizeInfoException extends Exception {
    /**
     * http返回码
     */
    private HttpStatus httpStatus;

    /**
     * body中的code字段(业务返回码)
     */
    private String code;

    /**
     * body中的message字段(业务返回信息)
     */
    private String message;
}
