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
		
		String fromStation = (String) exchange.getProperty("fromStation");
		String toStation = (String) exchange.getProperty("toStation");
		String date = (String) exchange.getProperty("date");
		String ticketType = (String) exchange.getProperty("ticketType");
		
		if(fromStation == null) return;
		if(toStation == null) return;
		if(date == null) return;
		if(ticketType == null) return;
		
		JourneyScraper journeyScraper = new JourneyScraper(fromStation, toStation, date);
		
		List<Journey> journeyList = journeyScraper.scrape();
		
		List<JourneyTime> journeyTimeList = JourneyMapper.map(journeyList);
		
		logger.info("Starting persist...");
		
		String code = fromStation + toStation + date;
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
