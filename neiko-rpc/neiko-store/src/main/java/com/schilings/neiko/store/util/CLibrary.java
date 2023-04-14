package com.schilings.neiko.store.util;

import com.sun.jna.*;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.win32.W32APITypeMapper;

/**
 *
 * <p>
 * JNI https://blog.csdn.net/mrathena/article/details/125036604
 * </p>
 *
 * @author Schilings
 */
public interface CLibrary extends Library {
	
	CLibrary INSTANCE = load();
	
	static CLibrary load(){
		if (Platform.isWindows()) {
			//return Native.load("c", CLibrary.class);
			return Native.load("kernel32", CLibrary.class, W32APIOptions.DEFAULT_OPTIONS);
		}
		return Native.load("msvcrt", CLibrary.class);
	}
	
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
	
	default int memoryLock(Pointer var1,long var2){
		if (Platform.isWindows()) {
			INSTANCE.VirtualLock(var1, new BaseTSD.SIZE_T(var2));
			return 0;
		}
		return INSTANCE.mlock(var1, new NativeLong(var2));
	}

	default int memoryUnlock(Pointer var1,long var2){
		if (Platform.isWindows()) {
			INSTANCE.VirtualUnlock(var1, new BaseTSD.SIZE_T(var2));
			return 0;
		}
		return INSTANCE.munlock(var1, new NativeLong(var2));
	}

	default int memoryAdvise(Pointer pointer,long fileSize){
		if (Platform.isWindows()) {
			return INSTANCE.PrefetchVirtualMemory(Kernel32.INSTANCE.GetCurrentProcess(), new BaseTSD.ULONG_PTR(1),
					new WIN32_MEMORY_RANGE_ENTRY(pointer,new BaseTSD.SIZE_T(fileSize)), new BaseTSD.SIZE_T(0));
		}
		return INSTANCE.madvise(pointer, new NativeLong(fileSize), CLibrary.MADV_WILLNEED);
	}
	
	
	/**
	 * issue: https://github.com/apache/rocketmq/issues/4202
	 * windows下内存上锁 
	 * @param lpAddress
	 * @param dwSize
	 * @return
	 */
	boolean VirtualLock(Pointer lpAddress, BaseTSD.SIZE_T dwSize);

	/**
	 * 内存释放锁
	 * @param lpAddress
	 * @param dwSize
	 * @return
	 */
	boolean VirtualUnlock(Pointer lpAddress, BaseTSD.SIZE_T dwSize);


	/**
	 * @param hProcess 要预提取其虚拟地址范围的进程句柄。 使用 GetCurrentProcess 函数使用当前进程。
	 * @param numberOfEntries VirtualAddresses 参数指向的数组中的条目数。
	 * @param var1 指向 WIN32_MEMORY_RANGE_ENTRY 结构的数组的指针，每个结构都指定要预提取的虚拟地址范围。 虚拟地址范围可能涵盖目标进程可访问的进程地址空间的任何部分。
	 * @param var2 保留。 必须为 0。 
	 * @return 如果函数成功，则返回值为非零值。
	 * 如果函数失败，则返回值为 0 (零) 。 要获得更多的错误信息，请调用 GetLastError。
	 */
	int PrefetchVirtualMemory(WinNT.HANDLE hProcess,BaseTSD.ULONG_PTR numberOfEntries,WIN32_MEMORY_RANGE_ENTRY var1, BaseTSD.SIZE_T var2);


	/**
	 * issue: https://github.com/apache/rocketmq/issues/4202
	 * linux下内存上锁 
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


	@Structure.FieldOrder({"VirtualAddress", "NumberOfBytes"})
	public static class WIN32_MEMORY_RANGE_ENTRY extends Structure{
		public Pointer VirtualAddress;
		public BaseTSD.SIZE_T NumberOfBytes;

		public WIN32_MEMORY_RANGE_ENTRY(Pointer virtualAddress, BaseTSD.SIZE_T numberOfBytes) {
			VirtualAddress = virtualAddress;
			NumberOfBytes = numberOfBytes;
		}
	}
	
}
