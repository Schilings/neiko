package com.schilings.neiko.common.cache.operation;

import lombok.Getter;
import lombok.Setter;

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
	 * 不缓存条件
	 */
	private final String unless;

	public CacheableOperation(Builder b) {
		super(b);
		this.unless = b.unless;

	}

	public String getUnless() {
		return this.unless;
	}

	@Setter
	public static class Builder extends CacheOperation.Builder {

		private String unless;

		public void setUnless(String unless) {
			this.unless = unless;
		}

		@Override
		public CacheableOperation build() {
			return new CacheableOperation(this);
		}

	}

}
