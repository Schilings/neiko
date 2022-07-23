package com.schilings.neiko.samples.common.core;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DTO implements Event{
    private String message;

    @Override
    public String getTarget() {
        return message;
    }
}
