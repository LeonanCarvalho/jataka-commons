package org.jatakasource.common.properties;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * {@link PropertyPlaceholderConfigurer} subclass that expose resolvePlaceholder
 * as public method and resolve properties from system properties and/or
 * environment variables.
 */
public class SystemPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private Properties props;

	public SystemPlaceholderConfigurer() {
		setSearchSystemEnvironment(false);
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
		this.props = props;

		super.processProperties(beanFactoryToProcess, props);
	}

	public String get(String placeholder) {
		return resolvePlaceholder(placeholder, props);
	}

	public String get(String code, String defaultValue) {
		String value = get(code);
		if (StringUtils.isNotEmpty(value)) {
			return value;
		}

		return defaultValue;
	}

	public Integer getInteger(String code, Integer defaultValue) {
		String value = get(code);
		if (StringUtils.isNumeric(value) && StringUtils.isNotEmpty(value)) {
			return Integer.valueOf(value);
		}

		return defaultValue;
	}
}
