package org.jatakasource.common.svc.jmx;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

public class Log4jMBean {
	@ManagedOperation(description = "Change lo4j log level to TRACE.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "category", description = "Log4j Catagory name.") })
	public void activateTrace(String category) {
		LogManager.getLogger(category).setLevel(Level.TRACE);
	}
	
	@ManagedOperation(description = "Change lo4j log level to INFO.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "category", description = "Log4j Catagory name.") })
	public void activateInfo(String category) {
		LogManager.getLogger(category).setLevel(Level.INFO);
	}

	@ManagedOperation(description = "Change lo4j log level to DEBUG.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "category", description = "Log4j Catagory name.") })
	public void activateDebug(String category) {
		LogManager.getLogger(category).setLevel(Level.DEBUG);
	}

	@ManagedOperation(description = "Change lo4j log level to WARN.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "category", description = "Log4j Catagory name.") })
	public void activateWarn(String category) {
		LogManager.getLogger(category).setLevel(Level.WARN);
	}

	@ManagedOperation(description = "Change lo4j log level to ERROR.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "category", description = "Log4j Catagory name.") })
	public void activateError(String category) {
		LogManager.getLogger(category).setLevel(Level.ERROR);
	}

	@ManagedOperation(description = "Change lo4j log level to FATAL.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "category", description = "Log4j Catagory name.") })
	public void activateFatal(String category) {
		LogManager.getLogger(category).setLevel(Level.FATAL);
	}
}