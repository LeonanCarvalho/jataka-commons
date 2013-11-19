package org.jatakasource.common.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

class BaseHibernateUtilities {

	/**
	 * Recursively split list of ids according to limit (default 200) const. <br/>
	 * Use this method to ensure that in() query section will not exceed SQL limits.
	 * 
	 * @param ids
	 *            - Original list of ids
	 * @param inBlock
	 *            - Anonymous class implementation for a single query block
	 */
	@SuppressWarnings("unchecked")
	static <X, ID> List<X> getInRestrictions(List<ID> ids, IInRestrictions inBlock, int limit) {
		List<X> returnList = new ArrayList<X>();
		if (CollectionUtils.isEmpty(ids)) {
			return returnList;
		}

		if (ids.size() > limit) {
			List<ID> nextBlock = new ArrayList<ID>(ids.subList(limit, ids.size()));

			// nextBlock must be removed from the original list before next iteration
			ids.removeAll(nextBlock);

			returnList.addAll(0, (Collection<? extends X>) getInRestrictions(nextBlock, inBlock, limit));
		}

		returnList.addAll(0, (Collection<? extends X>) inBlock.getSingleBlock(ids));

		return returnList;
	}
}
