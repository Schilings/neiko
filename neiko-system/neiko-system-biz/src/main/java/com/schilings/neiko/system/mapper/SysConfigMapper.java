package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.converter.SysConfigConverter;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;

public interface SysConfigMapper extends ExtendMapper<SysConfig> {

    /**
     * 分页查询
     * @param pageParam 分页参数
     * @param sysConfigQO 查询参数
     * @return PageResult<SysRoleVO>
     */
    default PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO) {
        IPage<SysConfigPageVO> page = this.prodPage(pageParam);
        NeikoLambdaQueryWrapper<SysConfig> queryWrapper = WrappersX.<SysConfig>lambdaQueryJoin()
                .selectAll(SysConfig.class)
                .likeIfPresent(SysConfig::getConfKey, sysConfigQO.getConfKey())
                .likeIfPresent(SysConfig::getName, sysConfigQO.getName())
                .likeIfPresent(SysConfig::getCategory, sysConfigQO.getCategory());
        IPage<SysConfigPageVO> voPage = this.selectJoinPage(page, SysConfigPageVO.class, AUTO_RESULT_MAP, queryWrapper);
        return this.prodPage(voPage);
    }


}
