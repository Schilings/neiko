package com.schilings.neiko.store;

import com.schilings.neiko.svrutil.UtilAll;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.function.Supplier;

public class MultiPathMappedFileQueue extends MappedFileQueue {

	private final Supplier<Set<String>> fullStorePathsSupplier;

	private final String readOnlyStorePath;

	public MultiPathMappedFileQueue(String storePath, int mappedFileSize, String readOnlyStorePath,
			AllocateMappedFileService allocateMappedFileService, Supplier<Set<String>> fullStorePathsSupplier) {
		super(storePath, mappedFileSize, allocateMappedFileService);
		this.readOnlyStorePath = readOnlyStorePath;
		this.fullStorePathsSupplier = fullStorePathsSupplier;
	}

	private Set<String> getPaths() {
		String[] paths = this.getStorePath().trim().split(",");
		return new HashSet<>(Arrays.asList(paths));
	}

	private Set<String> getReadonlyPaths() {
		String pathStr = this.readOnlyStorePath;
		if (StringUtils.isBlank(pathStr)) {
			return Collections.emptySet();
		}
		String[] paths = pathStr.trim().split(",");
		return new HashSet<>(Arrays.asList(paths));
	}

	@Override
	public boolean load() {
		Set<String> storePathSet = getPaths();
		storePathSet.addAll(getReadonlyPaths());

		List<File> files = new ArrayList<>();
		for (String path : storePathSet) {
			File dir = new File(path);
			File[] ls = dir.listFiles();
			if (ls != null) {
				Collections.addAll(files, ls);
			}
		}

		return doLoad(files);
	}

	@Override
	protected MappedFile tryCreateMappedFile(long createOffset) {
		Set<String> storePath = getPaths();
		Set<String> readonlyPathSet = getReadonlyPaths();
		Set<String> fullStorePaths = fullStorePathsSupplier == null ? Collections.emptySet()
				: fullStorePathsSupplier.get();

		HashSet<String> availableStorePath = new HashSet<>(storePath);
		// 不要在只读存储路径中创建文件
		availableStorePath.removeAll(readonlyPathSet);
		// 不创建文件是空间快满了
		availableStorePath.removeAll(fullStorePaths);
		// 如果没有存储路径，则回退到可写存储路径。
		if (availableStorePath.isEmpty()) {
			availableStorePath = new HashSet<>(storePath);
			availableStorePath.removeAll(readonlyPathSet);
		}

		String[] paths = availableStorePath.toArray(new String[] {});
		Arrays.sort(paths);
		long fileIdx = createOffset / this.mappedFileSize;
		String nextFilePath = paths[(int) (fileIdx % paths.length)] + File.separator
				+ UtilAll.offset2FileName(createOffset);
		String nextNextFilePath = paths[(int) ((fileIdx + 1) % paths.length)] + File.separator
				+ UtilAll.offset2FileName(createOffset + this.mappedFileSize);
		return doCreateMappedFile(nextFilePath, nextNextFilePath);
	}

	@Override
	public void destroy() {
		for (MappedFile mf : this.mappedFiles) {
			mf.destroy(1000 * 3);
		}
		this.mappedFiles.clear();
		this.flushedWhere = 0;

		Set<String> storePathSet = getPaths();
		storePathSet.addAll(getReadonlyPaths());

		for (String path : storePathSet) {
			File file = new File(path);
			if (file.isDirectory()) {
				file.delete();
			}
		}
	}

}
