package com.schilings.neiko.samples.system.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

	/**
	 * 自动填充处理类
	 * @return FillMetaObjectHandle
	 */
	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public MetaObjectHandler fillMetaObjectHandle() {
		return new FillMetaObjectHandle();
	}

}
