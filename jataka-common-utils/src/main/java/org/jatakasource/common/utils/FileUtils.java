package org.jatakasource.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class FileUtils {
	private static final Logger logger = Logger.getLogger(FileUtils.class);

	public static void closeSilently(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			logger.error("Error while closing stream !!!");
		}
	}

	public static void closeSilently(InputStreamReader reader) {
		try {
			reader.close();
		} catch (IOException e) {
			logger.error("Error while closing stream !!!");
		}
	}

	/**
	 * Get directory path according to Class.
	 */
	public static <T> String getDirectoryPath(Class<T> owner) {
		String packageName = owner.getPackage().getName().replace(".", String.valueOf(File.separator));
		URL resource = owner.getClassLoader().getResource(packageName);

		if (resource != null) {
			return resource.getPath();
		}

		return null;
	}

	public static File getOSTempFolder() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * Read resource as Stream from class path and return the content as String
	 */
	public static String getResourceAsString(Class<?> resourceOwner, String relativeResource) {
		InputStream source = resourceOwner.getResourceAsStream(relativeResource);

		String content = null;
		try {
			content = IOUtils.toString(source);
			closeSilently(source);
		} catch (IOException e) {
			logger.error(e);
		}

		return content;
	}
	
	/**
	 * Read resource as Stream from class path and return the content as byte[]
	 */
	public static byte[] getResourceAsBytes(Class<?> resourceOwner, String relativeResource) {
		InputStream source = resourceOwner.getResourceAsStream(relativeResource);

		byte[] content = null;
		try {
			content = IOUtils.toByteArray(source);
			closeSilently(source);
		} catch (IOException e) {
			logger.error(e);
		}

		return content;
	}
}
