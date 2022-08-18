package com.schilings.neiko.autoconfigure.datascope;


import com.schilings.neiko.autoconfigure.datascope.advisor.DataPermissionAnnotationAdvisor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@RequiredArgsConstructor
public class DataScopeAutoConfiguration {

    /**
     * 数据权限注解 Advisor，用于处理数据权限的链式调用关系
     * @return DataPermissionAnnotationAdvisor
     */
    @Bean
    @ConditionalOnMissingBean(DataPermissionAnnotationAdvisor.class)
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }


}
