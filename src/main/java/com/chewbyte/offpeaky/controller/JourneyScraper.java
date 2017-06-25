package com.chewbyte.offpeaky.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.processor.TimesProcessor;
import com.google.gson.Gson;

public class JourneyScraper {

	Logger logger = Logger.getLogger(JourneyScraper.class);

	private Map<String, Journey> journeyList;
	private String baseUrl;
	private int previousSelection;
	private int timeSelection;
	private Gson gson;

	public JourneyScraper(String startStation, String endStation, String dateTravel) {
		journeyList = new TreeMap<String, Journey>();
		baseUrl = String.format("http://ojp.nationalrail.co.uk/service/timesandfares/%s/%s/%s/_TIME_/dep", startStation,
				endStation, dateTravel);
		previousSelection = -1;
		timeSelection = 0;
		gson = new Gson();
	}

	public List<Journey> scrape() throws IOException {
		
		long timeStart = System.currentTimeMillis();

		while (timeSelection > previousSelection) {
			previousSelection = timeSelection;
			scrapeSet();
		}
		
		logger.info("Request took " + (System.currentTimeMillis() - timeStart));
		
		return new ArrayList<Journey>(journeyList.values());
	}

	public void scrapeSet() throws IOException {

		String newBaseUrl = baseUrl.replace("_TIME_", String.format("%04d", timeSelection));

		Elements doc = Jsoup.connect(newBaseUrl).cookie("JSESSIONID", "F314BF030F0C599248900C5731E62EFA.app208").get().select("#oft");
		Elements journeyElementList = doc.select("[id^=jsonJourney]");

		int i;
		Element journeyElement;
		for (i = 0; i < journeyElementList.size(); i++) {
			journeyElement = journeyElementList.get(i);
			Journey journey = gson.fromJson(journeyElement.html(), Journey.class);
			String departureTimeString = journey.getJsonJourneyBreakdown().getDepartureTime().replace(":", "");
			int departureTime = Integer.parseInt(departureTimeString);
			if (!journeyList.containsKey(departureTimeString) && departureTime >= timeSelection) {
				journeyList.put(departureTimeString, journey);
				timeSelection = departureTime;
			}
		}

		logger.info("Completed scrape for " + timeSelection);
	}
}
