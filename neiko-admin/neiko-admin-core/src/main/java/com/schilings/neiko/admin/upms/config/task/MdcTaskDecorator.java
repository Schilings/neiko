package com.schilings.neiko.admin.upms.config.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.map.MapUtil;
import com.schilings.neiko.common.util.reflect.ReflectUtils;
import com.schilings.neiko.common.util.web.WebUtils;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.core.task.TaskDecorator;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 使async异步任务支持traceId
 *
 * @author huyuanzhi 2021-11-06 23:14:27
 */
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		try {
			Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
			
			ServletRequestAttributes requestAttributes = WebUtils.getServletRequestAttributes();
			//延迟Servlet的生命周期，防止Request回收(Request.recycle())
			AsyncContext asyncContext = requestAttributes.getRequest().startAsync();
			return () -> {
				// 现在：@Async线程上下文！
				// 恢复Web线程上下文的MDC数据
				if (MapUtil.isNotEmpty(copyOfContextMap)) {
					MDC.setContextMap(copyOfContextMap);
				}
				RequestContextHolder.setRequestAttributes(requestAttributes);
				runnable.run();
				asyncContext.complete();
			};
		}
		finally {
			MDC.clear();
		}
	}

}
