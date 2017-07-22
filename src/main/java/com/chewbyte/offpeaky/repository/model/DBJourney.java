package com.chewbyte.offpeaky.repository.model;

public class DBJourney {

	private String code;
	private String json;
	public DBJourney() {}
	public DBJourney(String code, String json) {
		this.code = code;
		this.json = json;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}
