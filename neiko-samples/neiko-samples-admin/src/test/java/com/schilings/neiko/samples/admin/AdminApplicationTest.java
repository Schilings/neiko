package com.schilings.neiko.samples.admin;


import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.system.mapper.SysRoleMapper;
import com.schilings.neiko.system.mapper.SysUserMapper;
import com.schilings.neiko.system.mapper.SysUserRoleMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {AdminApplication.class})
public class AdminApplicationTest {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    

    //SysUser

    @Test
    public void SysUser() {
        SysUser_updatePassword();
        SysUser_listByOrganizationIds();
        SysUser_listByUserTypes();
        SysUser_listByUserIds();
        SysUser_listByRoleCodes();
        
    }
    
    public void SysUser_queryPage() {
        PageResult<SysUserPageVO> pageResult = sysUserMapper.queryPage(new PageParam(), new SysUserQO());
        pageResult.getData().forEach(System.out::println);
    }


    public void SysUser_selectByUsername() {
        SysUser sysUser = sysUserMapper.selectByUsername("username1");
        System.out.println(sysUser);
    }
    

    public void SysUser_updateStatusBatch() {
        boolean update = sysUserMapper.updateStatusBatch(Arrays.asList(1L, 2L), 0);
        System.out.println(update);
    }
        
    

    public void SysUser_updatePassword() {
        boolean update = sysUserMapper.updatePassword(1L, "updatePass1");
        System.out.println(update);
    }
    
    

    public void SysUser_listByOrganizationIds() {
        List<SysUser> sysUsers = sysUserMapper.listByOrganizationIds(Arrays.asList(1L,2L));
        sysUsers.forEach(System.out::println);
    }

   
    public void SysUser_listByUserTypes() {
        List<SysUser> sysUsers = sysUserMapper.listByUserTypes(Arrays.asList(1, 2));
        sysUsers.forEach(System.out::println);
    }


    public void SysUser_listByUserIds() {
        List<SysUser> sysUsers = sysUserMapper.listByUserIds(Arrays.asList(1L, 2L));
        sysUsers.forEach(System.out::println);
    }
    
    
  
    public void SysUser_listByRoleCodes() {
        List<SysUser> sysUsers = sysUserMapper.listByRoleCodes(Arrays.asList("ADMIN", "NORMAL"));
        sysUsers.forEach(System.out::println);
    }
    
    


    //SysRole
    
    @Test
    public void SysRole_queryPage() {
        PageResult<SysRolePageVO> pageResult = sysRoleMapper.queryPage(new PageParam(), new SysRoleQO());
        pageResult.getData().forEach(System.out::println);
    }
    
    @Test
    public void SysUserRole_listRolesByUserId() {
        List<SysRole> sysRoles = sysRoleMapper.listByUserId(1L);
        sysRoles.forEach(System.out::println);
    }
}
