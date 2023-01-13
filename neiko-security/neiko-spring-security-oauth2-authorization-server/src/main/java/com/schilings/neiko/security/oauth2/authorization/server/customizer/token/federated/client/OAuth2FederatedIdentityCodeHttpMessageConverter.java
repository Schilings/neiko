package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client;

import com.schilings.neiko.security.oauth2.authorization.server.HttpMessageConverters;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * </p>
 *
 * @see OAuth2AccessTokenResponseHttpMessageConverter
 * @author Schilings
 */
public class OAuth2FederatedIdentityCodeHttpMessageConverter
		extends AbstractHttpMessageConverter<OAuth2FederatedIdentityCode> {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
	};

	private GenericHttpMessageConverter<Object> jsonMessageConverter = HttpMessageConverters.getJsonMessageConverter();

	protected Converter<Map<String, Object>, OAuth2FederatedIdentityCode> codeConverter = new OAuth2FederatedIdentityCodeHttpMessageConverter.OAuth2FederatedIdentityCodeConverter();

	protected Converter<OAuth2FederatedIdentityCode, Map<String, Object>> parametersConverter = new OAuth2FederatedIdentityCodeHttpMessageConverter.OAuth2FederatedIdentityParametersConverter();

	public OAuth2FederatedIdentityCodeHttpMessageConverter() {
		super(DEFAULT_CHARSET, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));

	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return OAuth2FederatedIdentityCode.class.isAssignableFrom(clazz);
	}

	@Override
	protected OAuth2FederatedIdentityCode readInternal(Class<? extends OAuth2FederatedIdentityCode> clazz,
			HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		try {
			// gh-8157: Parse parameter values as Object in order to handle potential JSON
			// Object and then convert values to String
			Map<String, Object> errorParameters = (Map<String, Object>) this.jsonMessageConverter
					.read(STRING_OBJECT_MAP.getType(), null, inputMessage);
			return this.codeConverter.convert(errorParameters.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, (entry) -> String.valueOf(entry.getValue()))));
		}
		catch (Exception ex) {
			throw new HttpMessageNotReadableException(
					"An error occurred reading the OAuth 2.0 Error: " + ex.getMessage(), ex, inputMessage);
		}
	}

	@Override
	protected void writeInternal(OAuth2FederatedIdentityCode code, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			Map<String, Object> errorParameters = this.parametersConverter.convert(code);
			this.jsonMessageConverter.write(errorParameters, STRING_OBJECT_MAP.getType(), MediaType.APPLICATION_JSON,
					outputMessage);
		}
		catch (Exception ex) {
			throw new HttpMessageNotWritableException(
					"An error occurred writing the OAuth 2.0 Error: " + ex.getMessage(), ex);
		}
	}

	private static class OAuth2FederatedIdentityCodeConverter
			implements Converter<Map<String, Object>, OAuth2FederatedIdentityCode> {

		@Override
		public OAuth2FederatedIdentityCode convert(Map<String, Object> source) {
			String code = getParameterValue(source, OAuth2ParameterNames.CODE);
			long expiresIn = getExpiresIn(source);
			return new OAuth2FederatedIdentityCode(code, null, Instant.ofEpochMilli(expiresIn));
		}

	}

	private static class OAuth2FederatedIdentityParametersConverter
			implements Converter<OAuth2FederatedIdentityCode, Map<String, Object>> {

		@Override
		public Map<String, Object> convert(OAuth2FederatedIdentityCode code) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(OAuth2ParameterNames.CODE, code.getTokenValue());
			parameters.put(OAuth2ParameterNames.EXPIRES_IN, code.getExpiresAt());
			return parameters;
		}

	}

	private static String getParameterValue(Map<String, Object> parameters, String parameterName) {
		Object obj = parameters.get(parameterName);
		return (obj != null) ? obj.toString() : null;
	}

	private static long getExpiresIn(Map<String, Object> parameters) {
		return getParameterValue(parameters, OAuth2ParameterNames.EXPIRES_IN, 0L);
	}

	private static long getParameterValue(Map<String, Object> parameters, String parameterName, long defaultValue) {
		long parameterValue = defaultValue;

		Object obj = parameters.get(parameterName);
		if (obj != null) {
			// Final classes Long and Integer do not need to be coerced
			if (obj.getClass() == Long.class) {
				parameterValue = (Long) obj;
			}
			else if (obj.getClass() == Integer.class) {
				parameterValue = (Integer) obj;
			}
			else {
				// Attempt to coerce to a long (typically from a String)
				try {
					parameterValue = Long.parseLong(obj.toString());
				}
				catch (NumberFormatException ignored) {
				}
			}
		}
		return parameterValue;
	}

}
