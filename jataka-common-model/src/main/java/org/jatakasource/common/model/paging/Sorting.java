package org.jatakasource.common.model.paging;

import java.util.ArrayList;
import java.util.List;

public class Sorting {
	private List<SortingElement> sorting;

	public Sorting(SortingElement sortingElement) {
		add(sortingElement);
	}

	public void add(SortingElement sortingElement) {
		if (sorting == null) {
			sorting = new ArrayList<SortingElement>();
		}

		sorting.add(sortingElement);
	}

	public Sorting(List<SortingElement> sorting) {
		this.sorting = sorting;
	}

	public List<SortingElement> getSorting() {
		return sorting;
	}

	public void setSorting(List<SortingElement> sorting) {
		this.sorting = sorting;
	}

	public enum SortDirection {
		ASC, DESC;
	}
}
