package com.schilings.neiko.authorization.converter;

import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;

@Mapper
public interface OAuth2RegisteredClientConverter {

	OAuth2RegisteredClientConverter INSTANCE = Mappers.getMapper(OAuth2RegisteredClientConverter.class);

	@Mappings(value = {
			@Mapping(target = "clientAuthenticationMethods",
					expression = "java(commaDelimitedListToSet(po.getClientAuthenticationMethods()))"),
			@Mapping(target = "authorizationGrantTypes",
					expression = "java(commaDelimitedListToSet(po.getAuthorizationGrantTypes()))"),
			@Mapping(target = "redirectUris", expression = "java(commaDelimitedListToSet(po.getRedirectUris()))"),
			@Mapping(target = "scopes", expression = "java(commaDelimitedListToSet(po.getScopes()))"),
			@Mapping(target = "createTime", expression = "java(po.getCreateTime())"),
			@Mapping(target = "updateTime", expression = "java(po.getUpdateTime())"), })
	OAuth2RegisteredClientPageVO poToPageVo(OAuth2RegisteredClient po);

	@Mappings(value = {
			@Mapping(target = "clientAuthenticationMethods",
					expression = "java(collectionToCommaDelimitedString(dto.getClientAuthenticationMethods()))"),
			@Mapping(target = "authorizationGrantTypes",
					expression = "java(collectionToCommaDelimitedString(dto.getAuthorizationGrantTypes()))"),
			@Mapping(target = "redirectUris",
					expression = "java(collectionToCommaDelimitedString(dto.getRedirectUris()))"),
			@Mapping(target = "scopes", expression = "java(collectionToCommaDelimitedString(dto.getScopes()))") })
	OAuth2RegisteredClient dtoToPo(OAuth2RegisteredClientDTO dto);

	default Set<String> commaDelimitedListToSet(String str) {
		return StringUtils.commaDelimitedListToSet(str);
	}

	default String collectionToCommaDelimitedString(Collection<?> collection) {
		return StringUtils.collectionToCommaDelimitedString(collection);
	}

}
