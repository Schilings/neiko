package com.schilings.neiko.system.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUserRole;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;

import java.util.List;

import static com.schilings.neiko.common.model.constants.GlobalConstants.NOT_DELETED_FLAG;

/**
 * 
 * <p>系统角色表</p>
 * 
 * @author Schilings
*/
public interface SysRoleMapper extends ExtendMapper<SysRole> {


    /**
     * 分页查询
     * @param pageParam 分页参数
     * @param qo 查询对象
     * @return PageResult<SysRoleVO>
     */
    default PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo) {
        IPage<SysRolePageVO> page = this.prodPage(pageParam);
        NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                .selectAll(SysRole.class)
                .likeIfPresent(SysRole::getName, qo.getName())
                .likeIfPresent(SysRole::getCode, qo.getCode())
                .betweenIfPresent(SysRole::getCreateTime, qo.getStartTime(), qo.getEndTime());
        IPage<SysRolePageVO> iPage = this.selectJoinPage(page, SysRolePageVO.class, AUTO_RESULT_MAP, queryWrapper);
        return this.prodPage(iPage);
    }

    /**
     * 通过用户ID，查询角色列表
     * @param userId 用户ID
     * @return List<SysRole>
     */
    default List<SysRole> listByUserId(Long userId){
        return this.selectJoinList(SysRole.class, AUTO_RESULT_MAP,
                WrappersX.lambdaQueryJoin()
                        .selectAll(SysRole.class)
                        .leftJoin(SysUserRole.class, SysUserRole::getRoleCode, SysRole::getCode)
                        .eq(SysRole::getDeleted,NOT_DELETED_FLAG)
                        .eq(SysUserRole::getUserId, userId));
    }
    
    
    
}
