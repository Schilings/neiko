package com.schilings.neiko.store.util;

import com.sun.jna.*;

/**
 *
 * <p>
 * JNI
 * </p>
 *
 * @author Schilings
 */
public interface CLibrary extends Library {

	CLibrary INSTANCE = (CLibrary) Native.loadLibrary(Platform.isWindows() ? "msvcrt" : "c", CLibrary.class);

	int MADV_WILLNEED = 3;

	int MADV_DONTNEED = 4;

	int MCL_CURRENT = 1;

	int MCL_FUTURE = 2;

	int MCL_ONFAULT = 4;

	/* sync memory asynchronously */
	int MS_ASYNC = 0x0001;

	/* invalidate mappings & caches */
	int MS_INVALIDATE = 0x0002;

	/* synchronous memory sync */
	int MS_SYNC = 0x0004;

	/**
	 * 内存上锁
	 * @param var1
	 * @param var2
	 * @return
	 */
	int mlock(Pointer var1, NativeLong var2);

	/**
	 * 内存释放锁
	 * @param var1
	 * @param var2
	 * @return
	 */
	int munlock(Pointer var1, NativeLong var2);

	int madvise(Pointer var1, NativeLong var2, int var3);

	Pointer memset(Pointer p, int v, long len);

	int mlockall(int flags);

	int msync(Pointer p, NativeLong length, int flags);

}
