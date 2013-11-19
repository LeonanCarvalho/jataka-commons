package org.jatakasource.common.jmx;

public interface JmxPropertiesLoader {

	int getJMXAvailablePort(int port);

	int initialize(int port);

	int getJMXPort();

}