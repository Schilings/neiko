package com.schilings.neiko.authorization.converter;


import com.schilings.neiko.authorization.model.dto.AuthorizationConsentDTO;
import com.schilings.neiko.authorization.model.entity.AuthorizationConsent;
import com.schilings.neiko.authorization.model.vo.AuthorizationConsentPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;

@Mapper
public interface AuthorizationConsentConverter {

    AuthorizationConsentConverter INSTANCE = Mappers.getMapper(AuthorizationConsentConverter.class);

    AuthorizationConsent dtoToPo(AuthorizationConsentDTO dto);

    @Mappings(value = {
            @Mapping(target = "authorities", expression = "java(commaDelimitedListToSet(po.getAuthorities()))"),
            @Mapping(target = "createTime", expression = "java(po.getCreateTime())"),
            @Mapping(target = "updateTime", expression = "java(po.getUpdateTime())"),
    })
    AuthorizationConsentPageVO poToPageVo(AuthorizationConsent po);

    default Set<String> commaDelimitedListToSet(String str) {
        return StringUtils.commaDelimitedListToSet(str);
    }

    default String collectionToCommaDelimitedString(Collection<?> collection) {
        return StringUtils.collectionToCommaDelimitedString(collection);
    }
}
