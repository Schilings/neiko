package com.schilings.neiko.extend.sa.token.oauth2.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "角色Authority抽象实体")
public class RoleAuthorityImpl implements RoleAuthority {

	@Schema(title = "角色标识符")
	private String role;

	@Override
	public String getRole() {
		return role;
	}

}
