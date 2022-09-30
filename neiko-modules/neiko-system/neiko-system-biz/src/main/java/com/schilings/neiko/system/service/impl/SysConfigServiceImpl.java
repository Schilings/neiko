package com.schilings.neiko.system.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.redis.core.annotation.RedisCacheEvict;
import com.schilings.neiko.common.redis.core.annotation.RedisCacheable;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.constant.SystemRedisKeyConstants;
import com.schilings.neiko.system.mapper.SysConfigMapper;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;
import com.schilings.neiko.system.service.SysConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ExtendServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param sysConfigQO 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO) {
		return baseMapper.queryPage(pageParam, sysConfigQO);
	}

	/**
	 * 根据配置key获取对应value
	 * @param confKey 配置key
	 * @return confValue
	 */
	@RedisCacheable(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX + "#confKey")
	@Override
	public String getConfValueByKey(String confKey) {
		SysConfig sysConfig = baseMapper.selectByKey(confKey);
		return sysConfig == null ? null : sysConfig.getConfValue();
	}

	/**
	 * 保存系统配置，由于查询不到时会缓存空值，所以新建时也需要删除对应 key，防止之前误存了空值数据
	 * @param entity 实体对象
	 * @return 保存成功 true
	 */
	@RedisCacheEvict(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX + "#p0.confKey")
	@Override
	public boolean save(SysConfig entity) {
		return SqlHelper.retBool(getBaseMapper().insert(entity));
	}

	/**
	 * 根据 confKey 进行更新
	 * @param sysConfig 系统配置
	 * @return 更新是否成功
	 */
	@RedisCacheEvict(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX + "#sysConfig.confKey")
	@Override
	public boolean updateByKey(SysConfig sysConfig) {
		return baseMapper.updateByKey(sysConfig);
	}

	/**
	 * 根据 confKey 进行删除
	 * @param confKey 配置key
	 * @return 删除是否成功
	 */
	@RedisCacheEvict(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX + "#confKey")
	@Override
	public boolean removeByKey(String confKey) {

		return baseMapper.deleteByKey(confKey);
	}

}
