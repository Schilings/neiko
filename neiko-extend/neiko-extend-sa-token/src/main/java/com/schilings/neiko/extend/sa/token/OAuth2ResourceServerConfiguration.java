package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.*;
import cn.dev33.satoken.basic.SaBasicUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import com.schilings.neiko.extend.sa.token.bean.ExtendAnnotationInterceptor;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2Logic;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.OpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.OpaqueTokenIntrospectorWrapper;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.RemoteOpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.SharedStoredOpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.properties.OAuth2ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@Import(OpaqueTokenIntrospectorConfiguration.class)
@EnableConfigurationProperties(OAuth2ResourceServerProperties.class)
public class OAuth2ResourceServerConfiguration {

	private final OAuth2ResourceServerProperties resourceServerProperties;

	private final OpaqueTokenIntrospectorWrapper opaqueTokenIntrospectorWrapper;

	/**
	 *
	 * <p>
	 * Web Mvc鉴权拦截器配置
	 * </p>
	 *
	 * @author Schilings
	 */
	@Configuration
	@RequiredArgsConstructor
	public class SaTokenConfigure implements WebMvcConfigurer {

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// 注册Sa-Token的注解拦截器，打开注解式鉴权功能，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
			registry.addInterceptor(
					new ExtendAnnotationInterceptor(resourceServerProperties, opaqueTokenIntrospectorWrapper))
					.addPathPatterns("/**").excludePathPatterns("/oauth2/**")
					.excludePathPatterns(resourceServerProperties.getIgnoreUrls());

