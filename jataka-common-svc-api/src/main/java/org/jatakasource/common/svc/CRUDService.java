package org.jatakasource.common.svc;

import java.io.Serializable;
import java.util.List;

import org.jatakasource.common.model.paging.Paging;
import org.jatakasource.common.model.paging.Sorting;

public interface CRUDService<T, ID extends Serializable> {

	T add(T pojo);

	void addAll(List<T> entities);

	void addAllStatelessly(List<T> entities);

	T update(T pojo);

	T saveOrUpdate(T pojo);

	void updateAll(List<T> entities);

	T remove(T pojo);

	T remove(ID id);
	
	T copy(T pojo);

	T copy(ID id);

	void removeAll(List<T> entities);

	T get(ID id);

	List<T> getAll();

	List<T> getAll(Paging paging, Sorting sorting);

	List<T> getAll(Paging paging, Sorting sorting, String keyword);
	
	List<T> getAllWithQueryCache();

	Long getMax(String property);
}
