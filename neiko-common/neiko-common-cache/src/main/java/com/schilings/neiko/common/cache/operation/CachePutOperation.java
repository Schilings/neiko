package com.schilings.neiko.common.cache.operation;


import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * <p>对应@NeikoCachePut的缓存操作</p>
 * </pre>
 * @author Schilings
*/
@Setter
@Getter
public class CachePutOperation extends CacheOperation {
    /**
     * 不缓存条件
     */
    private final String unless;
    
    protected CachePutOperation(Builder b) {
        super(b);
        this.unless = b.unless;
    }

    @Setter
    public static class Builder extends CacheOperation.Builder {

        private String unless;
        
        @Override
        public CachePutOperation build() {
            return new CachePutOperation(this);
        }
    }
}
