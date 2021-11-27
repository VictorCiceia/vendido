package com.vendido.vendido.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties("spring.cache")
@Data
public class CacheProperty {

	private String address;

	private int timer;

}
