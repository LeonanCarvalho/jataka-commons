package org.jatakasource.web.xml.rendered;

public interface XMLRenderer<T> {
	public void setDelegated(T delegated);
	public T getDelegated();
	
	static final String XML_NAME = "NAME";
	static final String XML_DESCRIPTION = "DESCRIPTION"; 
}
