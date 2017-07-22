package com.chewbyte.offpeaky.controller;

import java.io.File;
import java.util.List;

import com.chewbyte.offpeaky.csv.CsvSearch;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class StationCodeToName {
	
	public String codeToName(String code) {
		
		CsvParserSettings settings = new CsvParserSettings();
		settings.setHeaderExtractionEnabled(true);
		CsvSearch search = new CsvSearch("CRS Code", code);
		settings.setProcessor(search);
		CsvParser parser = new CsvParser(settings);
		ClassLoader classLoader = getClass().getClassLoader();
		parser.parse(new File(classLoader.getResource(Constants.STATION_CODE_FILE).getFile()));
		List<String[]> results = search.getRows();
		return results.get(0)[0];
	}
}
