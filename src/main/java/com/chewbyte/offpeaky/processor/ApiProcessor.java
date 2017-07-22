package com.chewbyte.offpeaky.processor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.chewbyte.offpeaky.model.Result;
import com.chewbyte.offpeaky.model.request.ApiRequest;
import com.chewbyte.offpeaky.model.response.ApiResponse;
import com.chewbyte.offpeaky.repository.GsonFactory;
import com.google.gson.Gson;

public class ApiProcessor implements Processor {
	
	Logger log = Logger.getLogger(ApiProcessor.class);

	public void process(Exchange exchange) throws Exception {
		
		final String MESSAGE_WAIT = "Please wait while I check that for you.";
		
		String string = exchange.getIn().getBody(String.class);
		ApiRequest request = (ApiRequest) GsonFactory.object(string, ApiRequest.class);
		
		String toStation = request.getResult().getParameters().getToStation();
		String fromStation = request.getResult().getParameters().getFromStation();
		String ticketType = request.getResult().getParameters().getTicketType();
		
		DateTime date = new DateTime(request.getResult().getParameters().getDate());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMMYY");
		String dateFormatted = fmt.print(date);
		
		String debug = String.format("to: %s, from: %s, date: %s, ticketType: %s",toStation,fromStation,dateFormatted,ticketType);
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setSpeech(debug);
		apiResponse.setDisplayTest(debug);
		apiResponse.setData("");
		apiResponse.setContextOut(new ArrayList<String>());
		apiResponse.setSource("chewbyte.com");
		
		exchange.getOut().setHeader("Content-type", MediaType.APPLICATION_JSON);
		
		exchange.setProperty("toStation", toStation);
		exchange.setProperty("fromStation", fromStation);
		exchange.setProperty("date", dateFormatted);
		exchange.setProperty("ticketType", ticketType);
		
		exchange.getOut().setBody(GsonFactory.json(apiResponse));
	}
}
