package org.jatakasource.common.svc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.jatakasource.common.dao.BaseDao;
import org.jatakasource.common.model.IDomainObject;
import org.jatakasource.common.model.paging.Paging;
import org.jatakasource.common.model.paging.Sorting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class CRUDServiceImpl<T extends IDomainObject<ID>, ID extends Serializable> implements CRUDService<T, ID> {
	private static final Logger logger = Logger.getLogger(CRUDServiceImpl.class);

	@Autowired
	private CacheManager cacheManager;

	private Class<T> serviceClass;

	public abstract BaseDao<T, ID> getDao();

	@SuppressWarnings("unchecked")
	public CRUDServiceImpl() {
		this.serviceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T add(T pojo) {
		return getDao().save(pojo);
	}

	public T get(ID id) {
		return getDao().getById(id);
	}

	public List<T> getAll() {
		return getDao().findAll();
	}

	public List<T> getAll(Paging paging, Sorting sorting) {
		return getDao().findAll(paging, sorting);
	}
	
	public List<T> getAll(Paging paging, Sorting sorting, String keyword){
		return getDao().findAll(paging, sorting, keyword);
	}

	public T remove(T pojo) {
		getDao().delete(pojo);
		return pojo;
	}

	public T remove(ID id) {
		return remove(get(id));
	}

	public void removeAll(List<T> entities) {
		getDao().deleteAll(entities);
	}
	
	@Override
	public T copy(T pojo) {
		getDao().copy(pojo);
		return pojo;
	}

	@Override
	public T copy(ID id) {
		return copy(get(id));
	}

	public T update(T pojo) {
		return getDao().update(pojo);
	}

	public T saveOrUpdate(T pojo) {
		return getDao().saveOrUpdate(pojo);
	}

	public void addAll(List<T> entities) {
		getDao().saveOrUpdateAll(entities);
	}

	public void addAllStatelessly(List<T> entities) {
		getDao().saveAllStatelessly(entities);
	}

	public List<T> getAllWithQueryCache() {
		return getDao().findAllWithQueryCache();
	}

	public Long getMax(String property) {
		return getDao().getMax(property);
	}

	public void updateAll(List<T> entities) {
		getDao().updateAll(entities);
	}

	private CacheManager getCacheManager() {
		return cacheManager;
	}

	public void clearCache() {
		Cache cache = getCacheManager().getCache(serviceClass.getName());
		if (cache != null) {
			logger.debug("Clearing cache for Cache name: " + cache.getName());
			cache.removeAll();
		} else {
			logger.warn("Unable to find cache for Class: " + serviceClass.getName());
		}
	}

}
