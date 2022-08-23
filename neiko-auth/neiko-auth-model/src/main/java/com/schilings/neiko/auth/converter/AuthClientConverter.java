package com.schilings.neiko.auth.converter;

import com.schilings.neiko.auth.model.dto.AuthClientDetails;
import com.schilings.neiko.auth.model.entity.AuthClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthClientConverter {

    AuthClientConverter INSTANCE = Mappers.getMapper(AuthClientConverter.class);

    @Mappings({
            //@Mapping(target = "scope",expression = "java(Arrays.stream(authClient.getScope().split(\",\")).collect(Collectors.toSet()))"),
            //@Mapping(target = "urls",expression = "java(Arrays.stream(authClient.getAllowUrl.split(\",\")).collect(Collectors.toSet()))"),
            //@Mapping(target = "authorizedGrantTypes",expression = "java(Arrays.stream(authClient.getAuthorizedGrantTypes.split(\",\")).collect(Collectors.toSet()))"),
            @Mapping(target = "scope",source = "scope"),
            @Mapping(target = "urls",source = "allowUrl"),
            @Mapping(target = "authorizedGrantTypes",source = "authorizedGrantTypes")
    })
    AuthClientDetails poToDetails(AuthClient authClient);
}
