package com.schilings.neiko.autoconfigure.shiro.cons;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 15:34
 */
public class JWTConstants {

    // 默认HMAC签名有效期：1分钟=60000毫秒(ms)
    public static final Integer DEFAULT_HMAC_PERIOD = 60000;
    // 默认HASH加密算法
    public static final String DEFAULT_HASH_ALGORITHM_NAME = "MD5";
    // 默认HASH加密盐
    public static final String DEFAULT_HASH_SALT = "A1B2C3D4efg.5679g8e7d6c5b4a_-=_)(8.";
    // 默认HASH加密迭代次数
    public static final Integer DEFAULT_HASH_ITERATIONS = 2;
    // 默认JWT加密算法
    public static final String DEFAULT_HMAC_ALGORITHM_NAME = "HmacMD5";
    // HASH加密算法
    public static final String HASH_ALGORITHM_NAME_MD5 = "MD5";
    public static final String HASH_ALGORITHM_NAME_SHA1 = "SHA-1";
    public static final String HASH_ALGORITHM_NAME_SHA256 = "SHA-256";
    public static final String HASH_ALGORITHM_NAME_SHA512 = "SHA-512";
    // HMACA签名算法
    public static final String HMAC_ALGORITHM_NAME_MD5 = "HmacMD5";// 128位
    public static final String HMAC_ALGORITHM_NAME_SHA1 = "HmacSHA1";// 126
    public static final String HMAC_ALGORITHM_NAME_SHA256 = "HmacSHA256";// 256
    public static final String HMAC_ALGORITHM_NAME_SHA512 = "HmacSHA512";// 512

}
