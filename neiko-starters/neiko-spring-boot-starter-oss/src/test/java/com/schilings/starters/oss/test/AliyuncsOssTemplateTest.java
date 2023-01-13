package com.schilings.starters.oss.test;


import com.schilings.neiko.autoconfigure.oss.DefaultOssTemplate;
import com.schilings.neiko.autoconfigure.oss.ObjectWithGlobalKeyPrefixOssTemplate;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.FileUpload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@ActiveProfiles("minio")
@RequiredArgsConstructor
public class AliyuncsOssTemplateTest extends AbstractOssTemplateTest {

	/**
	 * 测试用OSS名字
	 */
	private static final String TEST_BUCKET_NAME = "test";

	/**
	 * 测试用上传后的文件名
	 */
	private static final String TEST_OBJECT_NAME = "b.txt";

	/**
	 * 测试创建存储桶
	 */
	@Test
	public void createBucket() {
		String bucket = UUID.randomUUID().toString();
		CreateBucketResponse bucketResponse = ossTemplate.createBucket(bucket);
		Assertions.assertTrue(bucketResponse.sdkHttpResponse().isSuccessful());
		deleteBucket(bucket);
	}

	/**
	 * 测试罗列存储桶
	 */
	@Test
	public void listBuckets() {
		String bucket = UUID.randomUUID().toString();
		createBucket(bucket);
		List<Bucket> newBuckets = ossTemplate.listBuckets().buckets();
		List<String> bucketNames = newBuckets.stream().map(Bucket::name).collect(Collectors.toList());
		Assertions.assertTrue(bucketNames.contains(bucket));
		deleteBucket(bucket);
	}

	/**
	 * 测试删除存储桶
	 */
	@Test
	public void deleteBucket() {
		ossTemplate.deleteBucket("0fab8757-ef64-49fe-9cd0-697a9f50b2d7");
	}

	@Test
	public void getObjectPresignedUrl() {
		String objectPresignedUrl = ossTemplate.getObjectPresignedUrl(ossTemplate.getOssProperties().getBucket(),
				TEST_OBJECT_NAME, Duration.ofDays(1));
		System.out.println(objectPresignedUrl);
	}

	@Test
	public void getUrl() {
		String url = ossTemplate.getURL(ossTemplate.getOssProperties().getBucket(), TEST_OBJECT_NAME);
		System.out.println(url);
	}

	@Test
	public void getUrlWithCustomPrefix() {
		URL url = ossTemplate.getS3Client().utilities()
				.getUrl(GetUrlRequest.builder().bucket(ossTemplate.getOssProperties().getBucket()).key(TEST_OBJECT_NAME)
						.endpoint(URI.create(ossTemplate.getOssProperties().getEndpoint() + "/测试/"))
						.region(Region.of(ossTemplate.getOssProperties().getRegion())).build());

		System.out.println(url);
	}

	/**
	 * 测试获取对象
	 */
	@Test
	public void listObjects() {
		// 使用上传时自己构建的的对象名字查询
		List<S3Object> s3ObjectsWithUploadKey = ossTemplate.listObjects(ossTemplate.getOssProperties().getBucket(),
				TEST_OBJECT_NAME);
		Assertions.assertEquals(1, s3ObjectsWithUploadKey.size());
		Assertions.assertEquals(TEST_OBJECT_NAME, s3ObjectsWithUploadKey.get(0).key());
	}

	/**
	 * 测试文件上传 上传后的文件名和当前文件名保持一致
	 */
	@Test
	public void putObject() throws IOException {
		PutObjectResponse putObjectResponse = ossTemplate.putObject(ossTemplate.getOssProperties().getBucket(),
				TEST_OBJECT_NAME, ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "test.txt"));
		System.out.println(putObjectResponse);

	}

	@Test
	public void upload() throws FileNotFoundException {
		String bucket = UUID.randomUUID().toString();
		String key = UUID.randomUUID().toString();
		createBucket(bucket);
		FileUpload fileUpload = ossTemplate.uploadFile(key,
				ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "test.txt"));
		fileUpload.completionFuture().join();
		List<S3Object> s3Objects = ossTemplate.listObjects(key);
		Assertions.assertEquals(1, s3Objects.size());
		S3Object s3Object = s3Objects.get(0);
		Assertions.assertEquals(s3Object.key(), key);
		Assertions.assertEquals(s3Object.size(), 14);
		ossTemplate.deleteObject(key);
		deleteBucket(bucket);
	}

	@Test
	public void testBean() {
		if (objectKeyPrefixConverter.match()) {
			Assertions.assertInstanceOf(ObjectWithGlobalKeyPrefixOssTemplate.class, ossTemplate);
		}
		else {
			Assertions.assertInstanceOf(DefaultOssTemplate.class, ossTemplate);
		}
	}

}
