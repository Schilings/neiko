package com.schilings.neiko.extend.openapi.pageable;

/**
 * 通过内存的字节数组直接加载 class
 */
public class ByteClassLoader extends ClassLoader {

	public Class<?> defineClass(byte[] bytes) {
		return defineClass(null, bytes, 0, bytes.length);
	}

}
