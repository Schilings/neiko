package com.schilings.neiko.common.cache.operation;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CacheableOperation extends CacheOperation{

    /**
     * 有效时间
     */
    private final long ttl;

    /**
     * 时间单位
     */
    private final TimeUnit unit;

    /**
     * 不缓存条件
     */
    private final String unless;

    /**
     * 是否加锁同步
     */
    private final boolean sync;

    
    public CacheableOperation(Builder b) {
        super(b);
        this.unless = b.unless;
        this.sync = b.sync;
        this.ttl = b.ttl;
        this.unit = b.unit;
    }

    
    public String getUnless() {
        return this.unless;
    }

    public boolean isSync() {
        return this.sync;
    }

    
    @Setter
    public static class Builder extends CacheOperation.Builder {
        
        private String unless;

        private boolean sync;
        
        private long ttl;

        private TimeUnit unit;

        public void setUnless(String unless) {
            this.unless = unless;
        }

        public void setSync(boolean sync) {
            this.sync = sync;
        }
        

        @Override
        public CacheableOperation build() {
            return new CacheableOperation(this);
        }
    }
}
