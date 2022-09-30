package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;

public interface SysConfigService extends ExtendService<SysConfig> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param sysConfigQO 查询参数对象
	 * @return 分页数据
	 */
	PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO);

	/**
	 * 根据配置key获取对应value
	 * @param confKey 配置key
	 * @return confValue
	 */
	String getConfValueByKey(String confKey);

	/**
	 * 根据 confKey 进行更新
	 * @param sysConfig 系统配置
	 * @return 更新是否成功
	 */
	boolean updateByKey(SysConfig sysConfig);

	/**
	 * 根据 confKey 进行删除
	 * @param confKey 配置key
	 * @return 删除是否成功
	 */
	boolean removeByKey(String confKey);

}
