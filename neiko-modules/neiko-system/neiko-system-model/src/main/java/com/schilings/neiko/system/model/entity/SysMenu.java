package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Objects;

/**
 *
 * <p>
 * 系统权限
 * </p>
 *
 * @author Schilings
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("nk_sys_menu")
@Schema(title = "菜单权限")
public class SysMenu extends LogicDeletedBaseEntity {

	/**
	 * 菜单ID
	 */
	@TableId
	@Column(comment = "菜单ID")
	@Schema(title = "菜单ID")
	private Long id;

	/**
	 * 父级ID
	 */
	@Column(comment = "父级ID")
	@Schema(title = "父级ID")
	private Long parentId;

	/**
	 * 菜单名称
	 */
	@Column(comment = "菜单名称")
	@Schema(title = "菜单名称")
	private String title;

	/**
	 * 菜单图标
	 */
	@Column(comment = "菜单图标")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
	@Schema(title = "菜单图标")
	private String icon;

	/**
	 * 授权标识
	 */
	@Column(comment = "授权标识")
	@Schema(title = "授权标识")
	private String permission;

	/**
	 * 路由地址
	 */
	@Column(comment = "路由地址")
	@Schema(title = "路由地址")
	private String path;

	/**
	 * 打开方式 (1组件 2内链 3外链)
	 */
	@Column(comment = "打开方式 (1组件 2内链 3外链)")
	@Schema(title = "打开方式 (1组件 2内链 3外链)")
	private Integer targetType;

	/**
	 * 定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)
	 */
	@Column(comment = "定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)")
	@Schema(title = "定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)")
	private String uri;

	/**
	 * 显示排序
	 */
	@Column(comment = "显示排序")
	@Schema(title = "显示排序")
	private Integer sort;

	/**
	 * 组件缓存：0-开启，1-关闭
	 */
	@Column(comment = "组件缓存：0-开启，1-关闭")
	@Schema(title = "组件缓存：0-开启，1-关闭")
	private Integer keepAlive;

	/**
	 * 隐藏菜单: 0-否，1-是
	 */
	@Column(comment = "隐藏菜单:  0-否，1-是")
	@Schema(title = "隐藏菜单:  0-否，1-是")
	private Integer hidden;

	/**
	 * 菜单类型 （0目录，1菜单，2按钮）
	 */
	@Column(comment = "菜单类型 （0目录，1菜单，2按钮）")
	@Schema(title = "菜单类型 （0目录，1菜单，2按钮）")
	private Integer type;

	/**
	 * 备注信息
	 */
	@Column(comment = "备注信息")
	@Schema(title = "备注信息")
	private String remarks;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysMenu sysMenu = (SysMenu) o;
		return id.equals(sysMenu.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
