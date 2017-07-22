package com.chewbyte.offpeaky.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.repository.GsonFactory;

public class JourneyScraper {

	Logger logger = Logger.getLogger(JourneyScraper.class);

	private Map<String, Journey> finalJourneyList = new TreeMap<String, Journey>();
	private String baseUrl;
	private int threadNumber;

	public JourneyScraper(String startStation, String endStation, String dateTravel) {
		baseUrl = String.format("http://ojp.nationalrail.co.uk/service/timesandfares/%s/%s/%s/_TIME_/dep", startStation,
				endStation, dateTravel);
	}

	public List<Journey> scrape() throws IOException {

		long timeStart = System.currentTimeMillis();

		List<Future<Map<String, Journey>>> handles = new ArrayList<Future<Map<String, Journey>>>();
		Future<Map<String, Journey>> handle;
		int i;
		for (i = 0; i < 72; i++) {
			handle = executorService.submit(new Callable<Map<String, Journey>>() {

				public Map<String, Journey> call() throws Exception {
					logger.info("Thread started: " + Thread.currentThread().getId());
					String timeString = LocalTime.MIN.plus(Duration.ofMinutes(20 * threadNumber++)).toString();
					Map<String, Journey> journeyList = scrapeSet(timeString);
					logger.info("Thread ended: " + Thread.currentThread().getId());
					return journeyList;
				}
			});
			handles.add(handle);
		}

		for (Future<Map<String, Journey>> h : handles) {
			try {
				finalJourneyList.putAll(h.get());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		executorService.shutdownNow();

		logger.info("Scrape time: " + (System.currentTimeMillis() - timeStart));
		logger.info("Found journeys: " + finalJourneyList.size());

		return new ArrayList<Journey>(finalJourneyList.values());
	}

	public Map<String, Journey> scrapeSet(String time) throws IOException {

		Map<String, Journey> journeyList = new TreeMap<String, Journey>();

		String newBaseUrl = baseUrl.replace("_TIME_", time.replace(":", ""));

		Document doc = Jsoup.connect(newBaseUrl).cookie("JSESSIONID", Constants.JSESSIONID).get();
		Elements table = doc.select("#oft");
		
		if(doc.select("#dialog1Title").size() > 0) return null;
		if(table.select(".next-day").size() > 0){
			table.select(".next-day ~ tr").remove();
			table.select(".next-day").remove();
		}
		
		Elements journeyElementList = table.select("[id^=json]");

		int i;
		Element journeyElement;
		for (i = 0; i < journeyElementList.size(); i++) {
			journeyElement = journeyElementList.get(i);
			Journey journey = (Journey) GsonFactory.object(journeyElement.html(), Journey.class);
			String departureTimeString = journey.getJsonJourneyBreakdown().getDepartureTime();
			journeyList.put(departureTimeString, journey);
		}

		logger.info(String.format("Completed scrape for %s", time));

		return journeyList;
	}
}
