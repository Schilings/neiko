package com.schilings.neiko.cloud.gateway.predicate;

import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.handler.predicate.*;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import jakarta.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @see AfterRoutePredicateFactory
 * @see BeforeRoutePredicateFactory
 * @see BetweenRoutePredicateFactory
 * @see CookieRoutePredicateFactory
 * @see HeaderRoutePredicateFactory
 * @see HostRoutePredicateFactory
 * @see MethodRoutePredicateFactory
 * @see PathRoutePredicateFactory
 * @see QueryRoutePredicateFactory
 * @see ReadBodyRoutePredicateFactory
 * @see RemoteAddrRoutePredicateFactory
 * @see WeightRoutePredicateFactory
 * @see CloudFoundryRouteServiceRoutePredicateFactory
 * @author Schilings
 */
public class CustomizeRoutePredicateFactory
		extends AbstractRoutePredicateFactory<CustomizeRoutePredicateFactory.Config> {

	/**
	 * Header key.
	 */
	public static final String HEADER_KEY = "header";

	/**
	 * Regexp key.
	 */
	public static final String REGEXP_KEY = "regexp";

	public CustomizeRoutePredicateFactory() {
		super(Config.class);
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(HEADER_KEY, REGEXP_KEY);
	}

	@Override
	public Predicate<ServerWebExchange> apply(Config config) {
		boolean hasRegex = !StringUtils.isEmpty(config.regexp);

		return new GatewayPredicate() {
			@Override
			public boolean test(ServerWebExchange exchange) {
				List<String> values = exchange.getRequest().getHeaders().getOrDefault(config.header,
						Collections.emptyList());
				if (values.isEmpty()) {
					return false;
				}
				// 保证值不为空
				if (hasRegex) {
					// 检查值是否匹配
					return values.stream().anyMatch(value -> value.matches(config.regexp));
				}

				// 有一个值，由于正则表达式为空，我们只检查存在。
				return true;
			}

			@Override
			public String toString() {
				return String.format("Header: %s regexp=%s", config.header, config.regexp);
			}
		};
	}

	@Override
	public Predicate<ServerWebExchange> apply(Consumer<Config> consumer) {
		return super.apply(consumer);
	}

	@Override
	public AsyncPredicate<ServerWebExchange> applyAsync(Consumer<Config> consumer) {
		return super.applyAsync(consumer);
	}

	@Override
	public AsyncPredicate<ServerWebExchange> applyAsync(Config config) {
		return super.applyAsync(config);
	}

	@Override
	public void beforeApply(Config config) {
		super.beforeApply(config);
	}

	@Override
	public String name() {
		return super.name();
	}

	@Validated
	public static class Config {

		@NotEmpty
		private String header;

		private String regexp;

		public String getHeader() {
			return header;
		}

		public Config setHeader(String header) {
			this.header = header;
			return this;
		}

		public String getRegexp() {
			return regexp;
		}

		public Config setRegexp(String regexp) {
			this.regexp = regexp;
			return this;
		}

	}

}
