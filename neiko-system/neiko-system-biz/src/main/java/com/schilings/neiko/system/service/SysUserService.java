package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;

public interface SysUserService extends ExtendService<SysUser> {


    /**
     * 根据QueryObject查询系统用户列表
     * @param pageParam 分页参数
     * @param qo 查询参数对象
     * @return PageResult<SysUserVO> 分页数据
     */
    PageResult<SysUserPageVO> queryPage(PageParam pageParam, SysUserQO qo);


    /**
     * 获取用户详情信息
     * @param user SysUser
     * @return UserInfoDTO
     */
    UserInfoDTO findUserInfo(SysUser user);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 系统用户
     */
    public SysUser getByUsername(String username);
}
