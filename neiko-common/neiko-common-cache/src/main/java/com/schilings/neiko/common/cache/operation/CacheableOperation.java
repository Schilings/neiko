package com.schilings.neiko.common.cache.operation;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

/**
 * <pre>
 * <p>对应@NeikoCacheable的缓存操作</p>
 * </pre>
 *
 * @author Schilings
 */
@Getter
@Setter
public class CacheableOperation extends CacheOperation {

	/**
	 * 数据类型
	 */
	private final Type returnType;

	/**
	 * 不缓存条件
	 */
	private final String unless;

	public CacheableOperation(Builder b) {
		super(b);
		this.unless = b.unless;
		this.returnType = b.returnType;

	}

	public String getUnless() {
		return this.unless;
	}

	@Setter
	public static class Builder extends CacheOperation.Builder {

		private Type returnType;

		private String unless;

		public void setUnless(String unless) {
			this.unless = unless;
		}

		public void setReturnType(Type returnType) {
			this.returnType = returnType;
		}

		@Override
		public CacheableOperation build() {
			return new CacheableOperation(this);
		}

	}

}
