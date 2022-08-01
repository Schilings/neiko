package com.schilings.neiko.autoconfigure.web.pageable;

import com.schilings.neiko.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <pre>
 * <p>分页参数解析器{@link PageableArgumentResolver},{@link ExpressionValueMethodArgumentResolver}</p>
 * </pre>
 *
 * @author Schilings
 */
public interface PageParamArgumentResolver extends HandlerMethodArgumentResolver {

	/**
	 * Resolves a {@link PageParam} method parameter into an argument value from a given
	 * request.
	 * @param parameter the method parameter to resolve. This parameter must have
	 * previously been passed to {@link #supportsParameter} which must have returned
	 * {@code true}.
	 * @param mavContainer the ModelAndViewContainer for the current request
	 * @param webRequest the current request
	 * @param binderFactory a factory for creating {@link WebDataBinder} instances
	 * @return the resolved argument value, or {@code null} if not resolvable
	 * @throws Exception in case of errors with the preparation of argument values
	 */
	@Override
	PageParam resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception;

}
