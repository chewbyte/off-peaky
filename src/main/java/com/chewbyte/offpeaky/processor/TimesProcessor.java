package com.chewbyte.offpeaky.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.chewbyte.offpeaky.controller.JourneyScraper;
import com.chewbyte.offpeaky.mapper.JourneyMapper;
import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.repository.GsonFactory;
import com.google.gson.Gson;

public class TimesProcessor implements Processor {
	
	public void process(Exchange exchange) throws Exception {
		
		String toStation = (String) exchange.getProperty("toStation");
		String fromStation = (String) exchange.getProperty("fromStation");
		String date = (String) exchange.getProperty("date");
		String ticketType = (String) exchange.getProperty("ticketType");
		
		Map<String, Object> headers = new HashMap<String, Object>();
		headers = exchange.getIn().getHeaders();
		
		toStation = headers.get("start") != null ? (String) headers.get("start") : toStation;
		fromStation = headers.get("end") != null ? (String) headers.get("end") : fromStation;
		date = headers.get("date") != null ? (String) headers.get("date") : date;
		ticketType = headers.get("ticketType") != null ? (String) headers.get("ticketType") : ticketType;
		
		JourneyScraper journeyScraper = new JourneyScraper(toStation, fromStation, date);
		
		List<Journey> journeyList = journeyScraper.scrape();
		
		List<JourneyTime> journeyTimeList = JourneyMapper.map(journeyList);
		
		exchange.getOut().setBody(GsonFactory.json(journeyTimeList));
	}
}
