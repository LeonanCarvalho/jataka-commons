package org.jatakasource.common.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class PropertiesUtils {
	private static final Logger logger = Logger.getLogger(PropertiesUtils.class);

	public static Properties get(String filePath) {
		return get(filePath, true);
	}

	/**
	 * Read properties file.
	 */
	public static Properties get(String filePath, boolean errorIfMissing) {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);

			properties.load(fis);

			return properties;
		} catch (IOException e) {
			if (errorIfMissing)
				logger.error("Error loading properties from " + filePath, e);
			else
				logger.error("Error loading properties from " + filePath + " exception: " + e.getMessage());
			logger.debug("Error loading properties from " + filePath + " exception: " + e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(fis);
		}

		return null;
	}

	/**
	 * Write properties file..
	 */
	public static void save(String filePath, Properties properties) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);

			properties.store(fos, null);
		} catch (IOException e) {
			logger.error("Error saving properties to " + filePath + " exception: " + e.getMessage());
			logger.debug("Error saving properties to " + filePath + " exception: " + e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Load a properties file from the classpath
	 * 
	 * @param propsName
	 * @return Properties
	 * @throws Exception
	 */
	public static Properties load(String propsName) throws Exception {
		Properties props = new Properties();
		URL url = ClassLoader.getSystemResource(propsName);
		props.load(url.openStream());
		return props;
	}

	/**
	 * Load a Properties File
	 * 
	 * @param propsFile
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties load(File propsFile) {
		return load(propsFile, "UTF-8");
	}

	/**
	 * Load a Properties File with encoding
	 * 
	 * @param propsFile
	 *            , encoding
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties load(File propsFile, String encoding) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(propsFile);
			if (is != null) {
				props.load(new InputStreamReader(is, encoding));
			}
		} catch (IOException ioe) {
			logger.error("Error loading properties " + propsFile.getName() + " exception: " + ioe.getMessage());
			logger.debug("Error loading properties " + propsFile.getName() + " exception: " + ioe.getMessage(), ioe);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("Error loading properties " + propsFile.getName() + " exception: " + e.getMessage());
				logger.debug("Error loading properties " + propsFile.getName() + " exception: " + e.getMessage(), e);
			}
		}
		return props;
	}

	public static Double getDoubleValue(Object value, Double defaultValue) {
		Double newValue = getDoubleValue(value);

		if (newValue == null) {
			return defaultValue;
		}

		return newValue;
	}

	public static Double getDoubleValue(Object value) {
		Double newValue = null;
		if (value != null && value instanceof Double) {
			newValue = ((Double) value);
		} else if (value != null && value instanceof BigDecimal) {
			newValue = ((BigDecimal) value).doubleValue();
		} else if (value != null && value instanceof Long) {
			newValue = ((Long) value).doubleValue();
		} else if (value != null && value instanceof Integer) {
			newValue = ((Integer) value).doubleValue();
		}

		return newValue;
	}

	public static Integer getIntegerValue(Object value, Integer defaultValue) {
		Integer newValue = getIntegerValue(value);

		if (newValue == null) {
			return defaultValue;
		}

		return newValue;
	}

	public static Integer getIntegerValue(Object value) {
		Integer newValue = null;

		if (value != null && value instanceof Integer) {
			newValue = (Integer) value;
		} else if (value != null && value instanceof Double) {
			newValue = ((Double) value).intValue();
		} else if (value != null && value instanceof Long) {
			newValue = ((Long) value).intValue();
		} else if (value != null && value instanceof BigDecimal) {
			newValue = ((BigDecimal) value).intValue();
		} else if (value != null && value instanceof String) {
			newValue = Integer.valueOf((String) value);
		}

		return newValue;
	}

	public static Long getLongValue(Object value, Long defaultValue) {
		Long newValue = getLongValue(value);

		if (newValue == null) {
			return defaultValue;
		}

		return newValue;
	}

	public static Long getLongValue(Object value) {
		Long newValue = null;
		if (value != null && value instanceof Long) {
			newValue = ((Long) value);
		} else if (value != null && value instanceof Double) {
			newValue = ((Double) value).longValue();
		} else if (value != null && value instanceof Integer) {
			newValue = Long.valueOf((Integer) value);
		} else if (value != null && value instanceof String) {
			if (NumberUtils.isNumber(value.toString()))
				newValue = Long.valueOf((String) value);
		}

		return newValue;
	}
}
