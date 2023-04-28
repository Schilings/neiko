package com.schilings.neiko.store;


import com.schilings.neiko.store.config.StoreConfig;
import com.schilings.neiko.store.manage.DefaultStoreManager;
import com.schilings.neiko.store.manage.PutStoreResult;
import com.schilings.neiko.store.manage.StoreData;
import com.schilings.neiko.store.manage.StoreRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreRepositoryTest {

    @Test
    public void test1() throws IOException, ExecutionException, InterruptedException {

        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setStorePathRootDir("target\\unit_test_store");
        storeConfig.setStorePathData("target\\unit_test_store\\data");
        storeConfig.setMappedFileSize(1024);
        DefaultStoreManager defaultStoreManager = new DefaultStoreManager(storeConfig);
        StoreRepository storeRepository = defaultStoreManager.getStoreRepository();

        final String fixedMsg = "0123456789abcde";
        for (int i = 0; i < 1024; i++) {
            CompletableFuture<PutStoreResult> future = storeRepository.asyncAppend(new StoreData(fixedMsg.getBytes()));
            if (future.isDone()) {
                System.out.println(future.get().getStoreStatus());
            }
        }

        new CountDownLatch(1).await();
        
    }
}
