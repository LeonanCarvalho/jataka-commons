package org.jatakasource.common.dao;

import java.io.Serializable;
import java.util.List;

import org.jatakasource.common.model.paging.Paging;
import org.jatakasource.common.model.paging.Sorting;

/**
 * @author yaniv-na A gerenic CRUD api for all Daos
 * @param <T>
 *            The persistent Entity
 * @param <ID>
 *            The identifier of the persistent entity
 */
public interface BaseDao<T, ID extends Serializable> extends GenericDao {

	public final static String ORDER_BY = "ORDER BY";
	public final static int DEFAULT_BATCH_SIZE = 20;

	/**
	 * Creates an object in the DB
	 * 
	 * @param entity
	 *            The entity to save
	 * @return the saved entity
	 */
	T save(T entity);

	/**
	 * Updates an entity in the DB
	 * 
	 * @param entity
	 *            the entity to update
	 * @return the updated entity
	 */
	T update(T entity);

	/**
	 * Create a new entity if the given entity has no id or updates if the
	 * entity exists in the DB.
	 * 
	 * @param entity
	 *            the entity to save or update
	 * @return the updated entity
	 */
	T saveOrUpdate(T entity);

	/**
	 * Create or update list of Objects.
	 * 
	 * @param entity
	 *            the entity to save or update
	 * @return the updated entity
	 */
	void saveOrUpdateAll(List<T> entities);

	void updateAll(List<T> entities);

	void saveAll(List<T> entities);

	void saveAllStatelessly(List<T> entities);

	/**
	 * Delete an entity from the DB
	 * 
	 * @param entity
	 *            the entity to delete
	 */
	void delete(T entity);

	/**
	 * Delete an entity from the DB
	 * 
	 * @param id
	 *            the entity id to delete
	 */
	void delete(ID id);

	/**
	 * Copy an entity from the DB
	 * 
	 * @param entity
	 *            the entity to copy
	 */
	void copy(T entity);

	/**
	 * Delete list of entities from the DB
	 * 
	 * @param list
	 *            List to be deleted
	 */
	void deleteAll(List<T> entities);

	void deleteAllStatelessly(List<T> entities);

	/**
	 * Find an entity by an identifier
	 * 
	 * @param id
	 *            the identifier of the entity
	 * @return the entity by the identifier. Null will be returned in case there
	 *         is no such an corresponding entity.
	 */
	T getById(ID id);

	/**
	 * Find all entities in the system of the parameter
	 * 
	 * @return the list of entities
	 */
	List<T> findAll();

	/**
	 * Find all entities in the system of the parameter, According to paging
	 * data, sorted by Sorting
	 * 
	 * @return the list of entities
	 */
	List<T> findAll(Paging paging, Sorting sorting);

	/**
	 * Find all entities in the system of the parameter, According to paging
	 * data, sorted by Sorting
	 * 
	 * @return the list of entities
	 */
	List<T> findAll(Paging paging, Sorting sorting, String keyword);
	
	/**
	 * Find all entities in the system using query cache
	 * 
	 * @return the list of entities
	 */
	List<T> findAllWithQueryCache();

	/**
	 * Find the list of entities by a named query. This named query is defined
	 * in external resource such as XML file of the underlying ORM.
	 * 
	 * @param queryString
	 *            The named query criteria to run with.
	 * @param values
	 *            the optional pamaters for the query.
	 * @return the list of entities applying the given criteria
	 */
	List<T> findByNamedQuery(String queryString, Object... values);

	void executeByNamedQuery(String query, Object... values);

	/**
	 * @return the count of findAll
	 */
	int countAll();

	Long getMax(String property);
}