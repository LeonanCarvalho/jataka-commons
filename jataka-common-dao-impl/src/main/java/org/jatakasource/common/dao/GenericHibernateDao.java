package org.jatakasource.common.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericHibernateDao implements GenericDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public StatelessSession getStatelessSession() {
		return sessionFactory.openStatelessSession();
	}

}
