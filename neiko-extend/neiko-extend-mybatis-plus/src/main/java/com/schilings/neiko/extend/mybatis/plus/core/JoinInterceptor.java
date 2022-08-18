package com.schilings.neiko.extend.mybatis.plus.core;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.schilings.neiko.common.util.StringUtils;
import com.schilings.neiko.extend.mybatis.plus.constants.Constant;
import com.schilings.neiko.extend.mybatis.plus.method.JoinResultType;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连表拦截器 用于实现动态resultType 之前的实现方式是mybatis-plus的Interceptor,无法修改args,存在并发问题 所以将这个拦截器独立出来
 *
 * @author yulichang
 */
@Intercepts({ 
		@Signature(
				type = Executor.class, 
				method = "query", 
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }
		)
})
public class JoinInterceptor implements Interceptor {

	private static final Log logger = LogFactory.getLog(JoinInterceptor.class);

	private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);

	/**
	 * 缓存MappedStatement,不需要每次都去重写构建MappedStatement
	 */
	private static final Map<String, Map<Configuration, MappedStatement>> MS_CACHE = new ConcurrentHashMap<>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		if (args[0] instanceof MappedStatement) {
			MappedStatement ms = (MappedStatement) args[0];
			if (args[1] instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) args[1];
				Object ew = map.containsKey(Constants.WRAPPER) ? map.get(Constants.WRAPPER) : null;
				if (!map.containsKey(Constant.PARAM_TYPE)) {
					map.put(Constant.PARAM_TYPE, Objects.nonNull(ew) && (ew instanceof JoinResultType));
				}
				else {
					logger.warn(String.format("请不要使用MPJ预留参数名 %s", Constant.PARAM_TYPE));
				}
				if (CollectionUtils.isNotEmpty(map) && map.containsKey(Constant.CLAZZ)) {
					Class<?> clazz = (Class<?>) map.get(Constant.CLAZZ);
					if (Objects.nonNull(clazz)) {
						List<ResultMap> list = ms.getResultMaps();
						if (CollectionUtils.isNotEmpty(list)) {
							ResultMap resultMap = list.get(0);
							// 如果是Join方法
							if (resultMap.getType() == JoinResultType.class) {
								String result = (String) map.get(Constant.RESULT_MAP);
								args[0] = newMappedStatement(ms, result, clazz);
							}
						}
					}
				}
			}
		}
		return invocation.proceed();
	}

	/**
	 * 构建新的MappedStatement
	 */
	public MappedStatement newMappedStatement(MappedStatement ms, String resultMap, Class<?> resultType) {
		String id = ms.getId() + StringPool.UNDERSCORE + resultType.getName();
		Map<Configuration, MappedStatement> statementMap = MS_CACHE.get(id);
		if (CollectionUtils.isNotEmpty(statementMap)) {
			MappedStatement statement = statementMap.get(ms.getConfiguration());
			if (Objects.nonNull(statement)) {
				return statement;
			}
		}
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(),
				ms.getSqlCommandType()).resource(ms.getResource()).fetchSize(ms.getFetchSize())
						.statementType(ms.getStatementType()).keyGenerator(ms.getKeyGenerator())
						.timeout(ms.getTimeout()).parameterMap(ms.getParameterMap())
						.resultSetType(ms.getResultSetType()).cache(ms.getCache())
						.flushCacheRequired(ms.isFlushCacheRequired()).useCache(ms.isUseCache());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			builder.keyProperty(String.join(StringPool.COMMA, ms.getKeyProperties()));
		}
		List<ResultMap> resultMaps = new ArrayList<>();
		resultMaps.add(newResultMap(ms, resultMap, resultType));
		builder.resultMaps(resultMaps);
		MappedStatement mappedStatement = builder.build();

		if (statementMap == null) {
			statementMap = new ConcurrentHashMap<>();
			MS_CACHE.put(id, statementMap);
		}
		statementMap.put(ms.getConfiguration(), mappedStatement);
		return mappedStatement;
	}

	/**
	 * 构建resultMap
	 */
	private ResultMap newResultMap(MappedStatement ms, String resultMapId, Class<?> resultType) {
		ResultMap resultMap = null;
		// 指定ResultMap
		if (StringUtils.isNotBlank(resultMapId)) {
			resultMap = ms.getConfiguration().getResultMap(resultMapId);
		}
		else {
			// 表
			TableInfo tableInfo = TableInfoHelper.getTableInfo(resultType);
			if (tableInfo != null && tableInfo.getEntityType() == resultType) {
				if (tableInfo.isAutoInitResultMap()) {
					return ms.getConfiguration().getResultMap(tableInfo.getResultMap());
				}
				else {
					// 设置 tableInfo的autoInitResultMap属性 为 true
					ReflectUtil.setFieldValue(tableInfo, "autoInitResultMap", true);
					// 调用 tableInfo#initResultMapIfNeed() 方法，自动构建 resultMap 并注入
					ReflectUtil.invoke(tableInfo, "initResultMapIfNeed");
				}
			}
			// DTO
			else {
				MapperBuilderAssistant assistant = new MapperBuilderAssistant(ms.getConfiguration(), "");
				assistant.setCurrentNamespace(ms.getId().substring(0, ms.getId().lastIndexOf(".")));
				tableInfo = TableInfoHelper.initTableInfo(assistant, resultType);
				if (!tableInfo.isAutoInitResultMap()) {
					// 设置 tableInfo的autoInitResultMap属性 为 true
					ReflectUtil.setFieldValue(tableInfo, "autoInitResultMap", true);
					// 调用 tableInfo#initResultMapIfNeed() 方法，自动构建 resultMap 并注入
					ReflectUtil.invoke(tableInfo, "initResultMapIfNeed");
				}
			}

			resultMap = ms.getConfiguration().getResultMap(tableInfo.getResultMap());
			// resultMap.forceNestedResultMaps();
		}
		Assert.notNull(resultMap, "The ResultMap of {" + resultType.getName() + "} can not be null");
		return resultMap;
	}

}
