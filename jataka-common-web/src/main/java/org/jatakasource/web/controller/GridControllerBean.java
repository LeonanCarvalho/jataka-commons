package org.jatakasource.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jatakasource.common.model.IDomainObject;
import org.jatakasource.common.model.paging.Paging;
import org.jatakasource.common.model.paging.Sorting;
import org.jatakasource.web.exception.XmlDesirializeException;
import org.jatakasource.web.model.GridParameters;
import org.jatakasource.web.xml.rendered.GridParametersRendered;
import org.jatakasource.web.xml.rendered.XMLListRenderer;
import org.jatakasource.web.xml.rendered.XMLRenderer;

/**
 * Handle Grid requests (include sorting and paging)
 */
public abstract class GridControllerBean<T extends IDomainObject<ID>, ID extends Serializable> extends CRUDControllerBean<T, ID> {
	protected static final String GRID_PARAMETERS = "GRID_PARAMETERS";

	protected <G extends GridParametersRendered> G getGridParameters(String xmlGridParameters, Class<G> rendererType) {
		try {
			return getSerializer().read(rendererType, xmlGridParameters);
		} catch (Exception e) {
			throw new XmlDesirializeException(e.getMessage(), e);
		}
	}

	/**
	 * Check if rendered list is larger then requested list. if so, remove last list entry
	 */
	protected <X extends XMLRenderer<Z>, Y extends XMLListRenderer<X>, Z extends IDomainObject<ZID>, ZID extends Serializable> Y getAsRenderer(List<Z> originalList,
			Class<X> renderedType, Class<Z> objectType, Class<Y> renderedTypeList, GridParametersRendered gridParameters) {
		Y renderedlist = super.getAsRenderer(originalList, renderedType, objectType, renderedTypeList);

		renderedlist.setHasNext(isNextPageExists(renderedlist.getInnerList(), gridParameters));

		return renderedlist;
	}

	// Increase limit in 1 to check if next page exists
	private Paging getPaging(GridParameters gridParameters) {
		return new Paging(gridParameters.getStart(), gridParameters.getLimit() + 1);
	}

	private Sorting getSorting(GridParameters gridParameters) {
		return gridParameters.getSortingList();
	}

	protected PagingAndSorting getPagingAndSorting(GridParameters gridParameters) {
		if (gridParameters != null)
			return new PagingAndSorting(getSorting(gridParameters), getPaging(gridParameters));

		return new PagingAndSorting(null, null);
	}

	protected boolean isNextPageExists(List<?> list, GridParameters gridParameters) {
		// In case that the limit is 1, its means that we did not have limit (The limit was zero before we add 1 for
		// checking if next page exists)
		if (list != null && gridParameters != null && gridParameters.getLimit() > 0 && list.size() > gridParameters.getLimit()) {
			// Remove last entity
			list.remove(list.size() - 1);

			return true;
		}

		return false;
	}

	protected <X extends IDomainObject<XID>, XID extends Serializable> List<XID> getIds(List<X> domainObjects) {
		List<XID> ids = new ArrayList<XID>();
		if (CollectionUtils.isNotEmpty(domainObjects)) {
			for (X dom : domainObjects)
				ids.add(dom.getId());
		}

		return ids;
	}

	public class PagingAndSorting {
		private Sorting sorting;
		private Paging paging;

		protected PagingAndSorting(Sorting sorting, Paging paging) {
			this.paging = paging;
			this.sorting = sorting;
		}

		public Sorting getSorting() {
			return sorting;
		}

		public Paging getPaging() {
			return paging;
		}
	}
}
