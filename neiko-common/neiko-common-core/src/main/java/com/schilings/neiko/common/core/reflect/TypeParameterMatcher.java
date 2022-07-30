package com.schilings.neiko.common.core.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TypeParameterMatcher {

	private static final TypeParameterMatcher NOOP = new TypeParameterMatcher() {
		@Override
		public boolean match(Object msg) {
			return true;
		}
	};

	public static TypeParameterMatcher get(final Class<?> parameterType) {
		if (parameterType == Object.class) {
			return NOOP;
		}
		else {
			return new ReflectiveMatcher(parameterType);
		}
	}

	public static TypeParameterMatcher find(final Object object, final Class<?> parametrizedSuperclass,
			final String typeParamName) {

		final Class<?> thisClass = object.getClass();
		return get(find0(object, parametrizedSuperclass, typeParamName));

	}

	private static Class<?> find0(final Object object, Class<?> parametrizedSuperclass, String typeParamName) {

		final Class<?> thisClass = object.getClass();
		Class<?> currentClass = thisClass;
		for (;;) {
			if (currentClass.getSuperclass() == parametrizedSuperclass) {
				int typeParamIndex = -1;
				TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
				for (int i = 0; i < typeParams.length; i++) {
					if (typeParamName.equals(typeParams[i].getName())) {
						typeParamIndex = i;
						break;
					}
				}

				if (typeParamIndex < 0) {
					throw new IllegalStateException(
							"unknown type parameter '" + typeParamName + "': " + parametrizedSuperclass);
				}

				Type genericSuperType = currentClass.getGenericSuperclass();
				if (!(genericSuperType instanceof ParameterizedType)) {
					return Object.class;
				}

				Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();

				Type actualTypeParam = actualTypeParams[typeParamIndex];
				if (actualTypeParam instanceof ParameterizedType) {
					actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
				}
				if (actualTypeParam instanceof Class) {
					return (Class<?>) actualTypeParam;
				}
				if (actualTypeParam instanceof GenericArrayType) {
					Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
					if (componentType instanceof ParameterizedType) {
						componentType = ((ParameterizedType) componentType).getRawType();
					}
					if (componentType instanceof Class) {
						return Array.newInstance((Class<?>) componentType, 0).getClass();
					}
				}
				if (actualTypeParam instanceof TypeVariable) {
					// Resolved type parameter points to another type parameter.
					TypeVariable<?> v = (TypeVariable<?>) actualTypeParam;
					if (!(v.getGenericDeclaration() instanceof Class)) {
						return Object.class;
					}

					currentClass = thisClass;
					parametrizedSuperclass = (Class<?>) v.getGenericDeclaration();
					typeParamName = v.getName();
					if (parametrizedSuperclass.isAssignableFrom(thisClass)) {
						continue;
					}
					return Object.class;
				}

				return fail(thisClass, typeParamName);
			}
			currentClass = currentClass.getSuperclass();
			if (currentClass == null) {
				return fail(thisClass, typeParamName);
			}
		}
	}

	private static Class<?> fail(Class<?> type, String typeParamName) {
		throw new IllegalStateException(
				"cannot determine the type of the type parameter '" + typeParamName + "': " + type);
	}

	public abstract boolean match(Object msg);

	private static final class ReflectiveMatcher extends TypeParameterMatcher {

		private final Class<?> type;

		ReflectiveMatcher(Class<?> type) {
			this.type = type;
		}

		@Override
		public boolean match(Object msg) {
			return type.isInstance(msg);
		}

	}

	TypeParameterMatcher() {
	}

}
