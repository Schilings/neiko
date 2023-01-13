package com.schilings.starters.oss.test;


import com.schilings.neiko.autoconfigure.oss.OssAutoConfiguration;
import com.schilings.neiko.autoconfigure.oss.OssTemplate;
import com.schilings.neiko.autoconfigure.oss.prefix.ObjectKeyPrefixConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OssAutoConfiguration.class)
public abstract class AbstractOssTemplateTest {

    @Autowired
    protected OssTemplate ossTemplate;

    @Autowired
    protected ObjectKeyPrefixConverter objectKeyPrefixConverter;

    protected void createBucket(String bucket) {
        ossTemplate.createBucket(bucket);
    }

    protected void deleteBucket(String bucket) {
        ossTemplate.deleteBucket(bucket);
    }

}
