package org.jatakasource.common.data.dao;

import java.util.List;

import org.jatakasource.common.data.DataReader;

/**
 * Generic DAO which accept any data-model class.
 */
public interface DataReaderDao {
	<T extends DataReader<?>> int saveAll(List<T> entities);

	<T> List<T> getAll(Class<T> dataReader);
}
