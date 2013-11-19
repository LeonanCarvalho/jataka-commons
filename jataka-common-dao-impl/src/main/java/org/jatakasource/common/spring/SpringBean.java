package org.jatakasource.common.spring;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;

public abstract class SpringBean {

	@SuppressWarnings({ "unchecked" })
	public static <T> T getBean(Class<T> type, ApplicationContext context) {
		if (context == null) {
			throw new ApplicationContextException("Application Context not initialize !!!");
		}

		String[] optionalNames = context.getBeanNamesForType(type);

		if (optionalNames == null || optionalNames.length == 0) {
			throw new NoSuchBeanDefinitionException(type.getName());
		}

		if (optionalNames.length > 1) {
			throw new NoSuchBeanDefinitionException(type, "expected single matching bean but found " + optionalNames.length + ": " + optionalNames.toString());
		}

		return (T) context.getBean(optionalNames[0]);
	}

	public static Object getBean(String name, ApplicationContext context) {
		if (context == null) {
			throw new ApplicationContextException("Application Context not initialize !!!");
		}

		return context.getBean(name);
	}

}
