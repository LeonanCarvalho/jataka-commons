package org.jatakasource.common.startup;

import java.io.File;

/**
 * Default Startup properties.
 */
public abstract class CommonStartup {
	public static final String FS = File.separator;

	public static final String DEFAULT_INSTANCE = "default";
	public static final String SYS_CONFIG = "sys.properties";
	public static final String DB_CONFIG = "db.properties";
	public static final String ENV_CONFIG = "environment.properties";

	public static final String JATAKASOURCE = "jatakasource";
	public static final String SERVER = "server";

	public static final String CACHE_SYSTEM_PROPERTY = "ehcache.disk.store.dir";
	public static final String INSTANCE_NAME_KEY = "org.jatakasource.common.instance.name";
	
	public static final String INSTANCE_SYS_CONFIG_KEY = "org.jatakasource.common.instance.sysconfig";
	public static final String INSTANCE_DB_CONFIG_KEY = "org.jatakasource.common.instance.dbconfig";
	public static final String INSTANCE_ENV_CONFIG_KEY = "org.jatakasource.testcube.environment.properties";
}
