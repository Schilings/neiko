package com.schilings.neiko.autoconfigure.shiro.exception;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/11 2:19
 */
public class JWTAuthenticationFilterException extends RuntimeException {
    
    public JWTAuthenticationFilterException() {
        super();
    }

    public JWTAuthenticationFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTAuthenticationFilterException(String message) {
        super(message);
    }

    public JWTAuthenticationFilterException(Throwable cause) {
        super(cause);
    }
}
