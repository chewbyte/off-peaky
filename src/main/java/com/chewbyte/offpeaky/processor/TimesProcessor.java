package com.chewbyte.offpeaky.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.chewbyte.offpeaky.controller.JourneyScraper;
import com.chewbyte.offpeaky.mapper.JourneyMapper;
import com.chewbyte.offpeaky.model.Journey;

public class TimesProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {

		final String startStation = exchange.getIn().getHeader("start", String.class);
		final String endStation = exchange.getIn().getHeader("end", String.class);
		final String dateTravel = exchange.getIn().getHeader("date", String.class);
		final String ticketType = exchange.getIn().getHeader("ticketType", String.class);
		
		JourneyScraper journeyScraper = new JourneyScraper(startStation, endStation, dateTravel);
		
		List<Journey> journeyList = journeyScraper.scrape();
		
		List<JourneyTime> journeyTimeList = JourneyMapper.map(journeyList);
		
		exchange.getOut().setBody(journeyList);
	}
}
