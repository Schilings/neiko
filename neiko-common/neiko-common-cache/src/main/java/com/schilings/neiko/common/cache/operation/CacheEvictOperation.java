package com.schilings.neiko.common.cache.operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheEvictOperation extends CacheOperation {

	/**
	 * 是否应该在调用方法之前进行删除缓存。
	 */
	private final boolean beforeInvocation;

	protected CacheEvictOperation(Builder b) {
		super(b);
		this.beforeInvocation = b.beforeInvocation;

	}

	@Setter
	public static class Builder extends CacheOperation.Builder {

		private boolean beforeInvocation;

		@Override
		public CacheEvictOperation build() {
			return new CacheEvictOperation(this);
		}

	}

}
