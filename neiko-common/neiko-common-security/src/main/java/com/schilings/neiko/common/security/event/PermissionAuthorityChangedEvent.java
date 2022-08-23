package com.schilings.neiko.common.security.event;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>角色权限更新事件</p>
 * 
 * @author Schilings
*/
@Getter
@Setter
public class PermissionAuthorityChangedEvent extends AuthorityChangedEvent{


    private final String roleCode;
    
    public PermissionAuthorityChangedEvent(String roleCode) {
        super(roleCode);
        this.roleCode = roleCode;
    }
}
