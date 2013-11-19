package org.jatakasource.common.svc.jmx;

import java.io.IOException;
import java.util.Properties;

public interface JmxCommons {
	public static final String JMX_SERVICE_URL = "service:jmx:rmi://%s/jndi/rmi://%s:%s/JatakaJMXconnector";
	public static final String JMX_OBJECT_PREFIX = "org.jatakasource.common.svc.jmx:type";
	public static final String JMX_URL_KEY = "org.jatakasource.common.jmx.url";
	public static final String DEFAULT_HOSTNAME = "localhost";
	public static final String HOST_NAME_ARG = "java.rmi.server.hostname";

	// By definition, no ports can be registered in the dynamic range.(49152-65535)
	public static final int JMX_DEFAULT_PORT = 49152;
	public static final int JMX_PORT_FINDER_MAX_TRIES = 100;

	Properties getJMXProperties() throws IOException;

	String getJMXPropertiesPath();

	String getJMXHostName();

	String getDefaultURL();
}