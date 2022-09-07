package com.schilings.neiko.samples.starter.datascope.datarule.config;

import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.samples.starter.datascope.datarule.datascope.ClassDataScope;
import com.schilings.neiko.samples.starter.datascope.datarule.datascope.SchoolDataScope;
import com.schilings.neiko.samples.starter.datascope.datarule.datascope.StudentDataScope;
import com.schilings.neiko.samples.starter.datascope.datarule.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
public class DataPermissionRuleTestConfiguration {

	@Bean
	public DataScope classDataScope() {
		return new ClassDataScope();
	}

	@Bean
	public DataScope schoolDataScope() {
		return new SchoolDataScope();
	}

	@Bean
	public DataScope studentDataScope() {
		return new StudentDataScope();
	}

	@Bean
	public StudentService studentService() {
		return new StudentService();
	}

}
