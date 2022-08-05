package com.schilings.neiko.samples.extend.mybatis.plus.config;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.schilings.neiko.extend.mabatis.plus.injector.ExtendSqlInjector;
import com.schilings.neiko.extend.mabatis.plus.method.join.SelectJoinList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public ISqlInjector customSqlInjector() {
        return new ExtendSqlInjector();
    }
}
