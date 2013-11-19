package org.jatakasource.common.properties;

import java.util.Locale;

import org.springframework.context.support.AbstractMessageSource;

public class MessageSourceUtils {

	public static String getMessage(AbstractMessageSource messageSource, Class<?> enumClass, String enumName) {

		if (!enumClass.isEnum()) {
			throw new UnsupportedOperationException("Only enum classes are acceptable !!!");
		}

		return getMessage(messageSource, enumClass.getName() + "." + enumName);
	}

	public static String getMessage(AbstractMessageSource messageSource, String code) {
		return messageSource.getMessage(code, new Object[] {}, Locale.ENGLISH);
	}

	public static String getMessage(AbstractMessageSource messageSource, String code, Locale locale) {
		return messageSource.getMessage(code, new Object[] {}, locale);
	}

	public static String getMessage(AbstractMessageSource messageSource, String code, Locale locale, Integer... params) {
		return messageSource.getMessage(code, params, locale);
	}

	public static String getMessage(AbstractMessageSource messageSource, String code, Locale locale, String... params) {
		return messageSource.getMessage(code, params, locale);
	}

	public static String getMessage(AbstractMessageSource messageSource, String code, String defaultMessage, Locale locale) {
		return messageSource.getMessage(code, new Object[] {}, defaultMessage, locale);
	}
}
