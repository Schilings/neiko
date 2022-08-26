package com.schilings.neiko.extend.sa.token.core;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import com.schilings.neiko.common.redis.RedisHelper;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * <p>
 * Sa-Token持久层接口(使用框架自带RedisHelper实现 协议统一)
 * </p>
 *
 * @author Schilings
 */
public class RedisSaTokenDao implements SaTokenDao {

	/**
	 * 获取Value，如无返空
	 */
	@Override
	public String get(String key) {
		return RedisHelper.get(key);
	}

	/**
	 * 写入Value，并设定存活时间 (单位: 秒)
	 */
	@Override
	public void set(String key, String value, long timeout) {
		if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		// 判断是否为永不过期
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			RedisHelper.set(key, value);
		}
		else {
			RedisHelper.set(key, value, timeout);
		}
	}

	/**
	 * 修修改指定key-value键值对 (过期时间不变)
	 */
	@Override
	public void update(String key, String value) {
		long expire = getTimeout(key);
		// -2 = 无此键
		if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		this.set(key, value, expire);
	}

	/**
	 * 删除Value
	 */
	@Override
	public void delete(String key) {
		RedisHelper.delete(key);
	}

	/**
	 * 获取Value的剩余存活时间 (单位: 秒)
	 */
	@Override
	public long getTimeout(String key) {
		return RedisHelper.getTemplate().getExpire(key, TimeUnit.SECONDS).longValue();
	}

	/**
	 * 修改Value的剩余存活时间 (单位: 秒)
	 */
	@Override
	public void updateTimeout(String key, long timeout) {
		// 判断是否想要设置为永久
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			long expire = getTimeout(key);
			if (expire == SaTokenDao.NEVER_EXPIRE) {
				// 如果其已经被设置为永久，则不作任何处理
			}
			else {
				// 如果尚未被设置为永久，那么再次set一次
				this.set(key, this.get(key), timeout);
			}
			return;
		}
		RedisHelper.expire(key, timeout);
	}

	/**
	 * 获取Object，如无返空
	 */
	@Override
	public Object getObject(String key) {
		return RedisHelper.objectRedisTemplate().opsForValue().get(key);
	}

	/**
	 * 写入Object，并设定存活时间 (单位: 秒)
	 */
	@Override
	public void setObject(String key, Object object, long timeout) {
		if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		// 判断是否为永不过期
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			RedisHelper.objectRedisTemplate().opsForValue().set(key, object);
		}
		else {
			RedisHelper.objectRedisTemplate().opsForValue().set(key, object, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 更新Object (过期时间不变)
	 */
	@Override
	public void updateObject(String key, Object object) {
		long expire = getObjectTimeout(key);
		// -2 = 无此键
		if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		this.setObject(key, object, expire);
	}

	/**
	 * 删除Object
	 */
	@Override
	public void deleteObject(String key) {
		RedisHelper.delete(key);
	}

	/**
	 * 获取Object的剩余存活时间 (单位: 秒)
	 */
	@Override
	public long getObjectTimeout(String key) {
		return RedisHelper.getTemplate().getExpire(key, TimeUnit.SECONDS).longValue();
	}

	/**
	 * 修改Object的剩余存活时间 (单位: 秒)
	 */
	@Override
	public void updateObjectTimeout(String key, long timeout) {
		// 判断是否想要设置为永久
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			long expire = getObjectTimeout(key);
			if (expire == SaTokenDao.NEVER_EXPIRE) {
				// 如果其已经被设置为永久，则不作任何处理
			}
			else {
				// 如果尚未被设置为永久，那么再次set一次
				this.setObject(key, this.getObject(key), timeout);
			}
			return;
		}
		RedisHelper.expire(key, timeout);
	}

	/**
	 * 搜索数据
	 */
	@Override
	public List<String> searchData(String prefix, String keyword, int start, int size) {
		Set<String> keys = RedisHelper.keys(prefix + "*" + keyword + "*");
		List<String> list = new ArrayList<>(keys);
		return SaFoxUtil.searchList(list, start, size);
	}

}
