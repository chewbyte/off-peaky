
package com.chewbyte.offpeaky.controller;

import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.chewbyte.offpeaky.model.Result;	

/**
 * Define REST services using the Camel REST DSL
 */
public class RestRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		restConfiguration()
			.component("servlet")
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("offpeaky")
			.port(8080);

		rest("/testapi/code/{term}").description("Provider rest service")
			.produces(MediaType.APPLICATION_JSON)
			.get()
			.to("direct:getCode");
		
		rest("/testapi/times/{start}/{end}/{date}/{ticketType}")
			.produces(MediaType.APPLICATION_JSON)
			.get()
			.to("direct:getTimes");
		
		rest("/chatapi")
			.post()
			.to("direct:chatapi");
		
		from("direct:chatapi")
			.onCompletion()
				.process("timesProcessor")
			.end()
			.process("apiProcessor");
		
		from("direct:getCode")
			.process("stationCodeProcessor");
		
		from("direct:getTimes")
			.choice()
				.when(header("ticketType").isNotNull())
					.process("timesProcessor");
	}

}