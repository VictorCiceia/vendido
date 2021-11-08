package com.vendido.vendido.segurity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendido.vendido.entity.UserEntity;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private AuthenticationManager authManager;
	private JwtTokenManager tokenProvider;

	public JwtAuthenticationFilter( //
			final AuthenticationManager authenticationManager, final ApplicationContext applicationContext //
	) {
		this.authManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
		tokenProvider = applicationContext.getBean(JwtTokenManager.class);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
		setUsernameParameter("email");
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		UserEntity user = null;
		if (username != null && password != null) {
			log.debug("Username and Password error");
		} else {
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);

				username = user.getEmail();
				password = user.getPassword();

			} catch (final IOException e) {
				log.error("Can not extract user: {}", user);
			}

		}

		final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( //
				username.trim(), //
				password);

		return authManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication( //
			final HttpServletRequest request, //
			final HttpServletResponse response, //
			final FilterChain chain, //
			final Authentication authResult) throws IOException, ServletException {

		final User user = (User) authResult.getPrincipal();

		final String token = tokenProvider.generateToken(user);

		response.addHeader("Authorization", "Bearer " + token);

		final Map<String, Object> body = new HashMap<>();
		body.put("token", token);
		body.put("user", user);
		body.put("message", "sucess login");

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}
}
