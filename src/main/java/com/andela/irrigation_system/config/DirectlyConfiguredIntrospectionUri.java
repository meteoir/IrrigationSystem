package com.andela.irrigation_system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

import static java.util.Collections.singletonList;

@Slf4j
@Configuration
@EnableWebMvc
@EnableWebSecurity
@Profile({Profiles.PROD})
@PropertySource("classpath:application.properties")
public class DirectlyConfiguredIntrospectionUri {

	@Value("${security.oauth2.resourceserver.opaque-token.introspection-uri}")
	private String introspectionUri;
	@Value("${security.oauth2.resourceserver.opaque-token.client-id}")
	private String clientId;
	@Value("${security.oauth2.resourceserver.opaque-token.client-secret}")
	private String clientSecret;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		try {
			http
					.cors().and().csrf().disable()
					.securityMatcher("/api/v1/**")
					.authorizeHttpRequests()
					.anyRequest().authenticated()
					.and()
					.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
		} catch (Exception e) {
			log.error("Could not configure resource server");
		}
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(singletonList("https://www.tracker.andela.net"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		// uncomment for local
//		configuration.setAllowedOrigins(singletonList("*"));
//		configuration.setAllowedMethods(singletonList("*"));
//		configuration.setAllowedHeaders(singletonList("*"));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public OpaqueTokenIntrospector introspector() {
		return new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
	}
}

