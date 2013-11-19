package org.jatakasource.common.date;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.activation.UnsupportedDataTypeException;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Seconds;

public class DateUtil {
	public static final SafeJodaDateFormat STANDARD_ISO_DATE_FORMAT = new SafeJodaDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public static final SafeSimpleDateFormat DEFAULT_DATE_FORMAT = new SafeSimpleDateFormat("dd-MM-yyyy");
	public static final SafeSimpleDateFormat SHORT_DATE_FORMAT = new SafeSimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final SafeJodaDateFormat DAY_MONTH_DATE_FORMAT = new SafeJodaDateFormat("dd/MM");

	/**
	 * Creates a Days representing the number of whole days between the two
	 * specified datetimes.</BR> This method correctly handles any daylight
	 * savings time changes that may occur during the interval.
	 * 
	 * @param startDate
	 *            the start instant, must not be null
	 * @param endDate
	 *            the end instant, must not be null
	 * @return the period in days
	 */
	public static int wholeDaysDiff(Date startDate, Date endDate) {
		return daysDiff(new DateTime(startDate), new DateTime(endDate));
	}

	private static int daysDiff(DateTime startDate, DateTime endDate) {
		Days d = Days.daysBetween(startDate, endDate);
		return d.getDays();
	}

	/**
	 * Creates a Days representing the number of days difference between the two
	 * specified dates.</BR>
	 * 
	 * @param startDate
	 *            the start instant, must not be null
	 * @param endDate
	 *            the end instant, must not be null
	 * @return the period in days
	 */
	public static int partialDaysDiff(Date startDate, Date endDate) {
		return Days.daysBetween(new DateMidnight(startDate), new DateMidnight(endDate)).getDays();
	}

	private static int secondsDiff(DateTime startDate, DateTime endDate) {
		Seconds s = Seconds.secondsBetween(startDate, endDate);
		return s.getSeconds();
	}

	public static int secondsDiff(Date startTime, Date endTime) {
		return secondsDiff(new DateTime(startTime), new DateTime(endTime));
	}

	public static long millDiff(Date startTime, Date endTime) {
		return endTime.getTime() - startTime.getTime();
	}

	public static Date plusMonths(Date date, int months) {
		return new DateTime(date).plusMonths(months).toDate();
	}

	public static Date plusDays(Date date, int days) {
		return new DateTime(date).plusDays(days).toDate();
	}

	public static Date plusHours(Date date, int hours) {
		return new DateTime(date).plusHours(hours).toDate();
	}

	public static Date plusMinutes(Date date, int minutes) {
		return new DateTime(date).plusMinutes(minutes).toDate();
	}

	public static Date plusSeconds(Date date, int seconds) {
		return new DateTime(date).plusSeconds(seconds).toDate();
	}

	public static Date plusMillis(Date date, int millis) {
		return new DateTime(date).plusMillis(millis).toDate();
	}

	public static Date minusMonths(Date date, int months) {
		return new DateTime(date).minusMonths(months).toDate();
	}

	public static Date minusDays(Date date, int days) {
		return new DateTime(date).minusDays(days).toDate();
	}

	public static Date minusHours(Date date, int hours) {
		return new DateTime(date).minusHours(hours).toDate();
	}

	public static Date minusMinutes(Date date, int minutes) {
		return new DateTime(date).minusMinutes(minutes).toDate();
	}

	public static Date minusSeconds(Date date, int seconds) {
		return new DateTime(date).minusSeconds(seconds).toDate();
	}

	public static Date minusMillis(Date date, int millis) {
		return new DateTime(date).minusMillis(millis).toDate();
	}

	public static Date plus(Date date, int interval, TimeUnit unit) {
		switch (unit) {
		case DAYS:
			return plusDays(date, interval);
		case HOURS:
			return plusHours(date, interval);
		case MINUTES:
			return plusMinutes(date, interval);
		case SECONDS:
			return plusSeconds(date, interval);
		case MILLISECONDS:
			return plusMillis(date, interval);
		}

		return date;
	}

	public static Date minus(Date date, int interval, TimeUnit unit) {
		switch (unit) {
		case DAYS:
			return minusDays(date, interval);
		case HOURS:
			return minusHours(date, interval);
		case MINUTES:
			return minusMinutes(date, interval);
		case SECONDS:
			return minusSeconds(date, interval);
		case MILLISECONDS:
			return minusMillis(date, interval);
		}

		return date;
	}

	public static int convertToSecond(int interval, TimeUnit unit) throws UnsupportedDataTypeException {
		switch (unit) {
		case DAYS:
			return interval * DateTimeConstants.SECONDS_PER_DAY;
		case HOURS:
			return interval * DateTimeConstants.SECONDS_PER_HOUR;
		case MINUTES:
			return interval * DateTimeConstants.SECONDS_PER_MINUTE;
		case SECONDS:
			return interval;
		case MILLISECONDS:
			return interval / DateTimeConstants.MILLIS_PER_SECOND;
		}

		throw new UnsupportedDataTypeException();
	}

	public static long getMilliseconds(int interval, TimeUnit unit) {

		long millisPerUnit = 1;

		switch (unit) {
		case DAYS:
			millisPerUnit = DateTimeConstants.MILLIS_PER_DAY;
			break;
		case HOURS:
			millisPerUnit = DateTimeConstants.MILLIS_PER_HOUR;
			break;
		case MINUTES:
			millisPerUnit = DateTimeConstants.MILLIS_PER_MINUTE;
			break;
		case SECONDS:
			millisPerUnit = DateTimeConstants.MILLIS_PER_SECOND;
			break;
		}

		return interval * millisPerUnit;
	}

	public static boolean isAfterNow(Date date) {
		return new DateTime(date).isAfterNow();
	}

	public static boolean isBeforeNow(Date date) {
		return new DateTime(date).isBeforeNow();
	}

}
