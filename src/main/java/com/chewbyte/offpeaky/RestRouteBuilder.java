
package com.chewbyte.offpeaky;

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
		
		from("direct:getCode")
			.process("stationCodeProcessor");
		
	}

}