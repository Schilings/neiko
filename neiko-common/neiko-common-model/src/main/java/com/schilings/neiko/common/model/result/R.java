package com.schilings.neiko.common.model.result;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <pre>{@code
 *      
 * }
 * <p>一般接口通用返回封装类</p>
 * </pre>
 * @author Schilings
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(title = "通用的返回体结构")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "返回状态码", defaultValue = "0")
    private int code;
    
    @Schema(title = "返回信息", defaultValue = "Success")
    private String message;
    
    @Schema(title = "数据", nullable = true, defaultValue = "null")
    private T data;

    public static <T> R<T> ok() {
        return result(null, BaseResultCode.SUCCESS);
    }

    public static <T> R<T> ok(String message) {
        return result(null, BaseResultCode.SUCCESS.getCode(), message);
    }

    public static <T> R<T> ok(T data) {
        return result(data, BaseResultCode.SUCCESS);
    }

    public static <T> R<T> ok(T data,String message) {
        return result(data, BaseResultCode.SUCCESS.getCode(), message);
    }

    public static <T> R<T> fail() {
        return result(null, BaseResultCode.FAIL);
    }

    public static <T> R<T> fail(String message) {
        return result(null, BaseResultCode.FAIL.getCode(), message);
    }

    public static <T> R<T> fail(T data) {
        return result(data, BaseResultCode.FAIL);
    }

    public static <T> R<T> fail(T data,String message) {
        return result(data, BaseResultCode.FAIL.getCode(), message);
    }

    private static <T> R<T> result(T data, int code,String message) {
        return new R<T>().setData(data).setCode(code).setMessage(message);
    }
    
    private static <T> R<T> result(T data, BaseResultCode resultCode) {
        return new R<T>().setData(data).setCode(resultCode.getCode()).setMessage(resultCode.getMessage());
    }

    @JsonIgnore
    public boolean isOK() {
        return BaseResultCode.SUCCESS.equals(code);
    }

    @JsonIgnore
    public boolean isFailed() {
        return !isOK();
    }
    
    
}
