package com.schilings.neiko.extend.sa.token.oauth2.event.authentication;


import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import org.springframework.context.ApplicationEvent;

/**
 * 
 * <p>表示应用程序身份验证事件。
 * ApplicationEvent的source将是Authentication对象</p>
 * 
 * @author Schilings
*/
public abstract class AbstractAuthenticationEvent extends ApplicationEvent {
    
    public AbstractAuthenticationEvent(Authentication source) {
        super(source);
    }
    
    /**
     * 导致事件的Authentication请求的获取器。也可从super.getSource()获得
     * @return
     */
    public Authentication getAuthentication() {
        return (Authentication) super.getSource();
    }
    
}
