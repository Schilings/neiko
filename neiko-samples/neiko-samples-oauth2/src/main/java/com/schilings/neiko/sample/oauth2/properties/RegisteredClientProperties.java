package com.schilings.neiko.sample.oauth2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "neiko.security.oauth2.registered")
public class RegisteredClientProperties {

	private boolean enabled = true;

	private List<Map<String, String>> client = new ArrayList<>();

}
