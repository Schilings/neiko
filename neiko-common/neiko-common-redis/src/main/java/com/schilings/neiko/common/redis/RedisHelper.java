package com.schilings.neiko.common.redis;

import com.schilings.neiko.common.util.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2020/4/17 11:49
 */
@Slf4j
public class RedisHelper {

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static final String TIME_PATTERN = "HH:mm:ss";

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Getter
	@Setter
	static StringRedisTemplate template;

	@Getter
	@Setter
	static RedisTemplate<String, Object> objectRedisTemplate;

	public static RedisTemplate<String, Object> objectRedisTemplate() {
		if (objectRedisTemplate == null) {
			objectRedisTemplate = SpringUtils.getBean("redisTemplate", RedisTemplate.class);
		}
		return objectRedisTemplate;
	}

	/*
	 * common ----------------------------------------------------------
	 */

	/**
	 * @author lingting 2020-04-29 15:04:39
	 */
	public static HashOperations<String, Object, Object> getHash() {
		return template.opsForHash();
	}

	/**
	 * @author lingting 2020-04-29 15:04:40
	 */
	public static ValueOperations<String, String> getValue() {
		return template.opsForValue();
	}

	public static ListOperations<String, String> getList() {
		return template.opsForList();
	}

	public static SetOperations<String, String> getSet() {
		return template.opsForSet();
	}

	public static ZSetOperations<String, String> getZSet() {
		return template.opsForZSet();
	}

	public static boolean hasKey(String key) {
		Boolean b = template.hasKey(key);
		return b != null && b;
	}

	/**
	 * 设置过期时间
	 * @param time 时间，单位 秒
	 * @author lingting 2020-07-28 13:28:41
	 */
	public static boolean expire(String key, long time) {
		Boolean b = template.expire(key, time, TimeUnit.SECONDS);
		return b != null && b;
	}

	/**
	 * 获取所有符合指定表达式的 key
	 * @param pattern 表达式
	 * @return java.util.Set<java.lang.String>
	 * @author lingting 2020-04-27 15:44:09
	 */
	public static Set<String> keys(String pattern) {
		return template.keys(pattern);
	}

	/*
	 * lua 脚本 ----------------------------------------------------------
	 */

	/**
	 * 执行 lua脚本
	 * @param action
	 * @return T
	 * @author lingting 2021-02-26 15:22
	 */
	public static <T> T execute(RedisCallback<T> action) {
		return template.execute(action);
	}

