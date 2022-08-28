package com.schilings.neiko.extend.sa.token.oauth2.event.authentication;


import cn.dev33.satoken.exception.SaTokenException;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import org.springframework.util.Assert;

/**
 * 
 * <p>鉴权不通过事件：由于某种原因身份验证失败的抽象应用程序事件。</p>
 * 
 * @author Schilings
*/
public abstract class AbstractAuthenticationFailureEvent extends AbstractAuthenticationEvent{

    private final SaTokenException exception;
    
    public AbstractAuthenticationFailureEvent(Authentication authentication,SaTokenException exception) {
        super(authentication);
        Assert.notNull(exception, "AuthenticationException is required");
        this.exception = exception;
    }

    public SaTokenException getException() {
        return this.exception;
    }
}
