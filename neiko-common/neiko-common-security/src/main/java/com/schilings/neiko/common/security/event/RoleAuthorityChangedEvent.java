package com.schilings.neiko.common.security.event;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>用户角色更新事件</p>
 * 
 * @author Schilings
*/
@Getter
@Setter
public class RoleAuthorityChangedEvent extends AuthorityChangedEvent {
    
    private final String userId;
    
    public RoleAuthorityChangedEvent(String userId) {
        super(userId);
        this.userId = userId;
    }
}
