package org.jatakasource.common.data.svc;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.jatakasource.common.data.DataReader;

public interface ImportCsvService {
	public final static String CSV_SUFFIX = "csv";

	<T extends DataReader<?>> void importAll(Class<T> type, Locale locale);

	<T> List<T> readFile(InputStreamReader streamReader, Class<T> type);
}
