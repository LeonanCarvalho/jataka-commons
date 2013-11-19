package org.jatakasource.web.xml.rendered;

public abstract class XMLRendererImpl<T> implements XMLRenderer<T> {
	private T delegated;

	public XMLRendererImpl() {

	}

	public XMLRendererImpl(T delegated) {
		this.delegated = delegated;
	}

	@Override
	public T getDelegated() {
		return delegated;
	}

	@Override
	public void setDelegated(T delegated) {
		this.delegated = delegated;
	}

	/**
	 * Check if submitted value is not null and not zero or -1
	 */
	protected boolean isItemSelected(Long value) {
		return value != null && value != 0L && value != -1L;
	}
}
