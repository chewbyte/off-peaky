package com.chewbyte.offpeaky.model;

import java.util.List;

public class Journey {

	private JsonJourneyBreakdown jsonJourneyBreakdown;
	private List<SingleJsonFareBreakdown> singleJsonFareBreakdowns;

	public JsonJourneyBreakdown getJsonJourneyBreakdown() {
		return jsonJourneyBreakdown;
	}

	public void setJsonJourneyBreakdown(JsonJourneyBreakdown jsonJourneyBreakdown) {
		this.jsonJourneyBreakdown = jsonJourneyBreakdown;
	}

	public List<SingleJsonFareBreakdown> getSingleJsonFareBreakdowns() {
		return singleJsonFareBreakdowns;
	}

	public void setSingleJsonFareBreakdowns(List<SingleJsonFareBreakdown> singleJsonFareBreakdowns) {
		this.singleJsonFareBreakdowns = singleJsonFareBreakdowns;
	}
}
