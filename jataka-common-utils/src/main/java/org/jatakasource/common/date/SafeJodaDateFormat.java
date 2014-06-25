package org.jatakasource.common.date;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeJodaDateFormat {
	private final static Logger logger = LoggerFactory.getLogger(SafeSimpleDateFormat.class);

	private final ThreadLocal<DateTimeFormatter> threadLocal;
	private final String format;

	public SafeJodaDateFormat(final String format) {
		this.format = format;

		threadLocal = new ThreadLocal<DateTimeFormatter>() {
			@Override
			protected DateTimeFormatter initialValue() {
				return DateTimeFormat.forPattern(format);
			}
		};
	}

	public String format(Date date) {
		return threadLocal.get().print(new DateTime(date));
	}

	public Date parse(String str) {
		try {
			return threadLocal.get().parseDateTime(str).toDate();
		} catch (Exception e) {
			logger.error("Fail to parse Time date: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Error", e);
			}
		}

		return null;
	}

	public String format(long time) {
		return threadLocal.get().print(time);
	}

	public DateTimeFormatter get() {
		return threadLocal.get();
	}

	public String getFormat() {
		return format;
	}
}
