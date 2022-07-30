package com.schilings.neiko.common.util.reflect;

import cn.hutool.core.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>{@code
 *
 * }
 * <p>反射操作工具类</p>
 * </pre>
 *
 * @author Schilings
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectUtils extends ReflectUtil {

	private org.springframework.cglib.core.ReflectUtils springReflect;

}
