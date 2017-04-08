package com.chewbyte.offpeaky.model.response;

import java.util.List;

import com.chewbyte.offpeaky.model.Message;

public class MessageResponse {

	private List<Message> messages;
	
	public List<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
