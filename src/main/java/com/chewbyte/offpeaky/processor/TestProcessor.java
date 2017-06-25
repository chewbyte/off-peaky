package com.chewbyte.offpeaky.processor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.chewbyte.offpeaky.model.response.ApiResponse;

public class TestProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		
		final String MESSAGE_WAIT = "Please wait while I check that for you.";
 
		LinkedHashMap<String, Object> apiRequest = (LinkedHashMap<String, Object>) exchange.getIn().getBody();
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) apiRequest.get("result");
		LinkedHashMap<String, Object> parameters = (LinkedHashMap<String, Object>) result.get("parameters");
		
		String sessionId = (String) apiRequest.get("sessionId");
		String toStation = (String) parameters.get("toStation");
		String fromStation = (String) parameters.get("fromStation");
		String ticketType = (String) parameters.get("ticketType");
		
		DateTime date = new DateTime(parameters.get("date"));
		DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMMYY");
		String dateFormatted = fmt.print(date);
		
		String debug = String.format("to: %s, from: %s, date: %s, ticketType: %s, sessionId: %s",toStation,fromStation,dateFormatted,ticketType,sessionId);
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setSpeech(debug);
		apiResponse.setDisplayTest(debug);
		apiResponse.setData("");
		apiResponse.setContextOut(new ArrayList<String>());
		apiResponse.setSource("chewbyte.com");
		
		exchange.getOut().setHeader("Content-type", MediaType.APPLICATION_JSON);
		
		exchange.setProperty("sessionId", sessionId);
		exchange.setProperty("toStation", toStation);
		exchange.setProperty("fromStation", fromStation);
		exchange.setProperty("date", dateFormatted);
		exchange.setProperty("ticketType", ticketType);
		
		exchange.getOut().setBody(apiResponse);
	}
}
