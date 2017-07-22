package com.chewbyte.offpeaky.model;

import com.chewbyte.offpeaky.model.TicketType;

public class JourneyTime {

	private String departureTime;
	private TicketType ticketType;
	
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public TicketType getTicketType() {
		return ticketType;
	}
	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}
}
