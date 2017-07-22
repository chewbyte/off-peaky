package com.chewbyte.offpeaky.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.chewbyte.offpeaky.controller.JourneyScraper;
import com.chewbyte.offpeaky.mapper.JourneyMapper;
import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.repository.GsonFactory;

public class TestProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		
		Map<String, Object> headers = new HashMap<String, Object>();
		headers = exchange.getIn().getHeaders();

		String toStation = (String) headers.get("start");
		String fromStation = (String) headers.get("end");
		String date = (String) headers.get("date");
		String ticketType = (String) headers.get("ticketType");

		JourneyScraper journeyScraper = new JourneyScraper(toStation, fromStation, date);

		List<Journey> journeyList = journeyScraper.scrape();

		List<JourneyTime> journeyTimeList = JourneyMapper.map(journeyList);

		exchange.getOut().setBody(GsonFactory.json(journeyTimeList));
	}
}
