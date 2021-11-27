package com.vendido.vendido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VendidoApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(VendidoApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder app) {
		return app.sources(VendidoApplication.class);
	}

}
