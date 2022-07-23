package com.schilings.neiko.common.cache.configuration;


import com.schilings.neiko.common.cache.EnableNeikoCaching;
import com.schilings.neiko.common.cache.components.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <pre>
 * <p>注解EnableNeikoCaching驱动引入配置类</p>
 * <p>{@link org.springframework.cache.annotation.AbstractCachingConfiguration}</p>
 * </pre>
 * @author Schilings
*/
@Configuration(proxyBeanMethods = false)
public abstract class AbstractCachingConfiguration implements ImportAware {

    /**
     * 注解EnableNeikoCaching的信息
     */
    @Nullable
    protected AnnotationAttributes enableCaching;
    
    @Bean
    @ConditionalOnMissingBean
    public CacheRepository simpleCacheRepository() {
        return new LocalCacheRepository();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(ObjectProvider<List<CacheRepository>> cacheRepository) {
        List<CacheRepository> repositoryList = cacheRepository.getIfAvailable();
        if (!repositoryList.isEmpty() && repositoryList != null) {
            return new GenericCacheManager(repositoryList);
        }
        return new GenericCacheManager();
    }
    

    /**
     * 确保是注解EnableNeikoCaching驱动
     * @param importMetadata
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        //取得注解信息
        this.enableCaching = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableNeikoCaching.class.getName()));
        if (this.enableCaching == null) {
            throw new IllegalArgumentException(
                    "@EnableNeikoCaching is not present on importing class " + importMetadata.getClassName());
        }
    }
}
