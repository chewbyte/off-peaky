package com.chewbyte.offpeaky.model;

public class SingleJsonFareBreakdown {

	private String ticketType;
	private String ticketTypeCode;
	private String fareId;
	
	public String getTicketType() {
		return ticketType;
	}
	
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	
	public String getTicketTypeCode() {
		return ticketTypeCode;
	}
	
	public void setTicketTypeCode(String ticketTypeCode) {
		this.ticketTypeCode = ticketTypeCode;
	}
	
	public String getFareId() {
		return fareId;
	}
	
	public void setFareId(String fareId) {
		this.fareId = fareId;
	}
}
