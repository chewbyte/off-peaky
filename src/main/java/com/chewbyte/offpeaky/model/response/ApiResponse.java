package com.chewbyte.offpeaky.model.response;

import java.util.List;

import com.chewbyte.offpeaky.model.Context;

public class ApiResponse {
	
	private String speech;
	private String displayTest;
	private String data;
	private List<Context> contextOut;
	private String source;
	
	public String getSpeech() {
		return speech;
	}
	
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	
	public String getDisplayTest() {
		return displayTest;
	}
	
	public void setDisplayTest(String displayTest) {
		this.displayTest = displayTest;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public List<Context> getContextOut() {
		return contextOut;
	}
	
	public void setContextOut(List<Context> contextOut) {
		this.contextOut = contextOut;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
}