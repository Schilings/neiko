package com.schilings.neiko.samples.redis;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTO {

    private int id;
    private String username;
    private String password;
    
}
