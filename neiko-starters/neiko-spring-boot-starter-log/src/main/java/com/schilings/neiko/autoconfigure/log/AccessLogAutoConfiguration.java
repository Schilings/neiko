package com.schilings.neiko.autoconfigure.log;


import com.schilings.neiko.autoconfigure.log.properties.AccessLogProperties;
import com.schilings.neiko.common.log.access.filter.AccessLogFilter;
import com.schilings.neiko.common.log.access.handler.AccessLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Slf4j
@ConditionalOnWebApplication
@RequiredArgsConstructor
@EnableConfigurationProperties(AccessLogProperties.class)
public class AccessLogAutoConfiguration {

    private final AccessLogHandler<?> accessLogService;

    private final AccessLogProperties accessLogProperties;

    @Bean
    @ConditionalOnBean(AccessLogHandler.class)
    public FilterRegistrationBean<AccessLogFilter> accessLogFilterRegistrationBean() {
        log.debug("access log 记录拦截器已开启====");
        FilterRegistrationBean<AccessLogFilter> registrationBean = new FilterRegistrationBean<>(
                new AccessLogFilter(accessLogService, accessLogProperties.getIgnoreUrlPatterns()));
        registrationBean.setOrder(-10);
        return registrationBean;
    }
}
