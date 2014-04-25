package org.jatakasource.common.concurrency;

import org.apache.log4j.Logger;

public class ThreadUtils {
	private static final Logger logger = Logger.getLogger(ThreadUtils.class);

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
			logger.error(e);
		}
	}

}
