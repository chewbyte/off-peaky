package com.chewbyte.offpeaky.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.chewbyte.offpeaky.csv.CsvSearch;
import com.chewbyte.offpeaky.model.Station;
import com.chewbyte.offpeaky.model.response.StationCodeResponse;
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
		
		StationCodeResponse response = new StationCodeResponse();
		List<Station> stationList = new ArrayList<Station>();
		for(String[] entry: results) {
			Station station = new Station();
			station.setStationName(entry[0]);
			station.setStationCode(entry[1]);
			stationList.add(station);
		}
		response.setStationList(stationList);
		
		exchange.getOut().setBody(response);
	}
}
