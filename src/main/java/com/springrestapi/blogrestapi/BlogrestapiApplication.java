package com.springrestapi.blogrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@SpringBootApplication()
@OpenAPIDefinition (
		info = @Info(
				title="Spring boot blog App REST APIs",
				description="Spring boot blog App REST API description",
				version="v1.0",
				contact=@Contact(
						name="Gina",
						email="test@gmail.com"
						),
				license=@License(
						name="Apache 2.0",
						url="dummy.com")),
		externalDocs=@ExternalDocumentation(
				description="Spring boot blog App REST APIs",
				url="dummy.com")
		)
public class BlogrestapiApplication {
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogrestapiApplication.class, args);
	}
}
