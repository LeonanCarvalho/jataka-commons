package org.jatakasource.common.model.paging;

import org.jatakasource.common.model.paging.Sorting.SortDirection;

public class SortingElement {
	private String name;
	private SortDirection dir;

	public SortingElement(String name) {
		super();
		this.name = name;
		this.dir = SortDirection.ASC;
	}

	public SortingElement(String name, SortDirection dir) {
		super();
		this.name = name;
		this.dir = dir;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SortDirection getDir() {
		return dir;
	}

	public void setDir(SortDirection dir) {
		this.dir = dir;
	}
}