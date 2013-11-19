package org.jatakasource.web.controller;

import java.io.Serializable;
import java.util.List;

import org.jatakasource.common.model.IDomainObject;
import org.jatakasource.web.xml.rendered.XMLListRenderer;
import org.jatakasource.web.xml.rendered.XMLRenderer;
import org.jatakasource.web.xml.rendered.XMLRendererUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.web.servlet.ModelAndView;

public abstract class CRUDControllerBean<T extends IDomainObject<ID>, ID extends Serializable> extends WebControllerBean {
	protected static final String CRUD_PARAMETERS = "CRUD_PARAMETERS";
	
	private Serializer serializer = new Persister();
	/**
	 * Serializing object using simple API
	 */
	protected Serializer getSerializer() {
		return serializer;
	}

	/**
	 * Deserializing object using simple API
	 */
	protected <X extends XMLRenderer<Z>, Z, F extends Z> X read(String xml, Class<X> returnType, Class<F> delegatedType) {
		return XMLRendererUtils.read(xml, returnType, delegatedType, getSerializer());
	}

	/**
	 * Convert list of model objects (originalList Example: List<Language>) to ListRenderer object (returnTypeList
	 * Example: LanguageListRenderer) that include renderer objects (renderedType Example: LanguageRenderer)
	 */
	protected <X extends XMLRenderer<Z>, Y extends XMLListRenderer<X>, Z> Y getAsRenderer(List<Z> originalList, Class<X> renderedType, Class<Z> objectType, Class<Y> returnTypeList) {
		return XMLRendererUtils.getAsRenderer(originalList, renderedType, objectType, returnTypeList);
	}

	/**
	 * Convert model object (object Example: Language) to renderer object (returnType Example: LanguageRenderer)
	 */
	protected <X extends XMLRenderer<Z>, Z> X getAsRenderer(Z object, Class<X> renderedType) {
		return XMLRendererUtils.getAsRenderer(object, renderedType);
	}

	protected abstract ModelAndView delete(ID id);
	
	protected abstract <R extends XMLRenderer<T>, D extends T> ModelAndView update(String xmlCrudParameters, Class<R> renderedType, Class<D> delegatedType);

	protected abstract <R extends XMLRenderer<T>, D extends T> ModelAndView create(String xmlCrudParameters, Class<R> renderedType, Class<D> delegatedType);
}