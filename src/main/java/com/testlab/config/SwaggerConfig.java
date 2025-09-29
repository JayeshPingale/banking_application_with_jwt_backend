package com.testlab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	  @Bean
	  public OpenAPI baseOpenAPI() {
	    return new OpenAPI()
	        .info(new Info()
	            .title("Banking app API")
	            .version("v1.0.0")
	            .description("Banking APplication")
	            .contact(new Contact().name("Your Team").email("team@example.com")))
	        .servers(List.of(
	            new Server().url("http://localhost:8080").description("Local")
	        ));
	  }
}
