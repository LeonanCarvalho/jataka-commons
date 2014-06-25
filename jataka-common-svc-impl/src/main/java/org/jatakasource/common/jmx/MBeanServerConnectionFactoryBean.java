package org.jatakasource.common.jmx;

import java.io.IOException;

import org.jatakasource.common.svc.jmx.JmxCommons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * This beaan is references from common-jmx-context.xml
 */
public class MBeanServerConnectionFactoryBean extends org.springframework.jmx.support.MBeanServerConnectionFactoryBean {
	private static final Logger logger = LoggerFactory.getLogger(MBeanServerConnectionFactoryBean.class);

	private String serviceUrlStr;

	@Autowired
	private JmxCommons jmxCommons;

	/**
	 * serviceUrl is taken from JVM -D parameter. make sure serviceUrl is properly set. 
	 */
	@Override
	public void afterPropertiesSet() throws IOException {
		try {
			// load service URL from (/var/run/jatakasource/@PRODUCT@/@INSTANCE@/jmx)
			serviceUrlStr = jmxCommons.getJMXProperties().getProperty(JmxCommons.JMX_URL_KEY);
			this.setServiceUrl(serviceUrlStr);
		} catch (Exception e) {
			// Server is down
			logger.warn("JMX ServiceUrl is not set, probably due to missing jmx URL file!!!");

			// Set ServiceUrl outside of our port range 49152 + 10 + 1
			serviceUrlStr = jmxCommons.getDefaultURL();

			this.setServiceUrl(serviceUrlStr);
		}

		super.afterPropertiesSet();
	}

	public String getServiceUrl() {
		return serviceUrlStr;
	}
}