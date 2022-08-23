package com.schilings.neiko.common.security.component;


import com.schilings.neiko.common.security.pojo.UserDetails;

/**
 * 
 * <p>加载用户特定数据的核心接口。
 * 它在整个框架中用作用户 DAO</p>
 * 
 * @author Schilings
*/
public interface UserDetailsService<T extends UserDetails> {

    /**
     * 根据用户名定位用户。
     * @param username
     * @return
     */
    T loadUserByUsername(String username);

    /**
     * 根据用户ID定位用户。
     * @param userId
     * @return
     */
    T loadUserByUserId(String userId);
    
    
}
