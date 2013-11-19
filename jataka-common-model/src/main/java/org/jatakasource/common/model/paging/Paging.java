package org.jatakasource.common.model.paging;

public class Paging {

	private int limit;
	private int start;

	public Paging(int start, int limit) {
		this.setStart(start);
		this.setLimit(limit);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}
}
