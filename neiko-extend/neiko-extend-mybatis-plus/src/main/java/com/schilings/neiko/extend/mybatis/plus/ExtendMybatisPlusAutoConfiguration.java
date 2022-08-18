package com.schilings.neiko.extend.mybatis.plus;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.schilings.neiko.extend.mybatis.plus.core.JoinInterceptor;
import com.schilings.neiko.extend.mybatis.plus.injector.ExtendSqlInjector;
import com.schilings.neiko.extend.mybatis.plus.method.join.SelectJoinList;
import com.schilings.neiko.extend.mybatis.plus.method.join.SelectJoinPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@Slf4j
public class ExtendMybatisPlusAutoConfiguration {
    
    /**
     * MybatisPlusInterceptor 插件，默认提供分页插件</br>
     * 如需其他MP内置插件，则需自定义该Bean
     * @return MybatisPlusInterceptor
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 连表拦截器 
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JoinInterceptor joinInterceptor() {
        return new JoinInterceptor();
    }


    /**
     * 拓展方法注入
     * @return
     */
    @Bean
    @ConditionalOnMissingBean({DefaultSqlInjector.class, AbstractSqlInjector.class, ISqlInjector.class})
    public ISqlInjector extendSqlInjector() {
        return new ExtendSqlInjector();
    }
    
}
