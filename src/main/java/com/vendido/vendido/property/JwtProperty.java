package com.vendido.vendido.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {
	
	private String key;

	private long expiration;

}
