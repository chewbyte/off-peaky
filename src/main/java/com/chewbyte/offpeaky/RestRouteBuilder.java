
package com.chewbyte.offpeaky;

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

		rest("/test").description("Provider rest service")
			.get()
				.to("direct:test");
		
		from("direct:test")
			.setBody(simple("Hello World!"));
	}

}