package com.schilings.neiko.common.security.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.schilings.neiko.common.security.exception.SecurityException;
import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * https://sa-token.dev33.cn/doc/index.html#/up/password-secure
 * <p>
 * copy from sa-token
 * </p>
 * <p>
 * 之所以copy出来，是让common包不与框架耦合
 * </p>
 *
 * @author Schilings
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * Base64编码
	 */
	private static Base64.Encoder encoder = Base64.getEncoder();

	/**
	 * Base64解码
	 */
	private static Base64.Decoder decoder = Base64.getDecoder();

	// ----------------------- 摘要加密 -----------------------

	/**
	 * md5加密
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String md5(String str) {
		str = (str == null ? "" : str);
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] strA = new char[j * 2];
			int k = 0;
			for (byte byte0 : md) {
				strA[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strA[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strA);
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * sha1加密
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha1(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] b = str.getBytes();
			md.update(b);
			byte[] b2 = md.digest();
			int len = b2.length;
			String strA = "0123456789abcdef";
			char[] ch = strA.toCharArray();
			char[] chs = new char[len * 2];
			for (int i = 0, k = 0; i < len; i++) {
				byte b3 = b2[i];
				chs[k++] = ch[b3 >>> 4 & 0xf];
				chs[k++] = ch[b3 & 0xf];
			}
			return new String(chs);
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * sha256加密
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha256(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));

			byte[] bytes = messageDigest.digest();
			StringBuilder builder = new StringBuilder();
			String temp;
			for (int i = 0; i < bytes.length; i++) {
				temp = Integer.toHexString(bytes[i] & 0xFF);
				if (temp.length() == 1) {
					builder.append("0");
				}
				builder.append(temp);
			}

			return builder.toString();
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * md5加盐加密: md5(md5(str) + md5(salt))
	 * @param str 字符串
	 * @param salt 盐
	 * @return 加密后的字符串
	 */
	public static String md5BySalt(String str, String salt) {
		return md5(md5(str) + md5(salt));
	}

	// ----------------------- 对称加密 AES -----------------------

	/**
	 * 默认密码算法
	 */
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * AES加密
	 * @param key 加密的密钥
	 * @param text 需要加密的字符串
	 * @return 返回Base64转码后的加密数据
	 */
	public static String aesEncrypt(String key, String text) {
		try {
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			byte[] byteContent = text.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
			byte[] result = cipher.doFinal(byteContent);
			return encoder.encodeToString(result);
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * AES解密
	 * @param secretKey 加密的密钥
	 * @param text 已加密的密文
	 * @return 返回解密后的数据
	 */
	public static String aesDecrypt(String secretKey, String text) {
		try {
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey));
			byte[] result = cipher.doFinal(decoder.decode(text));
			return new String(result, "utf-8");
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * 生成加密秘钥
	 * @param password 秘钥
	 * @return SecretKeySpec
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(password.getBytes());
		kg.init(128, random);
		SecretKey secretKey = kg.generateKey();
		return new SecretKeySpec(secretKey.getEncoded(), "AES");
	}

	// -----------------自定义ASE加密-------------
	/**
	 * 将前端传递过来的密文解密为明文
	 * @param aesPass AES加密后的密文
	 * @param secretKey 密钥
	 * @return 明文密码
	 */
	public static String decodeAES(String aesPass, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		byte[] result = aes.decrypt(cn.hutool.core.codec.Base64.decode(aesPass.getBytes(StandardCharsets.UTF_8)));
		return new String(result, StandardCharsets.UTF_8);
	}

	/**
	 * 将明文密码加密为密文
	 * @param password 明文密码
	 * @param secretKey 密钥
	 * @return AES加密后的密文
	 */
	public static String encodeAESBase64(String password, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		return aes.encryptBase64(password, StandardCharsets.UTF_8);
	}

	// ----------------------- 非对称加密 RSA -----------------------

	private static final String ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024;

	private static final String PUBLIC_KEY = "public";
	private static final String PRIVATE_KEY = "private";

	public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	// ---------- 5个常用方法

	/**
	 * 生成密钥对
	 * @return Map对象 (private=私钥, public=公钥)
	 * @throws Exception 异常
	 */
	public static HashMap<String, String> rsaGenerateKeyPair() throws Exception {

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		KeyPair keyPair;

		try {
			keyPairGenerator.initialize(KEY_SIZE,
					new SecureRandom(UUID.randomUUID().toString().replaceAll("-", "").getBytes()));
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (InvalidParameterException e) {
			throw e;
		}
		catch (NullPointerException e) {
			throw e;
		}

		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

		HashMap<String, String> map = new HashMap<String, String>(16);
		map.put("private", encoder.encodeToString(rsaPrivateKey.getEncoded()));
		map.put("public", encoder.encodeToString(rsaPublicKey.getEncoded()));
		return map;
	}

	/**
	 * RSA公钥加密
	 * @param publicKeyString 公钥
	 * @param content 内容
	 * @return 加密后内容
	 */
	public static String rsaEncryptByPublic(String publicKeyString, String content) {
		try {
			// 获得公钥对象
			PublicKey publicKey = getPublicKeyFromString(publicKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content.getBytes(), splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * RSA私钥加密
	 * @param privateKeyString 私钥
	 * @param content 内容
	 * @return 加密后内容
	 */
	public static String rsaEncryptByPrivate(String privateKeyString, String content) {
		try {
			PrivateKey privateKey = getPrivateKeyFromString(privateKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content.getBytes(), splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * RSA公钥解密
	 * @param publicKeyString 公钥
	 * @param content 已加密内容
	 * @return 解密后内容
	 */
	public static String rsaDecryptByPublic(String publicKeyString, String content) {

		try {
			PublicKey publicKey = getPublicKeyFromString(publicKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * RSA私钥解密
	 * @param privateKeyString 公钥
	 * @param content 已加密内容
	 * @return 解密后内容
	 */
	public static String rsaDecryptByPrivate(String privateKeyString, String content) {
		try {
			PrivateKey privateKey = getPrivateKeyFromString(privateKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		}
		catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * 签名
	 * @param key	私钥
	 * @param requestData	请求参数
	 * @return
	 */
	public static String sign(String key, String requestData){
		String signature = null;
		byte[] signed = null;
		try {
			PrivateKey privateKey = getPrivateKeyFromString(key);

			Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
			Sign.initSign(privateKey);
			Sign.update(requestData.getBytes());
			signed = Sign.sign();

			signature = Base64.getEncoder().encodeToString(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return signature;
	}

	/**
	 * 验签
	 * @param key	公钥
	 * @param requestData	请求参数
	 * @param signature	签名
	 * @return
	 */
	public static boolean verifySign(String key, String requestData, String signature){
		boolean verifySignSuccess = false;
		try {
			PublicKey publicKey = getPublicKeyFromString(key);

			Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
			verifySign.initVerify(publicKey);
			verifySign.update(requestData.getBytes());
			verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return verifySignSuccess;
	}
	

	// ---------- 获取*钥

	/** 根据公钥字符串获取 公钥对象 */
	private static PublicKey getPublicKeyFromString(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		// 过滤掉\r\n
		key = key.replace("\r\n", "");

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decoder.decode(key));

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

		return publicKey;
	}

	/** 根据私钥字符串获取 私钥对象 */
	private static PrivateKey getPrivateKeyFromString(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		// 过滤掉\r\n
		key = key.replace("\r\n", "");

		// 取得私钥
		PKCS8EncodedKeySpec x509KeySpec = new PKCS8EncodedKeySpec(decoder.decode(key));

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		PrivateKey privateKey = keyFactory.generatePrivate(x509KeySpec);

		return privateKey;
	}

	// ---------- 一些辅助方法

	/** 根据限定的每组字节长度，将字节数组分组 */
	private static byte[][] splitBytes(byte[] bytes, int splitLength) {

		// bytes与splitLength的余数
		int remainder = bytes.length % splitLength;
		// 数据拆分后的组数，余数不为0时加1
		int quotient = remainder != 0 ? bytes.length / splitLength + 1 : bytes.length / splitLength;
		byte[][] arrays = new byte[quotient][];
		byte[] array = null;
		for (int i = 0; i < quotient; i++) {
			// 如果是最后一组（quotient-1）,同时余数不等于0，就将最后一组设置为remainder的长度
			if (i == quotient - 1 && remainder != 0) {
				array = new byte[remainder];
				System.arraycopy(bytes, i * splitLength, array, 0, remainder);
			}
			else {
				array = new byte[splitLength];
				System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
			}
			arrays[i] = array;
		}
		return arrays;
	}

	/** 将字节数组转换成16进制字符串 */
	private static String bytesToHexString(byte[] bytes) {

		StringBuffer sb = new StringBuffer(bytes.length);
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(0xFF & bytes[i]);
			if (temp.length() < 2) {
				sb.append(0);
			}
			sb.append(temp);
		}
		return sb.toString();
	}

	/** 将16进制字符串转换成字节数组 */
	private static byte[] hexStringToBytes(String hex) {

		int len = (hex.length() / 2);
		hex = hex.toUpperCase();
		byte[] result = new byte[len];
		char[] chars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
		}
		return result;
	}

	/** 将char转换为byte */
	private static byte toByte(char c) {

		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
