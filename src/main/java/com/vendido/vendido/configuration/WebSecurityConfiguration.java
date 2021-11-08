package com.vendido.vendido.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vendido.vendido.segurity.JwtAuthenticationFilter;
import com.vendido.vendido.segurity.JwtAuthorizationFilter;
import com.vendido.vendido.segurity.service.UserJpaService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	private static final long MAX_AGE_SECS = 3600;

	@Autowired
	private UserJpaService jpaUserService;

	@Bean
	public BCryptPasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder //
				.userDetailsService(jpaUserService) //
				.passwordEncoder(passwordEncode());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.addFilter(new JwtAuthenticationFilter(authenticationManager(), getApplicationContext())) //
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), getApplicationContext())) //
				.csrf().disable().exceptionHandling() //
				.and().cors()//
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
				.and().headers().frameOptions().disable()//
				.and().authorizeRequests().antMatchers("/auth/**").permitAll()//
				.and().authorizeRequests().antMatchers("/api/**").authenticated();
	}

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**") //
				.allowedOrigins("*") //
				.allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE") //
				.maxAge(MAX_AGE_SECS);
	}

}
