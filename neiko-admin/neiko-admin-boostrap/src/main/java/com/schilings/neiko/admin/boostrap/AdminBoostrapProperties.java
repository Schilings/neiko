package com.schilings.neiko.admin.boostrap;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("neiko.admin.boostrap")
public class AdminBoostrapProperties {

	private boolean enabled = false;

}
