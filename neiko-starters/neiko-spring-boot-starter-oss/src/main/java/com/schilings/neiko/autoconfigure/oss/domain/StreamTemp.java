package com.schilings.neiko.autoconfigure.oss.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.io.*;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class StreamTemp {

	private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"), "neiko/oss/stream");

	static {
		if (!TEMP_DIR.exists()) {
			TEMP_DIR.mkdirs();
		}
	}

	private final Long size;

	private final InputStream stream;

	public static StreamTemp of(InputStream stream) throws IOException {
		File file;

		do {
			file = new File(TEMP_DIR, System.currentTimeMillis() + ".stream.tmp");
		}
		while (!file.createNewFile());

		try (final FileOutputStream out = new FileOutputStream(file)) {
			long size = 0;
			byte[] buffer = new byte[1024];
			int len;

			while ((len = stream.read(buffer)) > -1) {
				size += len;
				out.write(buffer, 0, len);
			}

			return new StreamTemp(size, new FileInputStream(file));
		}
	}

}
