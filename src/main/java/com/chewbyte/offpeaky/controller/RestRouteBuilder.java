
package com.chewbyte.offpeaky.controller;

import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Define REST services using the Camel REST DSL
 */
public class RestRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint", "true").contextPath("offpeaky")
				.port(8080);

		rest("/code/{term}").description("Provider rest service")
			.produces(MediaType.APPLICATION_JSON)
			.get()
			.to("direct:getCode");
		
		rest("/times/{start}/{end}/{date}")
			.produces(MediaType.APPLICATION_JSON)
			.get()
			.to("direct:getTimes");
		
		rest("/test")
			.produces(MediaType.APPLICATION_JSON)
			.get()
			.to("direct:test");
		
		from("direct:getCode")
			.process("stationCodeProcessor");
		
		from("direct:getTimes")
			.process("timesProcessor");
		
		from("direct:test")
			.process("testProcessor");
	}

}