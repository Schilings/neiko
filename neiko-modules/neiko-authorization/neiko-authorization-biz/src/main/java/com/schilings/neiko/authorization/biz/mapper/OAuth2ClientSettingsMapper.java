package com.schilings.neiko.authorization.biz.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;

public interface OAuth2ClientSettingsMapper extends ExtendMapper<OAuth2ClientSettings> {


    default OAuth2ClientSettings selectByClientId(String clientId) {
        //return this.selectOne(WrappersX.<OAuth2ClientSettings>lambdaQueryX().eq(OAuth2ClientSettings::getClientId, clientId));
        return this.selectById(clientId);
    }

    default Integer updateByClientId(OAuth2ClientSettings entity) {
        return this.updateById(entity);
    }
}
