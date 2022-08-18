package com.schilings.neiko.extend.mybatis.plus.core;

import org.apache.ibatis.type.*;
import org.springframework.core.ResolvableType;

import java.sql.*;
import java.util.List;

/**
 *
 * <p>
 * {@link TypeHandlerRegistry}
 * </p>
 * <p>
 * {@link LocalDateTimeTypeHandler}
 * </p>
 *
 * @author Schilings
 */
public class ListTypeHandler extends BaseTypeHandler<Object> {

	private final Class<?> targetClass = null;

	public ListTypeHandler() {
		super();
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
			throws SQLException {
		if (parameter instanceof List) {
			Class<?> targetClass = ResolvableType.forInstance(parameter).getGeneric(0).getRawClass();

		}
		else {
			if (!parameter.getClass().isArray()) {
				throw new TypeException(
						"ArrayType Handler requires SQL array or java array parameter and does not support type "
								+ parameter.getClass());
			}
			Class<?> componentType = parameter.getClass().getComponentType();
		}

	}

	@Override
	public List getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}

	@Override
	public List getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public List getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return null;
	}

}
