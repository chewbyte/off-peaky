package com.chewbyte.offpeaky.model;

public class Parameters {

	private String fromStation;
	private String toStation;
	private String date;
	private String ticketType;
	
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
}
