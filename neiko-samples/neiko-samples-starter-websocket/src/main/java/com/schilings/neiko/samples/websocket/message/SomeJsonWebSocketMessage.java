package com.schilings.neiko.samples.websocket.message;


import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SomeJsonWebSocketMessage extends JsonWebSocketMessage {
    
    protected SomeJsonWebSocketMessage() {
        super("some");
    }


    private String message;
    
    
    
    
}
