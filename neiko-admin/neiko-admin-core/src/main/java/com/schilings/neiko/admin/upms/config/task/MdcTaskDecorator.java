package com.schilings.neiko.admin.upms.config.task;

import cn.hutool.core.map.MapUtil;
import com.schilings.neiko.common.util.web.WebUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
			return () -> {
				// 现在：@Async线程上下文！
				// 恢复Web线程上下文的MDC数据
				if (MapUtil.isNotEmpty(copyOfContextMap)) {
					MDC.setContextMap(copyOfContextMap);
				}
				RequestContextHolder.setRequestAttributes(requestAttributes);
				runnable.run();
			};
		}
		finally {
			MDC.clear();
		}
	}

}
