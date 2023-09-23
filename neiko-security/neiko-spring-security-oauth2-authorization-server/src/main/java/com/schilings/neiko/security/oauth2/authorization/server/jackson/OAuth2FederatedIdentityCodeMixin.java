package com.schilings.neiko.security.oauth2.authorization.server.jackson;

import com.fasterxml.jackson.annotation.*;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityCode;
import org.springframework.boot.jackson.JsonMixin;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
// spring
// @JsonMixin(type = OAuth2FederatedIdentityCode.class)
public abstract class OAuth2FederatedIdentityCodeMixin {

	@JsonCreator
	public OAuth2FederatedIdentityCodeMixin(@JsonProperty(value = "tokenValue") String tokenValue,
			@JsonProperty(value = "issuedAt") Instant issuedAt, @JsonProperty(value = "expiresAt") Instant expiresAt) {

	}

}
