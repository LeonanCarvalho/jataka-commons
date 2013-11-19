package org.jatakasource.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class ListUtils {

	/**
	 * <p>
	 * Convert string comma separator string to list of Strings.
	 * </p>
	 * 
	 * Supported formats:</br> [X, Y, Z]</br> X, Y, Z
	 */
	public static List<String> toList(String arrayString) {
		if (StringUtils.isNotEmpty(arrayString)) {
			arrayString = StringUtils.removeStart(arrayString, "[");
			arrayString = StringUtils.removeEnd(arrayString, "]");

			String[] tokens = arrayString.split(",");

			if (tokens != null && tokens.length > 0) {
				return Arrays.asList(tokens);
			}
		}

		return null;
	}

	/**
	 * <p>
	 * Convert string comma separator string to list of Longs.
	 * </p>
	 * 
	 * Supported formats:</br> [X, Y, Z]</br> X, Y, Z
	 */
	public static List<Long> toLongs(String arrayString) {
		List<Long> numbers = new ArrayList<Long>();

		if (StringUtils.isNotEmpty(arrayString)) {
			arrayString = StringUtils.removeStart(arrayString, "[");
			arrayString = StringUtils.removeEnd(arrayString, "]");

			String[] tokens = arrayString.split(",");

			if (!ArrayUtils.isEmpty(tokens)) {
				for (String number : tokens) {
					numbers.add(new Long(number));
				}
			}
		}

		return numbers;
	}

	/**
	 * <p>
	 * Convert string comma separator string to list of enums.
	 * </p>
	 * 
	 * Supported formats:</br> [X, Y, Z]</br> X, Y, Z
	 */
	public static <T> List<T> toList(String arrayString, Class<T> enumType) {
		if (!enumType.isEnum()) {
			throw new UnsupportedOperationException("This method Support only enum Convention !!!");
		}

		List<String> enumAsStrings = ListUtils.toList(arrayString);
		List<T> enumList = new ArrayList<T>();

		T[] enums = enumType.getEnumConstants();

		if (CollectionUtils.isNotEmpty(enumAsStrings)) {
			for (String name : enumAsStrings) {
				for (T value : enums) {
					if (value.toString().equals(name)) {
						enumList.add(value);
					}
				}
			}
		}

		return enumList;
	}
}
