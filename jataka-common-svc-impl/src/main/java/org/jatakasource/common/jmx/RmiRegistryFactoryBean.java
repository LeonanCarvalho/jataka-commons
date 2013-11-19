package org.jatakasource.common.jmx;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class RmiRegistryFactoryBean extends org.springframework.remoting.rmi.RmiRegistryFactoryBean {
	private final static Logger logger = Logger.getLogger(RmiRegistryFactoryBean.class);

	@Autowired
	private JmxPropertiesLoader jmxPropertiesLoader;

	/*
	 * There might be a race condition when several servers are started
	 * simultaneously, so LocateRegistry search previously registered JMX.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			boolean reinitialize = false;
			super.setPort(jmxPropertiesLoader.getJMXPort());

			// Search existing JMX Registries.
			while (checkExistingRegistry(super.getPort())) {
				super.setPort(jmxPropertiesLoader.getJMXAvailablePort(super.getPort() + 1));
				reinitialize = true;
			}

			// Reinitialize JMXPropertiesLoader for new port.
			if (reinitialize) {
				jmxPropertiesLoader.initialize(super.getPort());
			}

			// Assume no registry found - so this IP/Port is available
			// setAlwaysCreate to true will avoid additional call to
			// testRegistry
			super.setAlwaysCreate(true);
			super.afterPropertiesSet();
		} catch (Exception e) {
			logger.debug("RMI Registry threw exception !!! " + e.getMessage());
		}
	}

	private boolean checkExistingRegistry(int port) {
		try {
			// Retrieve existing registry.
			Registry reg = LocateRegistry.getRegistry(port);
			testRegistry(reg);

			// Assume registry already exists in other process.
			return true;
		} catch (ConnectException cex) {
			// Assume no registry found - so this IP/Port is available
			logger.debug("Could not detect RMI registry - creating new one (IP/Port is available)");
			return false;
		} catch (RemoteException ex) {
			logger.error("RMI registry access threw exception while checkExistingRegistry !!! " + ex.getMessage());
			return false;
		}
	}
}