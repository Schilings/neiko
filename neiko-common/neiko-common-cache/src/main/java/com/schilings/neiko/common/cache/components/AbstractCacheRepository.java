package com.schilings.neiko.common.cache.components;

import com.schilings.neiko.common.cache.parser.CacheAnnotationParser;
import com.schilings.neiko.common.cache.parser.NeikoCacheAnnotationParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <pre>
 * <p>CacheRepository的抽象实现</p>
 * </pre>
 *
 * @author Schilings
 */
@Getter
@Service
@Slf4j
public abstract class AbstractCacheRepository implements CacheRepository {

	private final boolean publicMethodsOnly;

	private final CacheAnnotationParser annotationParser;

	public AbstractCacheRepository() {
		this(true);
	}

	public AbstractCacheRepository(boolean publicMethodsOnly) {
		this.publicMethodsOnly = publicMethodsOnly;
		this.annotationParser = new NeikoCacheAnnotationParser();
	}

	public AbstractCacheRepository(boolean publicMethodsOnly, CacheAnnotationParser annotationParser) {
		this.publicMethodsOnly = publicMethodsOnly;
		this.annotationParser = annotationParser;
	}

	public AbstractCacheRepository(CacheAnnotationParser annotationParser) {
		this.publicMethodsOnly = true;
		Assert.notNull(annotationParser, "CacheAnnotationParser must not be null");
		this.annotationParser = annotationParser;
	}

}
