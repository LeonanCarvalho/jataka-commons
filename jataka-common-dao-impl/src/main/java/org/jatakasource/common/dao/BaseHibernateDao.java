package org.jatakasource.common.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.jatakasource.common.model.IDomainObject;
import org.jatakasource.common.model.paging.Paging;
import org.jatakasource.common.model.paging.Sorting;
import org.jatakasource.common.model.paging.Sorting.SortDirection;
import org.jatakasource.common.model.paging.SortingElement;
import org.jatakasource.common.properties.PropertiesUtils;
import org.jatakasource.common.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yaniv-na
 * @param <T>
 *            The persistent entity
 * @param <ID>
 *            The Identifier for the entity
 */
public abstract class BaseHibernateDao<T extends IDomainObject<ID>, ID extends Serializable> extends
		GenericHibernateDao implements BaseDao<T, ID> {
	private static final Logger logger = LoggerFactory.getLogger(BaseHibernateDao.class);
	private static final int IN_LIMIT = 200;

	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public <IT extends T> BaseHibernateDao(Class<IT> concretClass) {
		this.persistentClass = (Class<T>) concretClass;
	}

	protected Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getById(ID id) {
		return (T) getCurrentSession().get(persistentClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getCurrentSession().createCriteria(persistentClass).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Paging paging, Sorting sorting) {
		Criteria criteria = getCurrentSession().createCriteria(persistentClass);
		addPaging(criteria, paging);
		addSorting(criteria, sorting);

		return criteria.list();
	}

	@Override
	public abstract List<T> findAll(Paging paging, Sorting sorting, String keyword);

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllWithQueryCache() {
		Criteria criteria = getCurrentSession().createCriteria(persistentClass);
		criteria.setCacheable(true);
		return criteria.list();
	}

	@Override
	public T saveOrUpdate(T entity) {
		validateUniqueConstraints(entity);

		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public T save(T entity) {
		validateUniqueConstraints(entity);

		getCurrentSession().save(entity);
		return entity;
	}

	@Override
	public T update(T entity) {
		validateUniqueConstraints(entity);

		getCurrentSession().update(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public void delete(ID id) {
		delete(getById(id));
	}

	@Override
	public void deleteAll(List<T> entities) {
		int counter = 0;
		if (entities != null) {
			for (T ent : entities) {

				delete(ent);

				if (counter > 0 && counter % BaseDao.DEFAULT_BATCH_SIZE == 0) {
					// flush a batch of updates and release memory:
					getCurrentSession().flush();
					getCurrentSession().clear();
				}

				counter++;
			}
		}
	}
	
	@Override
	public void copy(T entity) {
		entity.setId(null);
		save(entity);
	}

	@Override
	public void saveOrUpdateAll(List<T> entities) {
		int counter = 0;
		if (entities != null) {
			for (T ent : entities) {

				saveOrUpdate(ent);

				if (counter > 0 && counter % BaseDao.DEFAULT_BATCH_SIZE == 0) {
					// flush a batch of updates and release memory:
					getCurrentSession().flush();
					getCurrentSession().clear();
				}

				counter++;
			}
		}
	}

	@Override
	public void saveAll(List<T> entities) {
		int counter = 0;
		if (entities != null) {
			for (T ent : entities) {

				save(ent);

				if (counter > 0 && counter % BaseDao.DEFAULT_BATCH_SIZE == 0) {
					// flush a batch of updates and release memory:
					getCurrentSession().flush();
					getCurrentSession().clear();
				}

				counter++;
			}
		}
	}

	@Override
	public void updateAll(List<T> entities) {
		int counter = 0;
		if (entities != null) {
			for (T ent : entities) {

				update(ent);

				if (counter > 0 && counter % BaseDao.DEFAULT_BATCH_SIZE == 0) {
					// flush a batch of updates and release memory:
					getCurrentSession().flush();
					getCurrentSession().clear();
				}

				counter++;
			}
		}
	}

	/**
	 * Validate FKS before object removal.
	 * 
	 * @param T
	 *            model
	 */
	protected void validateFks(T model) {

	}

	/**
	 * Validate Uniqueness according to defined list in IPersistenceObject.
	 * 
	 * @param T
	 *            model
	 */
	protected void validateUniqueConstraints(T model) {
		if (ArrayUtils.isNotEmpty(model.uniqueConstraints())) {
			Integer counter = null;
			Object value = null;
			String uniqueKey = null;

			// Flush the model if we are in update mode.
			// This case prevent entity selection when entity is already
			// attached to session.
			if (model.getId() != null) {
				getCurrentSession().evict(model);
			}

			// Add the declared unique properties.
			for (String[] uniqueConstraints : model.uniqueConstraints()) {
				uniqueKey = StringUtils.EMPTY;
				Criteria criteria = getCurrentSession().createCriteria(persistentClass);

				// Support unique constrain from several fields.
				for (String unique : uniqueConstraints) {

					value = ReflectionUtils.getPropertyValue(persistentClass, model, unique);
					criteria.add(Restrictions.eq(unique, value));

					uniqueKey = uniqueKey + "." + unique;
				}

				// In case of update mode, exclude this instance.
				if (model.getId() != null && getLongValue(model.getId(), 0L) != 0L) {
					criteria.add(Restrictions.ne(IDomainObject.FIELD_ID, model.getId()));
				}
				criteria.setProjection(Projections.rowCount());
				counter = (getIntegerValue(criteria.list().get(0))).intValue();

				if (counter > 0) {
					throw new UniqueConstraintsException(persistentClass.getName() + ".UNIQUE"
							+ uniqueKey.toUpperCase(), "Unique Constraint validation failed, property " + uniqueKey);
				}
			}
		}
	}

	@Override
	public void deleteAllStatelessly(List<T> entities) {
		if (entities != null) {
			try {
				for (T entity : entities) {
					getStatelessSession().delete(entity);
				}
			} catch (Exception e) {
				logger.error("Unable to save object Statelessly", e);
			}
		}
	}

	@Override
	public void saveAllStatelessly(List<T> entities) {
		if (entities != null) {
			try {
				for (T entity : entities) {
					getCurrentSession().flush();
					getCurrentSession().clear();

					StatelessSession session = getStatelessSession();
					session.insert(entity);
					session.close();
				}
			} catch (Exception e) {
				logger.error("Unable to save object Statelessly", e);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String namedQueryString, Object... values) {
		return getNamedQuery(namedQueryString, values).list();
	}

	@Override
	public void executeByNamedQuery(String query, Object... values) {
		getNamedQuery(query, values).executeUpdate();
	}

	@Override
	public int countAll() {
		Criteria criteria = getCurrentSession().createCriteria(persistentClass).setProjection(Projections.rowCount());
		return (getIntegerValue(criteria.list().get(0))).intValue();
	}

	private Query getNamedQuery(String namedQueryString, Object... values) {
		Query query = getCurrentSession().getNamedQuery(namedQueryString);

		for (int i = 0; i < values.length; i++)
			query.setParameter(i, values[i]);
		return query;
	}

	@Override
	public Long getMax(String property) {
		Criteria criteria = getCurrentSession().createCriteria(persistentClass);

		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.max(property));
		criteria.setProjection(projList);

		return (Long) criteria.uniqueResult();
	}

	/**
	 * Add paging data to criteria.
	 */
	protected Criteria addPaging(Criteria criteria, Paging paging) {
		if (paging != null) {

			if (paging.getLimit() > 0)
				criteria.setMaxResults(paging.getLimit());
			if (paging.getStart() > 0)
				criteria.setFirstResult(paging.getStart());
		}

		return criteria;
	}

	/**
	 * Add paging data to Query.
	 */
	protected Query addPaging(Query query, Paging paging) {
		if (paging != null) {
			// In case that the limit is 1, its means that we did not have limit
			// (The limit was zero before we add 1 for checking if next page
			// exists)
			if (paging.getLimit() > 1)
				query.setMaxResults(paging.getLimit());
			if (paging.getStart() > 0)
				query.setFirstResult(paging.getStart());
		}

		return query;
	}

	protected Criteria addSorting(Criteria criteria, Sorting sorting) {
		if (sorting == null)
			return criteria;

		List<SortingElement> elements = sorting.getSorting();
		if (elements != null) {
			for (SortingElement element : elements) {
				if (SortDirection.DESC.equals(element.getDir())) {
					criteria.addOrder(Order.desc(element.getName()));
				} else {
					criteria.addOrder(Order.asc(element.getName()));
				}
			}
		}
		return criteria;
	}

	protected StringBuffer addSorting(StringBuffer hqlQuery, Sorting sorting) {
		if (sorting == null)
			return hqlQuery;

		List<SortingElement> elements = sorting.getSorting();
		if (elements != null) {
			hqlQuery.append(" ORDER BY ");
			boolean first = true;
			for (SortingElement element : elements) {
				if (!first) {
					hqlQuery.append(", ");
				}
				hqlQuery.append(element.getName()).append(" ").append(element.getDir());
				first = false;
			}
		}
		return hqlQuery;
	}

	/**
	 * Recursively split list of ids according to IN_LIMIT (200) const. <br/>
	 * Use this method to ensure that in() query section will not exceed SQL
	 * limits.
	 * 
	 * @param ids
	 *            - Original list of ids
	 * @param inBlock
	 *            - Anonymous class implementation for a single query block
	 */
	public <X> List<X> getInRestrictions(List<ID> ids, IInRestrictions inBlock) {
		return BaseHibernateUtilities.getInRestrictions(ids, inBlock, IN_LIMIT);
	}

	protected Double getDoubleValue(Object value, Double defaultValue) {
		return PropertiesUtils.getDoubleValue(value, defaultValue);
	}

	protected Double getDoubleValue(Object value) {
		return PropertiesUtils.getDoubleValue(value);
	}

	protected Integer getIntegerValue(Object value, Integer defaultValue) {
		return PropertiesUtils.getIntegerValue(value, defaultValue);
	}

	protected Integer getIntegerValue(Object value) {
		return PropertiesUtils.getIntegerValue(value);
	}

	protected Long getLongValue(Object value, Long defaultValue) {
		return PropertiesUtils.getLongValue(value, defaultValue);
	}

	protected Long getLongValue(Object value) {
		return PropertiesUtils.getLongValue(value);
	}

	protected Dialect getDialect() {
		return ((SessionFactoryImplementor) getCurrentSession().getSessionFactory()).getDialect();
	}
}
