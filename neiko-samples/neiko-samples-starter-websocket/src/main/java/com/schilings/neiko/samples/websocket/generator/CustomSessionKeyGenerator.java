package com.schilings.neiko.samples.websocket.generator;


import com.schilings.neiko.common.websocket.session.SessionKeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class CustomSessionKeyGenerator implements SessionKeyGenerator {
    
    public static final String ACCESS_TOKEN = "access_token";
    
    /**
     * 获取当前session的唯一标识
     * @param webSocketSession – 当前session
     * @return session唯一标识
     */
    @Override
    public Object sessionKey(WebSocketSession webSocketSession) {
        //我们将添加在websocket session的accessToken作为session的key
        return webSocketSession.getAttributes().get(ACCESS_TOKEN);
    }
}
