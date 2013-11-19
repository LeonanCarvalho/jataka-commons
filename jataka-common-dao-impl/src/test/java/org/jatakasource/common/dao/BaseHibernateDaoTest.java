package org.jatakasource.common.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BaseHibernateDaoTest extends TestCase {
	private static final int LIMIT1 = 1001;
	private static final int LIMIT2 = 800;
	
	public void testInRestrictions1() {
		List<Long> ids = new ArrayList<Long>();

		for (long i = 0L; i < LIMIT1; i++) {
			ids.add(i);
		}

		ids = BaseHibernateUtilities.getInRestrictions(ids, new IInRestrictions() {

			@SuppressWarnings("unchecked")
			@Override
			public <X, ID> List<X> getSingleBlock(List<ID> ids) {
				return (List<X>) ids;
			}
		}, 200);

		Assert.assertEquals(ids.size(), LIMIT1);
	}
	
	public void testInRestrictions2() {
		List<Long> ids = new ArrayList<Long>();

		for (long i = 0L; i < LIMIT2; i++) {
			ids.add(i);
		}

		ids = BaseHibernateUtilities.getInRestrictions(ids, new IInRestrictions() {

			@SuppressWarnings("unchecked")
			@Override
			public <X, ID> List<X> getSingleBlock(List<ID> ids) {
				return (List<X>) ids;
			}
		}, 200);

		Assert.assertEquals(ids.size(), LIMIT2);
	}
}
