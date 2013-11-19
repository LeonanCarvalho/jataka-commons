package org.jatakasource.common.data.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jatakasource.common.dao.BaseDao;
import org.jatakasource.common.data.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateDataReaderDao implements DataReaderDao {
	@Autowired
	@Qualifier(value = "sessionFactory")
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public <T extends DataReader<?>> int saveAll(List<T> entities) {
		int counter = 0;

		if (entities != null) {
			for (DataReader<?> ent : entities) {

				save(ent.getDelegated());

				if (counter > 0 && counter % BaseDao.DEFAULT_BATCH_SIZE == 0) {
					// flush a batch of updates and release memory:
					getCurrentSession().flush();
					getCurrentSession().clear();
				}

				counter++;
			}
		}

		return counter;
	}

	private Object save(Object entity) {
		getCurrentSession().save(entity);
		return entity;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> dataReader) {
		return getCurrentSession().createCriteria(dataReader).list();
	}
}
