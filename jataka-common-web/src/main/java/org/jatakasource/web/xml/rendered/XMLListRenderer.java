package org.jatakasource.web.xml.rendered;

import java.util.List;

import org.simpleframework.xml.Attribute;

public abstract class XMLListRenderer<T> {
	@Attribute(required = false)
	private boolean hasNext = false;

	public abstract void setInnerList(List<T> innerList);

	public abstract List<T> getInnerList();

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
}
