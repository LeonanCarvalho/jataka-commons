package org.jatakasource.web.properties;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.support.ServletContextPropertyPlaceholderConfigurer;

public class ContextPlaceholderConfigurer extends ServletContextPropertyPlaceholderConfigurer {
	private static final String CACHE_SYSTEM_PROPERTY = "ehcache.disk.store.dir";
	
	private Properties props;
	private ServletContext servletContext;

	public ContextPlaceholderConfigurer() {
		super();
		setSearchSystemEnvironment(true);
		setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
		this.props = props;

		// Expose ehcache.disk.store.dir to System property
		Object ehcache = get(CACHE_SYSTEM_PROPERTY);

		if (ehcache != null) {
			System.getProperties().put(CACHE_SYSTEM_PROPERTY, ehcache.toString());
		}

		super.processProperties(beanFactoryToProcess, props);
	}

	public Properties getProperties() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		super.setServletContext(servletContext);
	}

	public String get(String placeholder) {
		return resolvePlaceholder(placeholder, props);
	}
}
