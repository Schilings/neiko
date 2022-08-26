package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.*;
import cn.dev33.satoken.basic.SaBasicUtil;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.strategy.SaStrategy;
import com.schilings.neiko.extend.sa.token.core.StpOauth2Logic;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.extend.sa.token.properties.ExtendSaTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ExtendAnnotationConfiguration {

	private final ExtendSaTokenProperties extendSaTokenProperties;

	/**
	 * 注入自定义Sa-Token校验逻辑StpOauth2Logic
	 */
	@Autowired
	public void putStpLogic() {
		SaManager.putStpLogic(new StpOauth2Logic(StpOauth2UserUtil.TYPE));
	}

	/**
	 * 重写Sa-Token注解搜索逻辑
	 */
	@Autowired
	public void rewriteGetAnnotation() {
		// 重写Sa-Token的注解处理器，增加注解合并功能
		SaStrategy.me.getAnnotation = (element, annotationClass) -> {
			return AnnotatedElementUtils.getMergedAnnotation(element, annotationClass);
		};
	}

	/**
	 * 重写Sa-Token注解校验逻辑
	 */
	@Autowired
	public void rewriteCheckElementAnnotation() {
		// 重写Sa-Token的注解处理器，增加注解合并功能
		SaStrategy.me.checkElementAnnotation = (target) -> {
			// 跳过注解鉴权
			if (extendSaTokenProperties.isEnforceCancelAuthenticate()) {
				return;
			}

			// 校验 @SaCheckLogin 注解
			SaCheckLogin checkLogin = (SaCheckLogin) SaStrategy.me.getAnnotation.apply(target, SaCheckLogin.class);
			if (checkLogin != null) {
				SaManager.getStpLogic(checkLogin.type()).checkByAnnotation(checkLogin);
			}

			// 校验 @SaCheckRole 注解
			SaCheckRole checkRole = (SaCheckRole) SaStrategy.me.getAnnotation.apply(target, SaCheckRole.class);
			if (checkRole != null) {
				SaManager.getStpLogic(checkRole.type()).checkByAnnotation(checkRole);
			}

			// 校验 @SaCheckPermission 注解
			SaCheckPermission checkPermission = (SaCheckPermission) SaStrategy.me.getAnnotation.apply(target,
					SaCheckPermission.class);
			if (checkPermission != null) {
				SaManager.getStpLogic(checkPermission.type()).checkByAnnotation(checkPermission);
			}

			// 校验 @SaCheckSafe 注解
			SaCheckSafe checkSafe = (SaCheckSafe) SaStrategy.me.getAnnotation.apply(target, SaCheckSafe.class);
			if (checkSafe != null) {
				SaManager.getStpLogic(checkSafe.type()).checkByAnnotation(checkSafe);
			}

			// 校验 @SaCheckBasic 注解
			SaCheckBasic checkBasic = (SaCheckBasic) SaStrategy.me.getAnnotation.apply(target, SaCheckBasic.class);
			if (checkBasic != null) {
				SaBasicUtil.check(checkBasic.realm(), checkBasic.account());
			}

			// 校验自定义 @Oauth2CheckScope 注解
			Oauth2CheckScope checkScope = (Oauth2CheckScope) SaStrategy.me.getAnnotation.apply(target,
					Oauth2CheckScope.class);
			if (checkScope != null) {
				((StpOauth2Logic) SaManager.getStpLogic(checkScope.type())).checkByAnnotation(checkScope);
			}
		};
	}

}
