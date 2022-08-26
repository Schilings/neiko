package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Objects;

/**
 *
 * <p>
 * 角色表
 * </p>
 *
 * @author Schilings
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("nk_sys_role")
@Schema(title = "角色")
public class SysRole extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId
	@Column(comment = "角色ID编号", isKey = true)
	@Schema(title = "角色编号")
	private Long id;

	@Column(comment = "角色名称")
	@Schema(title = "角色名称")
	private String name;

	@Column(comment = "角色标识")
	@Schema(title = "角色标识")
	private String code;

	@Column(comment = "角色类型，1：系统角色 2：业务角色")
	@Schema(title = "角色类型，1：系统角色 2：业务角色")
	private Integer type;

	@Column(comment = "数据权限")
	@Schema(title = "数据权限")
	private Integer scopeType;

	@Column(comment = "数据范围资源，当数据范围类型为自定义时使用")
	@Schema(title = "数据范围资源，当数据范围类型为自定义时使用")
	private String scopeResources;

	@Column(comment = "角色备注")
	@Schema(title = "角色备注")
	private String remarks;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysRole sysRole = (SysRole) o;
		return code.equals(sysRole.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

}
