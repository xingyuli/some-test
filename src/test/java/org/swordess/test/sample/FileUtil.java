package org.swordess.test.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class FileUtil {

	static String ensureExistence(String filename) {
		File file = new File(filename);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		file.deleteOnExit();
		return file.getAbsolutePath();
	}
	
	static String ensureNonExistence(String filename) {
		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		}
		return file.getAbsolutePath();
	}
	
	static void write(String filename, byte b) throws IOException {
		try (FileOutputStream out = new FileOutputStream(filename)) {
			out.write(b);
		}
	}
	
	static void write(String filename, byte[] b) throws IOException {
		try (FileOutputStream out = new FileOutputStream(filename)) {
			out.write(b);
		}
	}
	
	private FileUtil() {
	}
	
}
