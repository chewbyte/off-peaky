package com.chewbyte.offpeaky.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chewbyte.offpeaky.model.Journey;
import com.google.gson.Gson;

public class JourneyScraper {

	private Map<String,Journey> journeyList;
	private String baseUrl;
	private int previousSelection;
	private int timeSelection;
	private Gson gson;
	
	public JourneyScraper(String startStation, String endStation, String dateTravel) {
		journeyList = new TreeMap<String,Journey>();
		baseUrl =  String.format("http://ojp.nationalrail.co.uk/service/timesandfares/%s/%s/%s/_TIME_/dep", startStation, endStation, dateTravel);
		previousSelection = -1;
		timeSelection = 0;
		gson = new Gson();
	}
	
	public List<Journey> scrape() throws IOException {
		
		while(timeSelection > previousSelection) {
			previousSelection = timeSelection;
			scrapeSet();
		}
		return new ArrayList<Journey>(journeyList.values());
	}

	public void scrapeSet() throws IOException{
		
		String newBaseUrl = baseUrl.replace("_TIME_", String.format("%04d", timeSelection));

		Document doc = Jsoup.connect(newBaseUrl).get();
		Elements journeyElementList = doc.select("[id^=jsonJourney]");

		for(Element journeyElement:journeyElementList) {
			Journey journey = gson.fromJson(journeyElement.html(), Journey.class);
			String departureTimeString = journey.getJsonJourneyBreakdown().getDepartureTime().replace(":", "");
			int departureTime = Integer.parseInt(departureTimeString);
			if(!journeyList.containsKey(departureTimeString) && departureTime >= timeSelection) {
				journeyList.put(departureTimeString, journey);
				timeSelection = departureTime;
			}
		}
	}
}
