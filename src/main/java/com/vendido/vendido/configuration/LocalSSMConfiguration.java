package com.vendido.vendido.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.AbstractSSMConfiguration;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheConfiguration;
import com.vendido.vendido.property.CacheProperty;

@Configuration
public class LocalSSMConfiguration extends AbstractSSMConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalSSMConfiguration.class);

	@Autowired
	private CacheProperty cacheProperty;

	@Bean
	@Autowired
	public CacheFactory defaultMemcachedClient() {
		final CacheConfiguration conf = new CacheConfiguration();
		conf.setConsistentHashing(true);
		conf.setOperationTimeout(cacheProperty.getTimer());
		final CacheFactory cf = new CacheFactory();
		cf.setCacheClientFactory(new com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl());
		cf.setAddressProvider(new DefaultAddressProvider(cacheProperty.getAddress()));
		cf.setConfiguration(conf);
		LOGGER.info("Se conecto el servior cache");
		return cf;
	}
}