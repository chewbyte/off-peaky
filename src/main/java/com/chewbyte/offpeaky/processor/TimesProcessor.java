package com.chewbyte.offpeaky.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chewbyte.offpeaky.model.Journey;
import com.google.gson.Gson;

public class TimesProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {

		final String startStation = exchange.getIn().getHeader("start", String.class);
		final String endStation = exchange.getIn().getHeader("end", String.class);
		final String dateTravel = exchange.getIn().getHeader("date", String.class);
		
		String url = String.format("http://ojp.nationalrail.co.uk/service/timesandfares/%s/%s/%s/0000/dep",
				startStation, endStation, dateTravel);

		Document doc = Jsoup.connect(url).get();
		Elements journeyElementList = doc.select("[id^=jsonJourney]");
		
		Gson gson = new Gson();
		List<Journey> journeyList = new ArrayList<Journey>();
		
		for(Element journeyElement: journeyElementList) {
			
			Journey journey = gson.fromJson(journeyElement.html(), Journey.class);
			journeyList.add(journey);
		}
		
		exchange.getOut().setBody(journeyList);
	}
}
