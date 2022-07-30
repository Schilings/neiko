package com.schilings.neiko.common.cache.components;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * <p>通用的CacheManager</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class GenericCacheManager extends AbstractCacheManager {

	public GenericCacheManager() {
		super(Collections.singletonList(new LocalCacheRepository()));
	}

	public GenericCacheManager(List<CacheRepository> cacheRepositories) {
		super(cacheRepositories);
	}

}
