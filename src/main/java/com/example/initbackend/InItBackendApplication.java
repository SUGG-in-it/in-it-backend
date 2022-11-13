package com.example.initbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class InItBackendApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(InItBackendApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedHeaders("Access-Control-Allow-Origin", "*")
				.allowedOrigins("http://localhost:3000","https://cr-init.herokuapp.com")
				.allowedMethods(
						HttpMethod.GET.name(),
						HttpMethod.HEAD.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name());
	}

}
