package org.jatakasource.common.svc.properties;

public interface SystemProperties {

	String getProperty(String code);

	String getProperty(String code, String defaultValue);

	Integer getInteger(String code, Integer defaultValue);
}
