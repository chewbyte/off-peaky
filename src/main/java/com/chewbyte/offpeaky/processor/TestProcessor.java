package com.chewbyte.offpeaky.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.chewbyte.offpeaky.model.Attributes;
import com.chewbyte.offpeaky.model.Message;
import com.chewbyte.offpeaky.model.TestResponse;

public class TestProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {

		TestResponse testResponse = new TestResponse();
		
		Message message = new Message();
		message.setText("Hello World!");
		
		List<Message> messageList = new ArrayList<Message>();
		messageList.add(message);
		
		Attributes attributes = new Attributes();
		attributes.setColor("yellow");
		
		testResponse.setSet_attributes(attributes);
		testResponse.setMessages(messageList);
		exchange.getOut().setBody(testResponse);
	}
}
