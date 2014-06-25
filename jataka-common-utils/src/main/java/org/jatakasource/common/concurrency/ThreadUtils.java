package org.jatakasource.common.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils {
	private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

	public static void start(Runnable executer, String threadName, boolean demon) {
		Thread thread = new Thread(executer, threadName);
		thread.setDaemon(demon);
		thread.start();
	}

	@SuppressWarnings("static-access")
	public static void sleepSilently(long mil) {
		try {
			Thread.currentThread().sleep(mil);
		} catch (InterruptedException e) {
			logger.error("Error", e);
		}
	}

}
