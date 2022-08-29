package com.schilings.neiko.samples.websocket.handler;


import com.schilings.neiko.common.websocket.handler.JsonMessageHandler;
import com.schilings.neiko.samples.websocket.message.SomeJsonWebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SomeJsonMessageHandler implements JsonMessageHandler<SomeJsonWebSocketMessage> {
    
    @Override
    public void handle(WebSocketSession session, SomeJsonWebSocketMessage message) {
        System.out.println("[json]:" + message.getMessage());
    }

    @Override
    public String type() {
        return "some";
    }

    @Override
    public Class<SomeJsonWebSocketMessage> getMessageClass() {
        return SomeJsonWebSocketMessage.class;
    }
}
