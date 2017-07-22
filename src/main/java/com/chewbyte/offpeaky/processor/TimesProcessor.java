package com.chewbyte.offpeaky.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.chewbyte.offpeaky.controller.JourneyScraper;
import com.chewbyte.offpeaky.mapper.JourneyMapper;
import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.repository.GsonFactory;
import com.chewbyte.offpeaky.repository.HibernateFactory;
import com.chewbyte.offpeaky.repository.model.DBJourney;

public class TimesProcessor implements Processor {
	
	Logger logger = Logger.getLogger(TimesProcessor.class);
	
	public void process(Exchange exchange) throws Exception {
		
		String startStation = (String) exchange.getProperty("startStation");
		String endStation = (String) exchange.getProperty("endStation");
		String date = (String) exchange.getProperty("date");
		String ticketType = (String) exchange.getProperty("ticketType");
		
		if(startStation.isEmpty()) return;
		if(endStation.isEmpty()) return;
		if(date.isEmpty()) return;
		if(ticketType.isEmpty()) return;
		
		JourneyScraper journeyScraper = new JourneyScraper(startStation, endStation, date);
		
		List<Journey> journeyList = journeyScraper.scrape();
		
		List<JourneyTime> journeyTimeList = JourneyMapper.map(journeyList);
		
		logger.info("Starting persist...");
		
		String code = startStation + endStation + date;
		DBJourney toSave = new DBJourney(code, GsonFactory.json(journeyTimeList));
		
		Session session = HibernateFactory.get();
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    session.saveOrUpdate(toSave);
		    tx.commit();
		} catch(Exception e) {
		    tx.rollback();
		}
		
		logger.info("Finished persist.");
	}
}
