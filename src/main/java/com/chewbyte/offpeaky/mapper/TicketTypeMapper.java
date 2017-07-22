package com.chewbyte.offpeaky.mapper;

import java.util.List;

import com.chewbyte.offpeaky.model.JourneyTime;
import com.chewbyte.offpeaky.model.TicketType;

public class TicketTypeMapper {

	public static String map(List<JourneyTime> journeyTimeList, String ticketType) {
		
		StringBuffer sb = new StringBuffer();

		TicketType type = null;
		if (ticketType.equals("OP")) {
			type = TicketType.OFF_PEAK;
			sb.append("Off-Peak times: ");
		}
		else if (ticketType.equals("SOP")) {
			type = TicketType.SUPER_OFF_PEAK;
			sb.append("Super Off-Peak times: ");
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
					sb.append(jt.getDepartureTime()).append("; ");
				} else if (leftBound) {
					sb.append(jt.getDepartureTime()).append(" => ");
				} else if (rightBound) {
					sb.append(jt.getDepartureTime()).append("; ");
				}
			}
		}

		return sb.toString();
	}
}
