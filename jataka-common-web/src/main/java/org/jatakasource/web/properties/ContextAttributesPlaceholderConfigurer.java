package org.jatakasource.web.properties;

import java.net.MalformedURLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.jatakasource.common.properties.PropertiesUtils;
import org.jatakasource.common.startup.CommonStartup;
import org.jatakasource.common.svc.jmx.JmxCommons;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

@Deprecated
public abstract class ContextAttributesPlaceholderConfigurer extends ContextPlaceholderConfigurer {
	public ContextAttributesPlaceholderConfigurer() {
		super();
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
		// props - properties defined at applicationContext.xml
		super.setProps(props);

		// Check if parameters were passed as -D
		String sysPath = System.getProperty(CommonStartup.INSTANCE_SYS_CONFIG_KEY, null);
		String dbPath = System.getProperty(CommonStartup.INSTANCE_DB_CONFIG_KEY, null);

		// Load properties from instance path location
		if (sysPath == null)
			sysPath = getServerEnvdirFolder() + CommonStartup.FS + getInstanceName() + CommonStartup.FS + CommonStartup.SYS_CONFIG;
		if (dbPath == null)
			dbPath = getServerEnvdirFolder() + CommonStartup.FS + getInstanceName() + CommonStartup.FS + CommonStartup.DB_CONFIG;

		Properties overrideSysProperties = PropertiesUtils.get(sysPath, false);
		Properties overrideDbProperties = PropertiesUtils.get(dbPath, false);

		if (overrideSysProperties != null) {
			props.putAll(overrideSysProperties);
			logger.info("Override system properties form: " + sysPath);
		}

		if (overrideDbProperties != null) {
			props.putAll(overrideDbProperties);
			logger.info("Override db properties form: " + dbPath);
		}

		// Expose org.jatakasource.common.instance.name both to property file
		// and as -D system parameter.
		props.put(CommonStartup.INSTANCE_NAME_KEY, getInstanceName());
		System.getProperties().put(CommonStartup.INSTANCE_NAME_KEY, props.get(CommonStartup.INSTANCE_NAME_KEY));
		logger.info("Webapplication Using instance name: " + props.getProperty(CommonStartup.INSTANCE_NAME_KEY) + " !!!");

		// Try to load org.jatakasource.common.jmx.url from sys.propertis
		String jmxUrl = props.getProperty(JmxCommons.JMX_URL_KEY, StringUtils.EMPTY);
		if (StringUtils.isEmpty(jmxUrl)) {
			// Set jmxUrl to default
			jmxUrl = getServerRundirFolder() + CommonStartup.FS + props.get(CommonStartup.INSTANCE_NAME_KEY);
			props.setProperty(JmxCommons.JMX_URL_KEY, jmxUrl);

			logger.info("Missing property -Dorg.jatakasource.common.jmx.url, setting value to default: " + System.getProperties().get(JmxCommons.JMX_URL_KEY));
		}
		// Expose org.jatakasource.common.jmx.url as -D system parameter
		System.getProperties().put(JmxCommons.JMX_URL_KEY, jmxUrl);
		logger.debug("Expose -Dorg.jatakasource.common.jmx.url=" + System.getProperties().get(JmxCommons.JMX_URL_KEY));

		// Expose ehcache.disk.store.dir to System property
		Object ehcache = get(CommonStartup.CACHE_SYSTEM_PROPERTY);

		if (ehcache != null) {
			System.getProperties().put(CommonStartup.CACHE_SYSTEM_PROPERTY, ehcache.toString());
		}

		super.processProperties(beanFactoryToProcess, props);

		// For some reason, systemProperties is already created before
		// super.processProperties is called,
		// So we have to manually call applyBeanPropertyValues.
		beanFactoryToProcess.applyBeanPropertyValues(beanFactoryToProcess.getBean(getSystemPropertiesBeanName()), getSystemPropertiesBeanName());
	}

	/**
	 * Set the ServletContext to resolve placeholders against. Will be
	 * auto-populated when running in a WebApplicationContext.
	 * <p>
	 * If not set, this configurer will simply not resolve placeholders against
	 * the ServletContext: It will effectively behave like a plain
	 * PropertyPlaceholderConfigurer in such a scenario.
	 */
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	public String getInstanceName() {
		// First try to get context path
		String contextPath = getServletContext().getContextPath();

		// If contextPath is empty or equals development war name, Use
		// DEFAULT_INSTANCE as context path
		if (StringUtils.isEmpty(contextPath) || getDevelopmentWarName().equals(StringUtils.remove(contextPath, "/"))) {
			contextPath = CommonStartup.DEFAULT_INSTANCE;
		}

		// In case of .war deployment, and contextPath return null.
		if (StringUtils.isEmpty(contextPath)) {
			try {
				contextPath = getServletContext().getResource("/").getPath();
				logger.debug("context.getResource(/) returned " + contextPath);
			} catch (MalformedURLException e) {
				logger.error(e);
			}
		}

		String instanceName = StringUtils.EMPTY;

		// Extract instance name from contextPath
		if (StringUtils.isNotEmpty(contextPath)) {
			// Remove last separator character
			contextPath = StringUtils.removeEnd(StringUtils.removeEnd(contextPath, "/"), CommonStartup.FS);

			// Support for non exploded war
			contextPath = contextPath.replace(".war", "");
			instanceName = StringUtils.right(contextPath, contextPath.length() - 1 - contextPath.lastIndexOf("/"));

			// Add support for several instance prefix.
			if (instanceName.split("-").length > 1) {
				instanceName = instanceName.split("-")[1];
			}
		}

		logger.debug("Webapplication Using instance name: " + instanceName + " !!!");

		return instanceName;
	}

	public abstract String getDevelopmentWarName();

	public abstract String getSystemPropertiesBeanName();

	public abstract String getServerEnvdirFolder();

	public abstract String getServerRundirFolder();

}
