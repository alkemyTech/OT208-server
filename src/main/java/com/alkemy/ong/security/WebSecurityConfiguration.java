package com.alkemy.ong.security;

import com.alkemy.ong.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableJpaAuditing
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final JwtTokenFilter jwtTokenFilter;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	private static final String[] ANY_USER_GET = {"/contacts", "/auth/me/**"};
	private static final String[] ANY_USER_POST = {"/comments", "/contacts", "/members"};
	private static final String[] ANY_USER_PUT = {"/comments/{id}", "/members/{id}", "/users/{id}"};
	private static final String[] ANY_USER_DELETE = {"/comments/{id}", "/users/{id}"};

	private static final String[] PUBLIC = {"/auth/register", "/auth/login"};
	private static final String[] PUBLIC_GET = {"/organization/public", "/post/{id}/comments", "/news/list"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// H2 Configuration
		http.authorizeRequests()
				.antMatchers("/h2-console/**").permitAll()
				.and().csrf().ignoringAntMatchers("/h2-console/**")
				.and().headers().frameOptions().sameOrigin();

		// Endpoints
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers(PUBLIC).permitAll()
				.antMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()

				// ANY USER
				.antMatchers(HttpMethod.GET, ANY_USER_GET).hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.POST, ANY_USER_POST).hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.PUT, ANY_USER_PUT).hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.DELETE, ANY_USER_DELETE).hasAnyRole("ADMIN", "USER")

				// ADMIN
				.antMatchers(HttpMethod.GET, "/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

				.anyRequest().permitAll()

				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

}