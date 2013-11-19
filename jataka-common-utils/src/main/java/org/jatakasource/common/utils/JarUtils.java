package org.jatakasource.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;

public class JarUtils {
	private static final Logger logger = Logger.getLogger(JarUtils.class);
	public static final String JAR_EXTENSION = ".jar";

	/**
	 * List all files according to owner class.
	 */
	public static List<String> listResources(Class<?> owner, Locale locale, String fileSuffix) {
		URLConnection connection = null;
		ZipInputStream zipStream = null;

		List<String> resources = new ArrayList<String>();
		Package pkg = owner.getPackage();
		try {
			// Get URLConnection either from jar file of from nested jar in war.
			connection = owner.getProtectionDomain().getCodeSource().getLocation().openConnection();
			String packageName = pkg.getName().replace(".", File.separator);
			packageName = addLocale(locale, packageName);

			logger.debug("Searching resources in jar: " + connection.getURL());

			if (isJarFile(connection)) {
				zipStream = new ZipInputStream(connection.getInputStream());
				ZipEntry zipEntry = null;
				while ((zipEntry = zipStream.getNextEntry()) != null) {
					// Only files that start with packageName
					// Only files that ends with fileSuffix
					if (zipEntry.getName().startsWith(packageName) && zipEntry.getName().toLowerCase().endsWith(fileSuffix.toLowerCase())) {
						resources.add(zipEntry.getName());
					}
				}
			} else {
				// Get resource from exploded package (e.g eclipse target/classes)
				String resourceFolder = owner.getProtectionDomain().getCodeSource().getLocation().getPath() + packageName;
				Collection<File> files = org.apache.commons.io.FileUtils.listFiles(new File(resourceFolder), new SuffixFileFilter(fileSuffix), TrueFileFilter.TRUE);

				for (File file : files) {
					if (file.getName().toLowerCase().endsWith(fileSuffix.toLowerCase())) {
						resources.add(packageName + File.separator + file.getName());
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error while getting resource list from jar !!!", e);
		} finally {
			if (zipStream != null) {
				FileUtils.closeSilently(zipStream);
			}
		}

		return resources;
	}

	private static boolean isJarFile(URLConnection connection) {
		return connection.getURL().toString().endsWith(JAR_EXTENSION);
	}

	public static String addLocale(Locale locale, String packageName) {
		if (!packageName.endsWith(File.separator))
			packageName += File.separator;
		packageName += locale.toString();

		return packageName;
	}
}
