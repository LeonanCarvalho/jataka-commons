package com.jatakasource.common.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.jatakasource.common.date.DateUtil;

public class DateUtilTest extends TestCase {
	private static Logger logger = Logger.getLogger(DateUtilTest.class);

	public void testGetMilliseconds() {
		assertEquals(1, DateUtil.getMilliseconds(1, TimeUnit.MILLISECONDS));
		assertEquals(1000, DateUtil.getMilliseconds(1, TimeUnit.SECONDS));
		assertEquals(60 * 1000, DateUtil.getMilliseconds(1, TimeUnit.MINUTES));
		assertEquals(60 * 60 * 1000, DateUtil.getMilliseconds(1, TimeUnit.HOURS));
		assertEquals(24 * 60 * 60 * 1000, DateUtil.getMilliseconds(1, TimeUnit.DAYS));
	}

	public void testWholeDaysDiff() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = df.parse("2012-11-25 01:00:00");
			date2 = df.parse("2012-11-26 00:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(0, DateUtil.wholeDaysDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-26 02:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(1, DateUtil.wholeDaysDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-27 00:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(1, DateUtil.wholeDaysDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-27 02:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(2, DateUtil.wholeDaysDiff(date1, date2));

	}

	public void testPartialDaysDiff() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = df.parse("2012-11-25 01:00:00");
			date2 = df.parse("2012-11-26 00:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(1, DateUtil.partialDaysDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-26 02:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(1, DateUtil.partialDaysDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-27 00:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(2, DateUtil.partialDaysDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-27 02:00:59");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(2, DateUtil.partialDaysDiff(date1, date2));

	}

	public void testSecondsDiff() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = df.parse("2012-11-26 00:00:00 0");
			date2 = df.parse("2012-11-26 00:00:30 0");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(30, DateUtil.secondsDiff(date1, date2));

		try {
			date2 = df.parse("2012-11-25 23:59:30 0");
		} catch (Exception e) {
			logger.error(e);
		}
		assertEquals(-30, DateUtil.secondsDiff(date1, date2));
	}

	public void testExternalAPIFormat() {
		try {
			Date date = DateUtil.STANDARD_ISO_DATE_FORMAT.parse("2011-02-22T16:45:43.3473839+02:00");
			date.toString();
		} catch (Exception e) {
			logger.error(e);
		}

		assertTrue(true);
	}

}
