package org.jatakasource.web.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jatakasource.common.model.paging.Sorting;
import org.jatakasource.common.model.paging.Sorting.SortDirection;
import org.jatakasource.common.model.paging.SortingElement;

public abstract class GridParameters {
	private static final String SEPARATOR = ";";

	private int start;
	private int limit;
	private List<String> sorting;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getSorting() {
		if (sorting != null && sorting.size() > 0)
			return StringUtils.join(sorting, SEPARATOR);

		return StringUtils.EMPTY;
	}

	public void setSorting(String sorting) {
		if (StringUtils.isNotEmpty(sorting)) {
			this.sorting = Arrays.asList(StringUtils.split(sorting, SEPARATOR));
		}
	}

	public Sorting getSortingList() {
		List<SortingElement> elements = new ArrayList<SortingElement>();
		SortingElement sortElement = null;
		if (sorting != null) {
			for (String sort : sorting) {
				String[] sortParts = sort.split(" ");

				if (sortParts != null && sortParts.length > 0) {
					sortElement = new SortingElement(sortParts[0]);
					if (sortParts.length > 1) {
						sortElement.setDir(SortDirection.valueOf(sortParts[1].toUpperCase()));
					}

					elements.add(sortElement);
				}
			}
		}
		if (elements.size() > 0)
			return new Sorting(elements);

		return null;
	}
}
