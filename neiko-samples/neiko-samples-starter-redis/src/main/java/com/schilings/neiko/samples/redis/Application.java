package com.schilings.neiko.samples.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.common.cache.EnableNeikoCaching;
import com.schilings.neiko.common.cache.annotation.NeikoCacheEvict;
import com.schilings.neiko.common.cache.annotation.NeikoCachePut;
import com.schilings.neiko.common.cache.annotation.NeikoCacheable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

@EnableNeikoCaching
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		Application bean = context.getBean(Application.class);
		// System.out.println(bean.test3("11111"));
		// System.out.println(bean.test3(null));
		// System.out.println(bean.test3("123"));
		// System.out.println(bean.test4(null));
		// System.out.println(bean.test4("123"));
		// System.out.println(bean.test5(null));
		// System.out.println(bean.test5("123"));

		RedisTemplate redisTemplate = (RedisTemplate) context.getBean("redisTemplate");
		// 如果用的是stringRedistemplate,那只能序列化String
		redisTemplate.convertAndSend("test", new DTO(100, "123", "456"));
		redisTemplate.convertAndSend("demo", "1232321321");

	}

	// @NeikoCacheable(key = "'prefix:'+#p0",condition = "1+1 == 2",unless =
	// "#p0.equals('11111')")
	// public String test(String msg) {
	// return msg;
	// }
	//
	// @NeikoCacheable(key = "'prefix:'+#p0",condition = "#result.equals('11111')")
	// public String test2(String msg) {
	// return msg;
	// }
	//
	//
	//
	// @NeikoCacheable(cacheRepository = "redis",key = "'key1'")
	// public String test3(String msg) {
	// return msg;
	// }
	//
	// @NeikoCachePut(cacheRepository = "redis",key = "'key2'")
	// public String test4(String msg) {
	// return msg;
	// }
	//
	// @NeikoCacheEvict(cacheRepository = "redis",key = "'key3'")
	// public String test5(String msg) {
	// return msg;
	// }

}
