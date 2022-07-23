package com.schilings.neiko.common.cache.operation;

import org.springframework.lang.Nullable;

public interface CacheOperationInvoker {

    @Nullable
    Object invoke() throws ThrowableWrapper;

    class ThrowableWrapper extends RuntimeException {

        private final Throwable original;

        public ThrowableWrapper(Throwable original) {
            super(original.getMessage(), original);
            this.original = original;
        }

        public Throwable getOriginal() {
            return this.original;
        }
    }
}
