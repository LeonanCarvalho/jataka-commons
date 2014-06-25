package org.jatakasource.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeSimpleDateFormat {
	private final static Logger logger = LoggerFactory.getLogger(SafeSimpleDateFormat.class);

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
				logger.error("Error", e);
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