			// 注册Sa-Token的路由拦截器，并排除不需要鉴权的接口地址 (与登录拦截器无关)
			// registry.addInterceptor(new ExtendRouteInterceptor())
			// .addPathPatterns("/**")
			// .excludePathPatterns("/oauth2/**")
			// .excludePathPatterns(resourceServerProperties.getIgnoreUrls());
		}

	}

	/**
	 *
	 * <p>
	 * 注解鉴权拓展配置类
	 * </p>
	 *
	 * @author Schilings
	 */
	@Configuration(proxyBeanMethods = false)
	@RequiredArgsConstructor
	public class ExtendStrategyConfiguration {

		private final Set<Class<? extends Annotation>> CACHE_OPERATION_ANNOTATIONS = new LinkedHashSet<>(8);

		/**
		 * 是否只允许public缓存
		 */
		@Setter
		private boolean publicMethodsOnly = false;

		public boolean allowPublicMethodsOnly() {
			return publicMethodsOnly;
		}

		/**
		 * 支持的注解
		 */
		@PostConstruct
		public void init() {
			CACHE_OPERATION_ANNOTATIONS.add(SaCheckLogin.class);
			CACHE_OPERATION_ANNOTATIONS.add(SaCheckRole.class);
			CACHE_OPERATION_ANNOTATIONS.add(SaCheckPermission.class);
			CACHE_OPERATION_ANNOTATIONS.add(SaCheckSafe.class);
			CACHE_OPERATION_ANNOTATIONS.add(SaCheckBasic.class);
			CACHE_OPERATION_ANNOTATIONS.add(OAuth2CheckScope.class);
		}

		/**
		 * 注入自定义Sa-Token校验逻辑StpOauth2Logic
		 */
		@PostConstruct
		public void putStpLogic() {
			SaManager.putStpLogic(new StpOAuth2Logic(StpOAuth2UserUtil.TYPE));
		}

		/**
		 * 重写Sa-Token注解搜索逻辑
		 */
		@PostConstruct
		public void rewriteGetAnnotation() {
			// 重写Sa-Token的注解处理器，增加注解合并功能
			SaStrategy.me.getAnnotation = (element, annotationClass) -> {
				return AnnotatedElementUtils.getMergedAnnotation(element, annotationClass);
			};
		}

		/**
		 * 重写Sa-Token注解校验逻辑
		 */
		@PostConstruct
		public void rewriteCheckElementAnnotation() {
			// 重写Sa-Token的注解处理器，增加注解合并功能
			SaStrategy.me.checkElementAnnotation = (target) -> {
				// 校验 @SaCheckLogin 注解
				if (resourceServerProperties.isCheckLogin()) {
					SaCheckLogin checkLogin = (SaCheckLogin) SaStrategy.me.getAnnotation.apply(target,
							SaCheckLogin.class);
					if (checkLogin != null) {
						SaManager.getStpLogic(checkLogin.type()).checkByAnnotation(checkLogin);
					}
				}
				// 校验 @SaCheckRole 注解
				if (resourceServerProperties.isCheckRole()) {
					SaCheckRole checkRole = (SaCheckRole) SaStrategy.me.getAnnotation.apply(target, SaCheckRole.class);
					if (checkRole != null) {
						SaManager.getStpLogic(checkRole.type()).checkByAnnotation(checkRole);
					}
				}
				// 校验 @SaCheckPermission 注解
				if (resourceServerProperties.isCheckPermission()) {
					SaCheckPermission checkPermission = (SaCheckPermission) SaStrategy.me.getAnnotation.apply(target,
							SaCheckPermission.class);
					if (checkPermission != null) {
						SaManager.getStpLogic(checkPermission.type()).checkByAnnotation(checkPermission);
					}
				}
				// 校验 @SaCheckSafe 注解
				if (resourceServerProperties.isCheckSafe()) {
					SaCheckSafe checkSafe = (SaCheckSafe) SaStrategy.me.getAnnotation.apply(target, SaCheckSafe.class);
					if (checkSafe != null) {
						SaManager.getStpLogic(checkSafe.type()).checkByAnnotation(checkSafe);
					}
				}
				// 校验 @SaCheckBasic 注解
				if (resourceServerProperties.isCheckBasic()) {
					SaCheckBasic checkBasic = (SaCheckBasic) SaStrategy.me.getAnnotation.apply(target,
							SaCheckBasic.class);
					if (checkBasic != null) {
						SaBasicUtil.check(checkBasic.realm(), checkBasic.account());
					}
				}

				// 校验自定义 @Oauth2CheckScope 注解
				if (resourceServerProperties.isCheckScope()) {
					OAuth2CheckScope checkScope = (OAuth2CheckScope) SaStrategy.me.getAnnotation.apply(target,
							OAuth2CheckScope.class);
					if (checkScope != null) {
						((StpOAuth2Logic) SaManager.getStpLogic(checkScope.type())).checkByAnnotation(checkScope);
					}
				}
			};

			// 重写Sa-Token的注解处理器，增加注解合并功能
			// SaStrategy.me.checkElementAnnotation = (target) -> {
			//
			// //查询注解
			// Collection<? extends Annotation> anns = (publicMethodsOnly
			// ? AnnotatedElementUtils.getAllMergedAnnotations(target,
			// CACHE_OPERATION_ANNOTATIONS)
			// : AnnotatedElementUtils.findAllMergedAnnotations(target,
			// CACHE_OPERATION_ANNOTATIONS));
			// // 如果不存在注解声明
			// if (anns.isEmpty()) {
			// return;
			// }
			//
			// // 校验 @SaCheckLogin 注解
			// anns.stream().filter(ann -> resourceServerProperties.isCheckLogin() && ann
			// instanceof SaCheckLogin).forEach(
			// // 解析@NeikoCacheable返回CacheableOperation
			// ann -> {
			// SaCheckLogin checkAnn = (SaCheckLogin) ann;
			// SaManager.getStpLogic(checkAnn.type()).checkByAnnotation(checkAnn);
			// });
			// // 校验 @SaCheckRole 注解
			// anns.stream().filter(ann -> resourceServerProperties.isCheckRole() && ann
			// instanceof SaCheckLogin).forEach(
			// ann -> {
			// SaCheckRole checkAnn = (SaCheckRole) ann;
			// SaManager.getStpLogic(checkAnn.type()).checkByAnnotation(checkAnn);
			// });
			// // 校验 @SaCheckPermission 注解
			// anns.stream().filter(ann -> resourceServerProperties.isCheckPermission() &&
			// ann instanceof SaCheckPermission).forEach(
			// ann -> {
			// SaCheckPermission checkAnn = (SaCheckPermission) ann;
			// SaManager.getStpLogic(checkAnn.type()).checkByAnnotation(checkAnn);
			// });
			// // 校验 @SaCheckSafe 注解
			// anns.stream().filter(ann -> resourceServerProperties.isCheckSafe() && ann
			// instanceof SaCheckSafe).forEach(
			// ann -> {
			// SaCheckSafe checkAnn = (SaCheckSafe) ann;
			// SaManager.getStpLogic(checkAnn.type()).checkByAnnotation(checkAnn);
			// });
			// // 校验 @SaCheckBasic 注解
			// anns.stream().filter(ann -> resourceServerProperties.isCheckBasic() && ann
			// instanceof SaCheckBasic).forEach(
			// ann -> {
			// SaCheckBasic checkAnn = (SaCheckBasic) ann;
			// SaBasicUtil.check(checkAnn.realm(), checkAnn.account());
			// });
			// // 校验 @OAuth2CheckScope 注解
			// anns.stream().filter(ann -> resourceServerProperties.isCheckBasic() && ann
			// instanceof OAuth2CheckScope).forEach(
			// ann -> {
			// OAuth2CheckScope checkAnn = (OAuth2CheckScope) ann;
			// ((StpOAuth2Logic)
			// SaManager.getStpLogic(checkAnn.type())).checkByAnnotation(checkAnn);
			// });
			// };
		}

	}

}
