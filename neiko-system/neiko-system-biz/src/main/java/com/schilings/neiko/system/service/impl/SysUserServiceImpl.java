package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysUserMapper;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ExtendServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final EventBus eventBus;
    

    /**
     * 根据QueryObject查询系统用户列表
     * @param pageParam 分页参数
     * @param qo 查询参数对象
     * @return PageResult<SysUserVO> 分页数据
     */
    @Override
    public PageResult<SysUserPageVO> queryPage(PageParam pageParam, SysUserQO qo) {
        return baseMapper.queryPage(pageParam, qo);
    }


    /**
     * 获取用户详情信息
     * @param user SysUser
     * @return UserInfoDTO
     */
    @Override
    public UserInfoDTO findUserInfo(SysUser user) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setSysUser(user);
        
        return null;
    }
}
