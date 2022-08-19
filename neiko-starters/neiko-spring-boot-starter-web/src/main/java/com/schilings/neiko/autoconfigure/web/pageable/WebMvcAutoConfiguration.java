package com.schilings.neiko.autoconfigure.web.pageable;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(PageableProperties.class)
public class WebMvcAutoConfiguration {

    private final PageableProperties pageableProperties;
    
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    

    /**
     * 追加 PageParam 请求处理器 到 springmvc 中
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> oldResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        //放在前面，否则会被ServletModelAttributeMethodProcessor拦截
        ArrayList<HandlerMethodArgumentResolver> newReolvers = new ArrayList<>();
        newReolvers.add(new DefaultPageParamArgumentResolver(pageableProperties));
        newReolvers.addAll(oldResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(newReolvers);
    }

}
