package com.schilings.neiko.samples.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schilings.neiko.samples.shiro.entity.User;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 16:22
 */
public interface UserService extends IService<User> {
    
    User selectByUsername(String username);
}
