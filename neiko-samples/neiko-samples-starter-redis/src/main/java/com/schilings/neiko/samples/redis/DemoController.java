package com.schilings.neiko.samples.redis;

import com.schilings.neiko.common.cache.annotation.NeikoCacheEvict;
import com.schilings.neiko.common.cache.annotation.NeikoCachePut;
import com.schilings.neiko.common.cache.annotation.NeikoCacheable;
import com.schilings.neiko.common.redis.core.annotation.RedisCacheEvict;
import com.schilings.neiko.common.redis.core.annotation.RedisCachePut;
import com.schilings.neiko.common.redis.core.annotation.RedisCacheable;
import com.schilings.neiko.common.redis.message.annoation.RedisListener;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/demo")
public class DemoController {

	private static AtomicInteger atomicInteger = new AtomicInteger();

	// Map
	@GetMapping("/test1")
	@NeikoCacheable(key = "#p0")
	public DTO test1(String username) {
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@GetMapping("/test2")
	@NeikoCacheEvict(key = "#p0")
	public DTO test2(String username) {
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@GetMapping("/test3")
	@NeikoCachePut(key = "#p0")
	public DTO test3(String username) {
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	// =================== redis模块的注解 =====================

	@GetMapping("/redis1")
	@RedisCacheable(key = "#p0", condition = "#p0.startsWith('admin')")
	public DTO redis1(String username) {
		System.out.println(username);
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@GetMapping("/redis2")
	@RedisCachePut(key = "#p0")
	public DTO redis2(String username) {
		System.out.println(username);
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@GetMapping("/redis3")
	@RedisCacheEvict(key = "#p0")
	public DTO redis3(String username) {
		System.out.println(username);
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@GetMapping("/redis4")
	@RedisCacheable(key = "#p0", ttl = 10)
	public DTO redis4(String username) {
		System.out.println(username);
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@GetMapping("/redis5")
	@RedisCachePut(key = "#p0", ttl = 10)
	public DTO redis5(String username) {
		System.out.println(username);
		return new DTO(atomicInteger.incrementAndGet(), username, username);
	}

	@RedisListener(value = "test", condition = "#p0.id==100")
	public void testListener1(DTO message, String channel) {
		System.out.println(message);
		System.out.println(channel);
		System.out.println("test1 test1 test1");
	}

	@RedisListener(value = "test")
	public void testListener2(String message, String channel) {
		System.out.println(message);
		System.out.println(channel);
		System.out.println("test2 test2 test2");
	}

	@RedisListener(value = "demo")
	public void testListener3(String message, String channel) {
		System.out.println(message);
		System.out.println(channel);
		System.out.println("demo demo demo");
	}
	

}
