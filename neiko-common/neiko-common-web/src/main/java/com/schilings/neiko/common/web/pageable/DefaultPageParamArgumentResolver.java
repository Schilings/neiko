package com.schilings.neiko.common.web.pageable;


import com.schilings.neiko.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <pre>
 * <p>默认的分页参数解析器</p>
 * </pre>
 * @author Schilings
*/
public class DefaultPageParamArgumentResolver implements PageParamArgumentResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public PageParam resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }
}
