package com.schilings.neiko.extend.sa.token.oauth2.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "权限Authority抽象实体")
public class PermissionAuthorityImpl implements PermissionAuthority {

	@Schema(title = "权限标识符")
	private String permission;

	@Override
	public String getPermission() {
		return null;
	}

}
