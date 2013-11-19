package org.jatakasource.common.data.svc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jatakasource.common.data.DataReader;
import org.jatakasource.common.data.dao.DataReaderDao;
import org.jatakasource.common.utils.FileUtils;
import org.jatakasource.common.utils.JarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameMappingStrategy;

@Service
@Transactional
public class ImportCsvServiceImpl implements ImportCsvService {
	private final static Logger logger = Logger.getLogger(ImportCsvServiceImpl.class);

	@Autowired
	private DataReaderDao domainObjectDao;

	/**
	 * Read all files according to class package.
	 */
	@Override
	public <T extends DataReader<?>> void importAll(Class<T> type, Locale locale) {
		List<String> resources = JarUtils.listResources(type, locale, CSV_SUFFIX);

		if (CollectionUtils.isNotEmpty(resources)) {
			for (String resource : resources) {
				List<T> imports = readFile(resource, type);

				domainObjectDao.saveAll(imports);
			}
		} else {
			logger.warn("No reasources found for package " + type.getPackage().getName());
		}
	}

	// Read CSV file and return all data
	private <T> List<T> readFile(String resource, Class<T> type) {
		InputStream stream = null;
		InputStreamReader streamReader = null;
		try {
			stream = type.getClassLoader().getResourceAsStream(resource);
			streamReader = new InputStreamReader(stream, "UTF-8");
			return readFile(streamReader, type);
		} catch (Exception e) {
			logger.error("Error getting resource as stream !!!", e);
		} finally {
			FileUtils.closeSilently(stream);
			FileUtils.closeSilently(streamReader);
		}

		return null;
	}

	/**
	 * Read input stream to List of <T>
	 */
	public <T> List<T> readFile(InputStreamReader streamReader, Class<T> type) {
		CSVReader reader = new CSVReader(streamReader);

		HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<T>();
		strategy.setType(type);

		CsvToBean<T> csv = new CsvToBean<T>();
		return csv.parse(strategy, reader);
	}
}
