package org.jatakasource.common.svc.jmx;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JmxCommonsImpl implements JmxCommons {
	private static final Logger logger = Logger.getLogger(JmxCommonsImpl.class);

	// Set default port outside of our port range 49152 + 10 + 1
	private final static int DEFAULT_PORT = JMX_DEFAULT_PORT + JMX_PORT_FINDER_MAX_TRIES + 1;
	private static final String JMX_FILE = "jmx";

	public Properties getJMXProperties() throws IOException {
		return getJMXProperties(getJMXPropertiesPath());
	}

	private Properties getJMXProperties(String jmxPropertiesPath) throws IOException {
		String url;
		try {
			url = FileUtils.readFileToString(new File(jmxPropertiesPath));
		} catch (IOException e) {
			logger.debug("Unable to load jmx service url from -> " + jmxPropertiesPath);
			throw e;
		}

		// Initialize jmxProperties only if we did't had an Exception.
		// Don't cache this in static member, when server is crashed jmx file is
		// not deleted but replaced on next startup.
		Properties jmxProperties = new Properties();

		jmxProperties.setProperty(JMX_URL_KEY, url);

		return jmxProperties;
	}

	public String getJMXPropertiesPath() {
		String path = System.getProperties().getProperty(JmxCommons.JMX_URL_KEY);
		if (StringUtils.isEmpty(path)) {
			logger.error("Unable to get -Dorg.jatakasource.common.jmx.url parameter, make sure this paremter is properly set");
		}

		return path + File.separator + JMX_FILE;
	}

	public String getJMXHostName() {
		String hostName = System.getProperty(HOST_NAME_ARG);

		if (hostName == null) {
			hostName = DEFAULT_HOSTNAME;
			logger.warn("\"" + HOST_NAME_ARG + "\" parameter is missing. Defaulting to \"" + DEFAULT_HOSTNAME + "\"");
		}

		return hostName;
	}

	public String getDefaultURL() {
		return String.format(JMX_SERVICE_URL, DEFAULT_HOSTNAME, DEFAULT_HOSTNAME, DEFAULT_PORT);
	}
}
