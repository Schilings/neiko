package com.schilings.neiko.autoconfigure.shiro.exception;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/11 2:19
 */
public class JWTAuthenticationException extends RuntimeException {
    
    public JWTAuthenticationException() {
        super();
    }

    public JWTAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTAuthenticationException(String message) {
        super(message);
    }

    public JWTAuthenticationException(Throwable cause) {
        super(cause);
    }
}
