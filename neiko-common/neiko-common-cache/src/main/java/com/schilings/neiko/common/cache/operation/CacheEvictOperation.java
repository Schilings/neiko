package com.schilings.neiko.common.cache.operation;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheEvictOperation extends CacheOperation{

    /**
     * 是否删除缓存内的所有数据。
     */
    private final boolean allEntries;

    /**
     * 是否应该在调用方法之前进行删除缓存。 
     */
    private final boolean beforeInvocation;
    
    
    protected CacheEvictOperation(Builder b) {
        super(b);
        this.allEntries = b.allEntries;
        this.beforeInvocation = b.beforeInvocation;

    }
    
    @Setter
    public static class Builder extends CacheOperation.Builder {
        
        private boolean allEntries;

        private boolean beforeInvocation;

        @Override
        public CacheEvictOperation build() {
            return new CacheEvictOperation(this);
        }
    }
}
