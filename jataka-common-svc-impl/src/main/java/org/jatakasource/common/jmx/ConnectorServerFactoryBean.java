package org.jatakasource.common.jmx;

import java.io.IOException;

import javax.management.JMException;

import org.apache.log4j.Logger;
import org.jatakasource.common.svc.jmx.JmxCommons;
import org.springframework.beans.factory.annotation.Autowired;

public class ConnectorServerFactoryBean extends org.springframework.jmx.support.ConnectorServerFactoryBean {
	private static final Logger logger = Logger.getLogger(ConnectorServerFactoryBean.class);

	@Autowired
	private JmxCommons jmxCommons;

	@Override
	public void afterPropertiesSet() throws JMException, IOException {
		// Set service url from JMXPropertiesLoader.
		this.setServiceUrl(jmxCommons.getJMXProperties().getProperty(JmxCommons.JMX_URL_KEY));

		try {
			super.afterPropertiesSet();
		} catch (Throwable e) {
			logger.warn("Unable to START JMX Server, some serices might be unavailable.");
			logger.warn("Unable to START JMX Server, This is a known issue for jboss 7.0.");
		}
	}

}
