package com.schilings.neiko.common.security.exception;


import cn.hutool.core.util.ObjectUtil;

public class SecurityException extends RuntimeException{
    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 6806129545290130132L;

    /**
     * 异常细分状态码
     */
    private int code;

    /**
     * 构建一个异常
     *
     * @param code 异常细分状态码 
     */
    public SecurityException(int code) {
        super();
        this.code = code;
    }


    /**
     * 构建一个异常
     *
     * @param message 异常描述信息
     */
    public SecurityException(String message) {
        super(message);
    }

    /**
     * 构建一个异常
     *
     * @param code 异常细分状态码 
     * @param message 异常信息
     */
    public SecurityException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构建一个异常
     *
     * @param cause 异常对象
     */
    public SecurityException(Throwable cause) {
        super(cause);
    }

    /**
     * 构建一个异常
     *
     * @param message 异常信息
     * @param cause 异常对象
     */
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 获取异常细分状态码
     * @return 异常细分状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 写入异常细分状态码 
     * @param code 异常细分状态码
     * @return 对象自身
     */
    public SecurityException setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 如果flag==true，则抛出message异常 
     * @param flag 标记
     * @param message 异常信息 
     */
    public static void throwBy(boolean flag, String message) {
        if(flag) {
            throw new SecurityException(message);
        }
    }

    /**
     * 如果value==null或者isEmpty，则抛出message异常 
     * @param value 值 
     * @param message 异常信息 
     */
    public static void throwByNull(Object value, String message) {
        if(ObjectUtil.isEmpty(value)) {
            throw new SecurityException(message);
        }
    }
}
