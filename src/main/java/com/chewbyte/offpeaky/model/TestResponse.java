package com.chewbyte.offpeaky.model;

import java.util.List;

public class TestResponse {

	Attributes set_attributes;
	List<Message> messages;
	
	public List<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public Attributes getSet_attributes() {
		return set_attributes;
	}
	
	public void setSet_attributes(Attributes set_attributes) {
		this.set_attributes = set_attributes;
	}
}
