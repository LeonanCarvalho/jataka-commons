package org.jatakasource.web.xml.rendered;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;

public class XMLRendererUtils {
	private static Logger logger = Logger.getLogger(XMLRendererUtils.class);

	/**
	 * Convert list of model objects (originalList Example: List<Language>) to ListRenderer object (returnTypeList Example: LanguageListRenderer) that include renderer objects
	 * (returnType Example: LanguageRenderer)
	 */
	public static <X extends XMLRenderer<Z>, Y extends XMLListRenderer<X>, Z, ZID extends Serializable> Y getAsRenderer(List<Z> objectList,
			Class<X> renderedType, Class<Z> objectType, Class<Y> rendredTypeList) {

		Y rendredList = null;
		try {
			rendredList = rendredTypeList.newInstance();
			rendredList.setInnerList(getAsRenderer(objectList, renderedType, objectType));
		} catch (Exception e) {
			logger.error("Unable to create new instance-> " + renderedType.getName(), e);
		}

		return rendredList;
	}

	public static <X extends XMLRenderer<Z>, Z> List<X> getAsRenderer(List<Z> objectList, Class<X> renderedType, Class<Z> objectType) {
		List<X> renderedList = new ArrayList<X>();

		if (objectList != null) {
			X newRendered = null;
			for (Z object : objectList) {
				try {
					newRendered = renderedType.newInstance();
				} catch (Exception e) {
					logger.error("Unable to create new instance-> " + renderedType.getName(), e);
				}
				newRendered.setDelegated(object);
				renderedList.add(newRendered);
			}
		}

		return renderedList;
	}

	/**
	 * Convert model object (domainObject Example: Language) to renderer object (returnType Example: LanguageRenderer)
	 */
	public static <X extends XMLRenderer<Z>, Z> X getAsRenderer(Z domainObject, Class<X> returnType) {
		X newInstance = null;
		try {
			newInstance = returnType.newInstance();
			newInstance.setDelegated(domainObject);
		} catch (Exception e) {
			logger.error("Unable to create new instance-> " + returnType.getName(), e);
		}

		return newInstance;
	}

	public static <X extends XMLRenderer<Z>, Z> List<Z> getAsObjectList(List<X> renderedList, Class<X> renderedType,
			Class<Z> objectType) {
		List<Z> objectList = new ArrayList<Z>();

		for (X rendered : renderedList) {
			objectList.add(rendered.getDelegated());
		}
		return objectList;
	}
	
	/**
	 * Deserializing object using simple API
	 */
	public static <X extends XMLRenderer<Z>, Z, F extends Z> X read(String xml, Class<X> returnType, Class<F> delegatedType, Serializer serializer) {
		X renderer = null;
		F delegated = null;
		try {
			renderer = returnType.newInstance();
			delegated = delegatedType.newInstance();

			renderer.setDelegated(delegated);

			return serializer.read(renderer, xml);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to create new instance-> " + returnType.getName(), e);
			}
			logger.error("Unable to create new instance-> " + returnType.getName() + " " + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Deserializing list using simple API

	 */
	public static <X extends XMLListRenderer<Z>, Z > X readList(String xml, Class<X> returnType, Class<Z> delegatedType,  Serializer serializer) {
		X listrenderer = null;
		List<Z> list = null;
		
		
		try {
			listrenderer = returnType.newInstance();
			list = new ArrayList<Z>();
			listrenderer.setInnerList(list);
			return serializer.read(listrenderer, xml);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to create new instance-> " + returnType.getName(), e);
			}
			logger.error("Unable to create new instance-> " + returnType.getName() + " " + e.getMessage());
		}
		
		return null;
	}
	
	
}
