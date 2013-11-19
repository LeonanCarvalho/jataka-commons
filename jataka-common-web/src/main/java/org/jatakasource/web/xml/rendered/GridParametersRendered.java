package org.jatakasource.web.xml.rendered;

import org.jatakasource.common.model.paging.Sorting;
import org.jatakasource.web.model.GridParameters;
import org.simpleframework.xml.Attribute;

public abstract class GridParametersRendered extends GridParameters {

	public GridParametersRendered() {
		super();
	}

	@Attribute(required = false)
	public int getLimit() {
		return super.getLimit();
	}

	@Attribute(required = false)
	public void setLimit(int limit) {
		super.setLimit(limit);
	}

	@Attribute(required = false)
	public int getStart() {
		return super.getStart();
	}

	@Attribute(required = false)
	public void setStart(int start) {
		super.setStart(start);
	}

	@Attribute(required = false)
	public String getSorting() {
		return super.getSorting();
	}

	@Attribute(required = false)
	public void setSorting(String sorting) {
		super.setSorting(sorting);
	}

	public Sorting getSortingList() {
		return super.getSortingList();
	}
}
