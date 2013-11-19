package org.jatakasource.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class SafeSimpleDateFormat {
	private final static Logger logger = Logger.getLogger(SafeSimpleDateFormat.class);

	private final ThreadLocal<SimpleDateFormat> threadLocal;

	public SafeSimpleDateFormat(final String format) {
		threadLocal = new ThreadLocal<SimpleDateFormat>() {
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat(format);
			}
		};
	}

	public String format(Date date) {
		return threadLocal.get().format(date);
	}

	public Date parse(String str) {
		try {
			return threadLocal.get().parse(str);
		} catch (ParseException e) {
			logger.error("Fail to parse Time date: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error(e);
			}
		}

		return null;
	}

	public String format(long time) {
		return threadLocal.get().format(time);
	}

	public SimpleDateFormat get() {
		return threadLocal.get();
	}
}
