package com.chewbyte.offpeaky.model.response;

import java.util.List;

import com.chewbyte.offpeaky.model.Station;

public class StationCodeResponse {

	private List<Station> stationList;

	public List<Station> getStationList() {
		return stationList;
	}

	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
}