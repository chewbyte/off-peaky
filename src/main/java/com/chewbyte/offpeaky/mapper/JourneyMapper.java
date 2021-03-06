package com.chewbyte.offpeaky.mapper;

import java.util.ArrayList;
import java.util.List;

import com.chewbyte.offpeaky.model.Journey;
import com.chewbyte.offpeaky.model.TicketType;
import com.chewbyte.offpeaky.model.JourneyTime;

public class JourneyMapper {

	public static List<JourneyTime> map(List<Journey> journeyList) {

		List<JourneyTime> journeyTimeList = new ArrayList<JourneyTime>();

		for (Journey journey : journeyList) {

			JourneyTime journeyTime = new JourneyTime();
			journeyTime.setDepartureTime(journey.getJsonJourneyBreakdown().getDepartureTime());
			String journeyTicketType = journey.getSingleJsonFareBreakdowns().get(0).getTicketType();

			if (journeyTicketType.startsWith("Anytime")) {
				journeyTime.setTicketType(TicketType.ANYTIME);
			} else if (journeyTicketType.startsWith("Off-Peak")) {
				journeyTime.setTicketType(TicketType.OFF_PEAK);
			} else if (journeyTicketType.startsWith("Super Off-Peak")) {
				journeyTime.setTicketType(TicketType.SUPER_OFF_PEAK);
			} else
				System.out.println("We found a new thing: " + journeyTicketType);

			journeyTimeList.add(journeyTime);
		}
		return journeyTimeList;
	}
}
