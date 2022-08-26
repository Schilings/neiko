package com.schilings.neiko.samples.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 16:19
 */
@TableName("t_user")
@Data
public class User {

	@TableId
	private Long id;

	private String username;

	private String password;

	private String type;

	private LocalDateTime createTime;

}
