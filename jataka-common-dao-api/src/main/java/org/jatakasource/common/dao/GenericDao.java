package org.jatakasource.common.dao;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

public interface GenericDao {
	Session getCurrentSession();

	StatelessSession getStatelessSession();
}
