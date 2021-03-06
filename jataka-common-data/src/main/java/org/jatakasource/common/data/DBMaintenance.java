package org.jatakasource.common.data;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.jatakasource.common.data.svc.InitializeDatabaseService;
import org.jatakasource.common.spring.SpringBean;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBMaintenance {
	private static Logger logger = LoggerFactory.getLogger(DBMaintenance.class);

	public static void main(String[] args) {
		// Create args object
		DBMaintenanceArgs dbArgs = new DBMaintenanceArgs();

		CmdLineParser parser = new CmdLineParser(dbArgs);
		try {
			// Parsing command line arguments
			parser.parseArgument(args);

			// Validatation
			if (Action.INITIALIZE.name().equals(dbArgs.action.toUpperCase()) && StringUtils.isEmpty(dbArgs.locale)) {
				logger.error("Usage: Missing locale (en_US|iw_IL|...)");
				System.exit(1);
			}
		} catch (CmdLineException e) {
			logger.error(e.getMessage(), e);
			logger.debug("Usage: Missing action (create|initialize|upgrade|reset)");
			System.exit(1);
		}

		logger.info("START DBMaintenance ..." + dbArgs.action);

		// Initialize spring context to create DB schema server
		ClassPathXmlApplicationContext applicationContext = null;

		switch (Action.valueOf(dbArgs.action.toUpperCase())) {

		case CREATE:
			applicationContext = createApplicationContext();
			break;
		case INITIALIZE:
			applicationContext = inititializeApplicationContext();
			InitializeDatabaseService service = SpringBean.getBean(InitializeDatabaseService.class, applicationContext);
			service.initialize(LocaleUtils.toLocale(dbArgs.locale));
			break;
		default:
			break;
		}
		
		applicationContext.close();
		
		try {
			// Wait for Asynchronous tasks to complete
			// e.g - hsqldb persistence is not synchronized 
			Thread.sleep(500);
		} catch (InterruptedException e) {
			logger.error("Error while tring to sleep.", e);
		}
		
		System.exit(0);
	}

	public static ClassPathXmlApplicationContext createApplicationContext() {
		String[] contextFiles = new String[] { "db-maintenance-context.xml" };
		return new ClassPathXmlApplicationContext(contextFiles);
	}

	public static ClassPathXmlApplicationContext inititializeApplicationContext() {
		String[] contextFiles = new String[] { "common-data-context.xml", "db-maintenance-context.xml" };
		ClassPathXmlApplicationContext context = null;

		try {
			context = new ClassPathXmlApplicationContext(contextFiles);
		} catch (Throwable e) {
			logger.error("Error Loading application context !!!", e);
		}

		return context;
	}

	public static class DBMaintenanceArgs {
		@Argument(required = true, usage = "create|initialize|upgrade|reset", metaVar = "action", index = 0)
		private String action;
		@Argument(required = false, usage = "en_US|he_IL", metaVar = "locale", index = 1)
		private String locale;
	}

	private enum Action {
		CREATE, INITIALIZE, UPGRADE, RESET;
	}
}
