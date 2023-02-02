package com.schilings.neiko.authorization.biz.service.impl;

import cn.hutool.core.lang.Assert;
import com.schilings.neiko.authorization.biz.mapper.OAuth2RegisteredClientMapper;
import com.schilings.neiko.authorization.biz.service.OAuth2ClientSettingsService;
import com.schilings.neiko.authorization.biz.service.OAuth2RegisteredClientService;
import com.schilings.neiko.authorization.biz.service.OAuth2TokenSettingsService;
import com.schilings.neiko.authorization.converter.OAuth2RegisteredClientConverter;
import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import com.schilings.neiko.common.core.exception.ServiceException;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.security.util.PasswordUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.Instant;

import static com.schilings.neiko.authorization.biz.component.OAuth2RegisteredClientUtils.*;

@Service
public class OAuth2RegisteredClientServiceImpl
		extends ExtendServiceImpl<OAuth2RegisteredClientMapper, OAuth2RegisteredClient>
		implements OAuth2RegisteredClientService {

	private final OAuth2ClientSettingsService clientSettingsService;

	private final OAuth2TokenSettingsService tokenSettingsService;

	private final PasswordEncoder passwordEncoder;

	public OAuth2RegisteredClientServiceImpl(OAuth2ClientSettingsService clientSettingsService,
			OAuth2TokenSettingsService tokenSettingsService, ObjectProvider<PasswordEncoder> passwordEncoder) {
		this.clientSettingsService = clientSettingsService;
		this.tokenSettingsService = tokenSettingsService;
		this.passwordEncoder = passwordEncoder.getIfAvailable(PasswordUtils::createDelegatingPasswordEncoder);
	}

	@Override
	public PageResult<OAuth2RegisteredClientPageVO> queryPage(PageParam pageParam, OAuth2RegisteredClientQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	@Override
	public OAuth2RegisteredClient getByClientId(String clienId) {
		return baseMapper.selectOne(
				WrappersX.<OAuth2RegisteredClient>lambdaQueryX().eq(OAuth2RegisteredClient::getClientId, clienId));
	}

	@Override
	public OAuth2RegisteredClientInfo getClientInfoById(Long id) {
		OAuth2RegisteredClient entity = baseMapper.selectById(id);
		if (entity != null) {
			OAuth2RegisteredClientInfo info = new OAuth2RegisteredClientInfo();
			entityToInfo(entity, info);
			info.setClientSettings(clientSettingsService.getByClientId(entity.getClientId()));
			info.setTokenSettings(tokenSettingsService.getByClientId(entity.getClientId()));
			return info;
		}
		return null;
	}

	@Override
	public OAuth2RegisteredClientInfo getClientInfoByClientId(String clientId) {
		return getClientInfo(new OAuth2RegisteredClientQO().setClientId(clientId));
	}

	@Override
	public OAuth2RegisteredClientInfo getClientInfo(OAuth2RegisteredClientQO qo) {
		OAuth2RegisteredClient entity = baseMapper.queryOne(qo);
		if (entity != null) {
			OAuth2RegisteredClientInfo info = new OAuth2RegisteredClientInfo();
			entityToInfo(entity, info);
			info.setClientSettings(clientSettingsService.getByClientId(entity.getClientId()));
			info.setTokenSettings(tokenSettingsService.getByClientId(entity.getClientId()));
			return info;
		}
		return null;
	}

	@Override
	@Transactional
	public boolean saveRegisteredClient(OAuth2RegisteredClientDTO dto) {
		// 密码加密
		if (StringUtils.hasText(dto.getClientSecret())) {
			dto.setClientSecret(this.passwordEncoder.encode(dto.getClientSecret()));
		}
		// 持久化
		OAuth2RegisteredClient entity = OAuth2RegisteredClientConverter.INSTANCE.dtoToPo(dto);
		boolean success = save(entity);
		// settings处理
		OAuth2ClientSettingsDTO clientSettings = dto.getClientSettings() == null ? defaultClientSettings()
				: dto.getClientSettings();
		OAuth2TokenSettingsDTO tokenSettingsDTO = dto.getTokenSettings() == null ? defaultTokenSettings()
				: dto.getTokenSettings();
		clientSettings.setClientId(dto.getClientId());
		tokenSettingsDTO.setClientId(dto.getClientId());
		Assert.isTrue(clientSettingsService.saveOrUpdateClientSettings(clientSettings), () -> {
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "添加失败");
		});
		Assert.isTrue(tokenSettingsService.saveOrUpdateTokenSettings(tokenSettingsDTO), () -> {
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "添加失败");
		});
		return success;
	}

	@Override
	@Transactional
	public boolean updateRegisteredClient(OAuth2RegisteredClientDTO dto) {
		// 密码加密
		if (StringUtils.hasText(dto.getClientSecret())) {
			dto.setClientSecret(this.passwordEncoder.encode(dto.getClientSecret()));
		}
		// 持久化
		OAuth2RegisteredClient entity = OAuth2RegisteredClientConverter.INSTANCE.dtoToPo(dto);
		boolean success = updateById(entity);
		// settings处理
		OAuth2ClientSettingsDTO clientSettings = dto.getClientSettings();
		OAuth2TokenSettingsDTO tokenSettings = dto.getTokenSettings();
		if (clientSettings != null) {
			clientSettings.setClientId(dto.getClientId());
			Assert.isTrue(clientSettingsService.saveOrUpdateClientSettings(clientSettings), () -> {
				return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "更新失败");
			});
		}
		if (tokenSettings != null) {
			tokenSettings.setClientId(dto.getClientId());
			Assert.isTrue(tokenSettingsService.saveOrUpdateTokenSettings(tokenSettings), () -> {
				return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "更新失败");
			});
		}
		return success;
	}

	private void entityToInfo(OAuth2RegisteredClient entity, OAuth2RegisteredClientInfo info) {
		String clientIdIssuedAt = entity.getClientIdIssuedAt();
		String clientSecretExpiresAt = entity.getClientSecretExpiresAt();

		info.setId(entity.getId());
		info.setClientId(entity.getClientId());
		info.setClientSecret(entity.getClientSecret());
		info.setClientName(entity.getClientName());

		info.setClientIdIssuedAt(clientIdIssuedAt != null ? Instant.parse(clientIdIssuedAt) : null);
		info.setClientSecretExpiresAt(clientSecretExpiresAt != null ? Instant.parse(clientSecretExpiresAt) : null);

		info.setClientAuthenticationMethods(
				StringUtils.commaDelimitedListToSet(entity.getClientAuthenticationMethods()));
		info.setAuthorizationGrantTypes(StringUtils.commaDelimitedListToSet(entity.getAuthorizationGrantTypes()));
		info.setRedirectUris(StringUtils.commaDelimitedListToSet(entity.getRedirectUris()));
		info.setScopes(StringUtils.commaDelimitedListToSet(entity.getScopes()));
	}

}
