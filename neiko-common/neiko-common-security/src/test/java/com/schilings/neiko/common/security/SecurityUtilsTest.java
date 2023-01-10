package com.schilings.neiko.common.security;

import com.schilings.neiko.common.security.utils.SecurityUtils;
import org.junit.jupiter.api.Test;

public class SecurityUtilsTest {

	 public void testMd5() {
	 System.out.println(SecurityUtils.md5BySalt("a123456", null));
	 System.out.println(SecurityUtils.md5BySalt("1001", null));

	 }

	 public void testAse() {
	 System.out.println(SecurityUtils.aesEncrypt("key", "ase"));//HX19ChxGmts3l/KkHnWmiw==
	 System.out.println(SecurityUtils.aesEncrypt("HX19ChxGmts3l/KkHnWmiw==", "1001"));//2dDIszpc8wCAfJHxVtMENw==
	 System.out.println(SecurityUtils.aesEncrypt("HX19ChxGmts3l/KkHnWmiw==", "a123456"));// IOUPsmOqsLY19z8BGidSFA==

	 System.out.println(check("2dDIszpc8wCAfJHxVtMENw==",
	 "99109f1b0cf6ae9b4572d99fc8bcc0df", null));
	 System.out.println(check("IOUPsmOqsLY19z8BGidSFA==",
	 "51f5c2f36d9803ab55d4ba5cee9a3e48", null));
	 }

	 public void testCheck() {
	 System.out.println(SecurityUtils.aesDecrypt("key", "bJtG7AW/n7WRQBJK25o/pw=="));

	 }

	 public void testCustomAse() {
	 System.out.println(SecurityUtils.encodeAESBase64("1001", "HX19ChxGmts3l/Kk"));
	 System.out.println(SecurityUtils.decodeAES("CgoW0TROJmENVc3erSHlEA==",
	 "HX19ChxGmts3l/Kk"));
	 }

	 public boolean check(String fontPass, String backPass, String salt) {
	 // 解析出明文密码
	 String plainPass = SecurityUtils.aesDecrypt("HX19ChxGmts3l/KkHnWmiw==", fontPass);
	 return SecurityUtils.md5BySalt(plainPass, salt).equals(backPass);
	 }

}
