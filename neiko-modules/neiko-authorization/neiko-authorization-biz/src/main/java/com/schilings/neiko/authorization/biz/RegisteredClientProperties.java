package com.schilings.neiko.authorization.biz;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "oauth2.registered")
public class RegisteredClientProperties {

	private List<Map<String, String>> client = new ArrayList<>();

}
