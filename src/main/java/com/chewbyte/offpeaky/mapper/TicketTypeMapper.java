package com.chewbyte.offpeaky.mapper;

import java.util.List;

import com.chewbyte.offpeaky.controller.StationCodeToName;
import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.model.TicketType;

public class TicketTypeMapper {

	public static String map(List<JourneyTime> journeyTimeList, String ticketType, String fromStation) {
		
		StringBuffer sb = new StringBuffer();
		String stationName = (new StationCodeToName()).codeToName(fromStation);

		TicketType type = null;
		if (ticketType.equals("OP")) {
			type = TicketType.OFF_PEAK;
			sb.append("You can travel on any of the following Off-Peak trains departing from ").append(stationName).append(":");
		}
		else if (ticketType.equals("SOP")) {
			type = TicketType.SUPER_OFF_PEAK;
			sb.append("You can travel on any of the following Super Off-Peak trains departing from ").append(stationName).append(":");
		}
		if (type == null) return "Invalid ticket type code.";

		if (type == TicketType.OFF_PEAK) {
			for (JourneyTime jt : journeyTimeList) {
				if (jt.getTicketType() == TicketType.SUPER_OFF_PEAK) {
					jt.setTicketType(TicketType.OFF_PEAK);
				}
			}
		}
		
		int currentIndex;
		for (JourneyTime jt : journeyTimeList) {
			currentIndex = journeyTimeList.indexOf(jt);

			if (jt.getTicketType() == type) {

				boolean leftBound = false;
				boolean rightBound = false;

				if (currentIndex > 0) {
					if (journeyTimeList.get(currentIndex - 1).getTicketType() != type) {
						leftBound = true;
					}
				} else {
					leftBound = true;
				}

				if (currentIndex < journeyTimeList.size() - 1) {
					if (journeyTimeList.get(currentIndex + 1).getTicketType() != type) {
						rightBound = true;
					}
				} else {
					rightBound = true;
				}

				if (leftBound && rightBound) {
					sb.append("\n").append(jt.getDepartureTime()).append(",");
				} else if (leftBound) {
					sb.append("\nbetween ").append(jt.getDepartureTime()).append(" and ");
				} else if (rightBound) {
					if(currentIndex == journeyTimeList.size() - 1) {
						sb.append(jt.getDepartureTime()).append(".");
					} else {
						sb.append(jt.getDepartureTime()).append(",");
					}
				}
			}
		}

		return sb.toString();
	}
}
