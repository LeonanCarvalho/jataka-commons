package org.jatakasource.common.jmx;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jatakasource.common.network.AvailablePortFinder;
import org.jatakasource.common.svc.jmx.JmxCommons;
import org.jatakasource.common.svc.jmx.JmxCommonsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * initialize JMX properties (host, port). if default port (49152) is in use -
 * search next available port.service url is written to file system.
 * 
 * This component should be scanned/initialized only for modules who wises to
 * setup a jmx-server.
 * 
 * @author yaniv
 */

public class JmxPropertiesLoaderImpl implements JmxPropertiesLoader {
	private static final Logger logger = LoggerFactory.getLogger(JmxPropertiesLoaderImpl.class);

	private static int jmxPort = JmxCommonsImpl.JMX_DEFAULT_PORT;

	@Autowired
	private JmxCommons commons;

	public int initialize() {
		return initialize(JmxCommonsImpl.JMX_DEFAULT_PORT);
	}

	public synchronized int initialize(int port) {
		Properties jmxProperties = new Properties();

		// Only server beans can change the url and save it to system file.
		jmxPort = getJMXAvailablePort(port);

		String hostname = commons.getJMXHostName();
		String url = String.format(JmxCommons.JMX_SERVICE_URL, hostname, hostname, jmxPort);

		jmxProperties.setProperty(JmxCommons.JMX_URL_KEY, url);

		saveJMXProperties(jmxProperties);

		logger.debug("JMX service is available at URL: " + url);

		return jmxPort;
	}

	public int getJMXAvailablePort(int port) {
		for (int i = 0; i < JmxCommons.JMX_PORT_FINDER_MAX_TRIES; i++, port++) {
			// Check if JMX port is available.
			if (AvailablePortFinder.available(port)) {
				return port;
			}
		}

		// Throw exception if all ports are in use.
		String message = "All ports from: " + String.valueOf(port) + " to: " + String.valueOf(port - JmxCommons.JMX_PORT_FINDER_MAX_TRIES) + " are in use !!! ";

		logger.error(message);

		throw new RuntimeException(message);
	}

	private void saveJMXProperties(Properties jmxProperties) {
		final File file = new File(commons.getJMXPropertiesPath());
		try {
			FileUtils.writeStringToFile(file, jmxProperties.getProperty(JmxCommons.JMX_URL_KEY));

			// delete the file when the loader exits
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					boolean success = file.delete();
					if (!success) {
						logger.error("Failed deleting JMX properties file " + file.getAbsolutePath());
					}
				}
			});
		} catch (IOException e) {
			logger.error("Unable to save jmx service url to file !!! " + file.getAbsolutePath());
		}
	}

	public final int getJMXPort() {
		return jmxPort;
	}
}