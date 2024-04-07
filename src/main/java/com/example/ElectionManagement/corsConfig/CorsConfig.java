package com.example.ElectionManagement.corsConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**").allowedOrigins("http://localhost:3000","https://election-tau.vercel.app/") // specify the origins you want to allow
				.allowedMethods("GET", "POST", "PUT", "DELETE","PATCH").allowedHeaders("*").allowCredentials(true);

	}
}
