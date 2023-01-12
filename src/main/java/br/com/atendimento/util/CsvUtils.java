package br.com.atendimento.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvUtils {

	private static final CsvMapper mapper = new CsvMapper();

	public static <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
//		mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
		CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnSeparator(';').withColumnReordering(true).withComments();
		ObjectReader reader = mapper.readerFor(clazz).with(schema);
		return reader.<T>readValues(stream).readAll();
	}
		
	
	/**
	 * @param listOfMap
	 * @param writer
	 * @throws IOException
	 */
	public static void csvWriter(List<HashMap<String, String>> listOfMap, Writer writer) throws IOException {
	    CsvSchema schema = null;
	    CsvSchema.Builder schemaBuilder = CsvSchema.builder();
	    if (listOfMap != null && !listOfMap.isEmpty()) {
	        for (String col : listOfMap.get(0).keySet()) {
	            schemaBuilder.addColumn(col);
	        }
	        schema = schemaBuilder.build().withLineSeparator(System.lineSeparator()).withHeader();
	    }
	    CsvMapper mapper = new CsvMapper();
	    mapper.writer(schema).writeValues(writer).writeAll(listOfMap);
	    writer.flush();
	}
}