package com.chewbyte.offpeaky.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hibernate.Session;

import com.chewbyte.offpeaky.controller.JourneyScraper;
import com.chewbyte.offpeaky.mapper.JourneyMapper;
import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.repository.GsonFactory;
import com.chewbyte.offpeaky.repository.HibernateFactory;
import com.chewbyte.offpeaky.repository.model.DBJourney;

public class TimesProcessor implements Processor {
	
	public void process(Exchange exchange) throws Exception {
		
		String toStation = (String) exchange.getProperty("toStation");
		String fromStation = (String) exchange.getProperty("fromStation");
		String date = (String) exchange.getProperty("date");
		String ticketType = (String) exchange.getProperty("ticketType");
		
		if(toStation.isEmpty()) return;
		if(fromStation.isEmpty()) return;
		if(date.isEmpty()) return;
		if(ticketType.isEmpty()) return;
		
		Map<String, Object> headers = new HashMap<String, Object>();
		headers = exchange.getIn().getHeaders();
		
		toStation = headers.get("start") != null ? (String) headers.get("start") : toStation;
		fromStation = headers.get("end") != null ? (String) headers.get("end") : fromStation;
		date = headers.get("date") != null ? (String) headers.get("date") : date;
		ticketType = headers.get("ticketType") != null ? (String) headers.get("ticketType") : ticketType;
		
		JourneyScraper journeyScraper = new JourneyScraper(toStation, fromStation, date);
		
		List<Journey> journeyList = journeyScraper.scrape();
		
		List<JourneyTime> journeyTimeList = JourneyMapper.map(journeyList);
		
		String code = toStation + fromStation + date;
		Session session = HibernateFactory.get();
		session.save(new DBJourney(code, GsonFactory.json(journeyTimeList)));
		session.close();
	}
}
