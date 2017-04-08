package com.chewbyte.offpeaky.processor;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.chewbyte.offpeaky.csv.CsvSearch;
import com.google.gson.Gson;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class StationCodeProcessor implements Processor{
	
	private static String STATION_CODE_FILE = "station_codes.csv";

	public void process(Exchange exchange) throws Exception {
		
		String searchterm = exchange.getIn().getHeader("term", String.class);
		
		CsvParserSettings settings = new CsvParserSettings();
		settings.setHeaderExtractionEnabled(true);

		CsvSearch search = new CsvSearch("Station Name", searchterm);

		//We instruct the parser to send all rows parsed to Row Processor
		settings.setProcessor(search);

		//Finally, we create a parser
		CsvParser parser = new CsvParser(settings);

		//Do the parse
		ClassLoader classLoader = getClass().getClassLoader();
		parser.parse(new File(classLoader.getResource(STATION_CODE_FILE).getFile()));

		//Get collected rows
		List<String[]> results = search.getRows();
		
		Gson gson = new Gson();
		exchange.getOut().setBody(gson.toJson(results));
		exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}
}
