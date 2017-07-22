package com.chewbyte.offpeaky.processor;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.chewbyte.offpeaky.controller.Constants;
import com.chewbyte.offpeaky.model.request.ApiRequest;
import com.chewbyte.offpeaky.model.response.ApiResponse;
import com.chewbyte.offpeaky.repository.GsonFactory;
import com.chewbyte.offpeaky.repository.HibernateFactory;
import com.chewbyte.offpeaky.repository.model.DBJourney;

public class ApiProcessor implements Processor {
	
	Logger log = Logger.getLogger(ApiProcessor.class);

	public void process(Exchange exchange) throws Exception {
		
		String string = exchange.getIn().getBody(String.class);
		ApiRequest request = (ApiRequest) GsonFactory.object(string, ApiRequest.class);
		
		String startStation = request.getResult().getParameters().getStartStation();
		String endStation = request.getResult().getParameters().getEndStation();
		String ticketType = request.getResult().getParameters().getTicketType();
		
		DateTime date = new DateTime(request.getResult().getParameters().getDate());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMMYY");
		String dateFormatted = fmt.print(date);
		
		String code = startStation + endStation + dateFormatted;
		Session session = HibernateFactory.get();
		DBJourney journey = (DBJourney) session.get(DBJourney.class, code);
		session.close();
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setData("");
		apiResponse.setContextOut(new ArrayList<String>());
		apiResponse.setSource("chewbyte.com");
		
		if(journey != null) {
			apiResponse.setSpeech(journey.getJson());
			apiResponse.setDisplayTest(journey.getJson());
		} else {
			apiResponse.setSpeech(Constants.MESSAGE_WAIT);
			apiResponse.setDisplayTest(Constants.MESSAGE_WAIT);
			
			exchange.setProperty("startStation", startStation);
			exchange.setProperty("endStation", endStation);
			exchange.setProperty("date", dateFormatted);
			exchange.setProperty("ticketType", ticketType);
		}
		exchange.getOut().setBody(GsonFactory.json(apiResponse));
	}
}
