package com.chewbyte.offpeaky.processor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.chewbyte.offpeaky.controller.Constants;
import com.chewbyte.offpeaky.mapper.TicketTypeMapper;
import com.chewbyte.offpeaky.model.Context;
import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.model.Parameters;
import com.chewbyte.offpeaky.model.request.ApiRequest;
import com.chewbyte.offpeaky.model.response.ApiResponse;
import com.chewbyte.offpeaky.repository.GsonFactory;
import com.chewbyte.offpeaky.repository.HibernateFactory;
import com.chewbyte.offpeaky.repository.model.DBJourney;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApiProcessor implements Processor {
	
	Logger log = Logger.getLogger(ApiProcessor.class);

	public void process(Exchange exchange) throws Exception {
		
		String string = exchange.getIn().getBody(String.class);
		ApiRequest request = (ApiRequest) GsonFactory.object(string, ApiRequest.class);
		
		String fromStation = request.getResult().getParameters().getFromStation();
		String toStation = request.getResult().getParameters().getToStation();
		String ticketType = request.getResult().getParameters().getTicketType();
		
		System.out.println(fromStation);
		System.out.println(toStation);
		System.out.println(ticketType);
		
		DateTime date = new DateTime(request.getResult().getParameters().getDate());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMMYY");
		String dateFormatted = fmt.print(date);
		
		System.out.println(dateFormatted);
		
		String code = fromStation + toStation + dateFormatted;
		Session session = HibernateFactory.get();
		DBJourney journey = (DBJourney) session.get(DBJourney.class, code);
		session.close();
		
		Parameters parameters = new Parameters();
		parameters.setFromStation(fromStation);
		parameters.setToStation(toStation);
		parameters.setTicketType(ticketType);
		parameters.setDate(dateFormatted);
		
		Context context = new Context();
		context.setLifespan("2");
		context.setName("send_offpeaky_inputs-followup");
		context.setParameters(parameters);
		
		List<Context> contextOut = new ArrayList<Context>();
		contextOut.add(context);
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setData("");
		apiResponse.setContextOut(contextOut);
		apiResponse.setSource("chewbyte.com");
		
		if(journey != null) {
			Type listType = new TypeToken<ArrayList<JourneyTime>>(){}.getType();
			List<JourneyTime> journeyTimeList = new Gson().fromJson(journey.getJson(), listType);
			String returnString = TicketTypeMapper.map(journeyTimeList, ticketType, fromStation);
			apiResponse.setSpeech(returnString);
			apiResponse.setDisplayTest(returnString);
		} else {
			apiResponse.setSpeech(Constants.MESSAGE_WAIT);
			apiResponse.setDisplayTest(Constants.MESSAGE_WAIT);
			
			exchange.setProperty("fromStation", fromStation);
			exchange.setProperty("toStation", toStation);
			exchange.setProperty("date", dateFormatted);
			exchange.setProperty("ticketType", ticketType);
		}
		exchange.getOut().setBody(GsonFactory.json(apiResponse));
	}
}