	@Nullable
	public static <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
		return execute(action, exposeConnection, false);
	}

	@Nullable
	public static <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
		return template.execute(action, exposeConnection, pipeline);
	}

	public static <T> T execute(SessionCallback<T> session) {
		return template.execute(session);
	}

	public static <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
		return template.execute(script, keys, args);
	}

	public static <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer,
			RedisSerializer<T> resultSerializer, List<String> keys, Object... args) {
		return template.execute(script, argsSerializer, resultSerializer, keys, args);
	}

	/*
	 * pipelined 操作 ----------------------------------------------------------
	 */

	/**
	 * 操作 pipelined
	 * @author lingting 2021-02-26 15:23
	 */
	public static List<Object> executePipelined(SessionCallback<?> session) {
		return template.executePipelined(session);
	}

	public static List<Object> executePipelined(SessionCallback<?> session,
			@Nullable RedisSerializer<?> resultSerializer) {
		return template.executePipelined(session, resultSerializer);
	}

	public static List<Object> executePipelined(RedisCallback<?> action) {
		return template.executePipelined(action);
	}

	public static List<Object> executePipelined(RedisCallback<?> action,
			@Nullable RedisSerializer<?> resultSerializer) {
		return template.executePipelined(action, resultSerializer);
	}

	/*
	 * string ----------------------------------------------------------
	 */

	/**
	 * @author lingting 2020-04-29 15:04:48
	 */
	public static String get(String key) {
		return getValue().get(key);
	}

	public static void set(String key, String val) {
		getValue().set(key, val);
	}

	/**
	 * 缓存数据
	 * @param key key
	 * @param val val
	 * @param second 过期时间 单位：秒
	 * @author lingting 2020-04-22 11:38:13
	 */
	public static void set(String key, String val, long second) {
		if (second > 0) {
			getValue().set(key, val, second, TimeUnit.SECONDS);
		}
	}

	/**
	 * 缓存数据
	 * @param key key
	 * @param val val
	 * @param instant 在指定时间过期
	 * @author lingting 2021-02-02 10:31
	 */
	public static void set(String key, String val, Instant instant) {
		getValue().set(key, val);
		getTemplate().expireAt(key, instant);
	}

	public static boolean delete(String key) {
		Boolean b = template.delete(key);
		return b != null && b;
	}

	public static long delete(Collection<String> keys) {
		Long l = template.delete(keys);
		return l == null ? 0 : l;
	}

	/**
	 * 如果 key 不存在，则设置 key为 val 并设置过期时间
	 * @param key key
	 * @param value val
	 * @return boolean
	 * @author lingting 2020-04-29 15:56:51
	 */
	public static boolean setIfAbsent(String key, String value) {
		Boolean b = getValue().setIfAbsent(key, value);
		return b != null && b;
	}

	/**
	 * 如果key存在则设置
	 * @param key key
	 * @param value 值
	 * @param time 过期时间, 单位 秒
	 * @return boolean
	 * @author lingting 2021-01-18 14:59
	 */
	public static boolean setIfAbsent(String key, String value, long time) {
		Boolean b = getValue().setIfAbsent(key, value, Duration.ofSeconds(time));
		return b != null && b;
	}

	public static List<String> multiGet(Collection<String> keys) {
		List<String> list = getValue().multiGet(keys);
		return list == null ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * 给 key +1
	 *
	 * @author lingting 2020-05-13 11:04:17
	 */
	public static Long increment(String key) {
		return getValue().increment(key);
	}

	/**
	 * 给 key 增加 指定数值
	 *
	 * @author lingting 2020-05-13 11:04:17
	 */
	public static Long increment(String key, long delta) {
		return getValue().increment(key, delta);
	}

	public static Long incrementAndExpire(String key, long time) {
		return incrementAndExpire(key, 1, time);
	}

	public static Long incrementAndExpire(String key, long delta, long time) {
		Long increment = getValue().increment(key, delta);
		expire(key, time);
		return increment;
	}

	/*
	 * list ----------------------------------------------------------
	 */

	/**
	 * @author lingting 2020-04-29 15:04:52
	 */
	public static List<String> listGet(String key) {
		return getList().range(key, 0, listSize(key) - 1);
	}

	/**
	 * 获取指定值在指定key中的索引
	 * @author lingting 2020-12-17 11:06
	 */
	public static Long listIndexOf(String key, String val) {
		return getList().indexOf(key, val);
	}

	/**
	 * 获知指定key中指定索引的值
	 * @author lingting 2020-12-17 11:07
	 */
	public static String listIndex(String key, long index) {
		return getList().index(key, index);
	}

	public static Long listRemove(String key, String val) {
		return listRemove(key, 1, val);
	}

	/**
	 * @param count 删除多少个
	 * @author lingting 2020-12-16 19:13
	 */
	public static Long listRemove(String key, long count, String val) {
		return getList().remove(key, count, val);
	}

	public static long listSize(String key) {
		Long size = getList().size(key);
		return size == null ? 0 : size;
	}

	private static long listSet(String key, List<String> list) {
		long l = 0;
		for (String str : list) {
			l += listLeftPush(key, str);
		}
		return l;
	}

	/**
	 * 插入list 并设置过期时间
	 * @param key key
	 * @param list list 值
	 * @param time 过期时间
	 * @return long
	 * @author lingting 2020-04-22 15:22:02
	 */
	public static long listSet(String key, List<String> list, long time) {
		long l = listSet(key, list);
		expire(key, time);
		return l;
	}

	/**
	 * 插入列表
	 * @param key key
	 * @param val val
	 * @author lingting 2020-04-22 15:18:07
	 */
	public static Long listLeftPush(String key, String val) {
		return getList().leftPush(key, val);
	}

	public static Long listRightPush(String key, String val) {
		return getList().rightPush(key, val);
	}

	public static String listLeftPop(String key) {
		return getList().leftPop(key);
	}

	public static String listRightPop(String key) {
		return getList().rightPop(key);
	}

	/*
	 * hash ----------------------------------------------------------
	 */

	/**
	 * @author lingting 2020-04-29 15:05:41
	 */
	public static void hashSet(String key, String field, String value) {
		getHash().put(key, field, value);
	}

	/**
	 * 获取 指定 key 中 指定 field 的值
	 * @param key key
	 * @param field field
	 * @return java.lang.Object
	 * @author lingting 2020-04-22 17:17:33
	 */
	public static String hashGet(String key, String field) {
		Object o = getHash().get(key, field);
		return o == null ? null : o.toString();
	}

	/**
	 * 移除指定 key中的 字段
	 * @param key key
	 * @param fields 字段
	 * @return java.lang.Long
	 * @author lingting 2020-12-21 10:16
	 */
	public static Long hashDelete(String key, String... fields) {
		return getHash().delete(key, (Object[]) fields);
	}

	/*
	 * set -----------------------------------------------------------
	 */

	/**
	 * set中添加数据
	 *
	 * @author lingting 2020-12-17 10:50
	 */
	public static Long setAdd(String key, String... values) {
		return getSet().add(key, values);
	}

	/**
	 * 获取集合中元素数量
	 * @author lingting 2020-12-17 10:51
	 */
	public static Long setSize(String key) {
		return getSet().size(key);
	}

	/**
	 * 随机弹出一个元素
	 * @author lingting 2020-12-17 10:52
	 */
	public static String setPop(String key) {
		return getSet().pop(key);
	}

	/**
	 * 移除集合中的元素
	 * @author lingting 2020-12-17 10:55
	 */
	public static Long setRemove(String key, String... values) {
		return getSet().remove(key, (Object[]) values);
	}

	/*
	 * zset -----------------------------------------------------------
	 */

	/**
	 * zset中添加数据
	 *
	 * @author svn 2022-03-10 18:20
	 */
	public static Boolean zSetAdd(String key, String value, double score) {
		return getZSet().add(key, value, score);
	}

	/**
	 * 获取有序集合中元素数量
	 * @author svn 2022-03-10 18:22
	 */
	public static Long zSetSize(String key) {
		return getZSet().size(key);
	}

	/**
	 * 随机弹出一个元素
	 * @author svn 2022-03-10 18:23
	 */
	public static String zSetRandom(String key) {
		return getZSet().randomMember(key);
	}

	/**
	 * 移除有序集合中的元素
	 * @author svn 2022-03-10 18:24
	 */
	public static Long zSetRemove(String key, String... values) {
		return getZSet().remove(key, (Object[]) values);
	}

	/**
	 * 在有序集合中的排名
	 * @author svn 2022-03-10 18:30
	 */
	public static Long zSetRank(String key, String value) {
		return getZSet().rank(key, value);
	}

	/**
	 * 在有序集合中的排名, 从小到大
	 * @author svn 2022-03-10 18:31
	 */
	public static Set<String> zSetRange(String key, int start, int end) {
		return getZSet().range(key, start, end);
	}

	/**
	 * 在有序集合中的排名, 从大到小
	 * @author svn 2022-03-10 18:33
	 */
	public static Set<String> zSetReverseRange(String key, int start, int end) {
		return getZSet().reverseRange(key, start, end);
	}

	/**
	 * 在有序集合中的排名, 分数区间, 从小到大
	 * @author svn 2022-03-10 18:34
	 */
	public static Set<String> zSetRangeByScore(String key, double min, double max) {
		return getZSet().rangeByScore(key, min, max);
	}

	/**
	 * 在有序集合中的排名, 分数区间, 从大到小
	 * @author svn 2022-03-10 18:35
	 */
	public static Set<String> zSetReverseRangeByScore(String key, double min, double max) {
		return getZSet().reverseRangeByScore(key, min, max);
	}

	public static Object evalLua(String lua, List<String> key, Object... argv) {
		String[] arg = Arrays.stream(argv).map(String::valueOf).toArray(String[]::new);

		try {
			DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(lua, String.class);
			return template.execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), key, arg);
		}
		catch (Exception e) {
			log.error("redis evalLua execute fail:lua[{}]", lua, e);
			return "false";
		}
	}

}
