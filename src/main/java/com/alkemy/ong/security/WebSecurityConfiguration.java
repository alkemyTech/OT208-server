package com.alkemy.ong.security;

import com.alkemy.ong.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final JwtTokenFilter jwtTokenFilter;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	private static final String[] USER_GET = {};
	private static final String[] USER_POST = {};
	private static final String[] USER_PUT = {};
	private static final String[] USER_PATCH_DELETE = {};
	private static final String[] ANY_USER_GET = {"/auth/me/{id}"};
	private static final String[] ANY_USER_POST = {};
	private static final String[] ANY_USER_PUT_DELETE = {"/comments/{id}"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Configuracion para H2
		http.authorizeRequests()
				.antMatchers("/h2-console/**").permitAll()
				.and().csrf().ignoringAntMatchers("/h2-console/**")
				.and().headers().frameOptions().sameOrigin();

		// Endpoints
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/api/**").permitAll()

				/*
				I leave this commentary as an informative starting point
				// Categories
				.antMatchers(HttpMethod.GET,"/categories").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/categories/{id}").hasRole("ADMIN")
				// Organization
				.antMatchers(HttpMethod.GET,"/organization/public").hasRole("ADMIN") // error 400
				// UploadFileController
				.antMatchers(HttpMethod.GET,"/storage/list").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/storage/download").hasRole("ADMIN") // error 400
				.antMatchers(HttpMethod.POST,"/storage/upload").hasRole("ADMIN") // error 500
				// Users
				.antMatchers(HttpMethod.DELETE,"/users/user/{id}").hasRole("ADMIN")
				// Authorization
				"/auth/login", "/auth/register" do not require rol
				.antMatchers(HttpMethod.GET,"/auth/me/{id}").hasAnyRole("ADMIN","USER")
				// Validation
				.antMatchers(HttpMethod.POST,"/validation/{id}").hasRole("ADMIN")
				// News
				.antMatchers(HttpMethod.GET,"/news/{id}").hasRole("ADMIN") // error 500
				//Slides
				.antMatchers(HttpMethod.GET,"/slides").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/slides/{id}").hasRole("ADMIN")
				*/

				// ANY USER
				.antMatchers(HttpMethod.GET, ANY_USER_GET).hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.POST, ANY_USER_POST).hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.PUT, ANY_USER_PUT_DELETE).hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.DELETE, ANY_USER_PUT_DELETE).hasAnyRole("ADMIN", "USER")
				// USER
				.antMatchers(HttpMethod.GET, USER_GET).hasRole( "USER")
				.antMatchers(HttpMethod.POST, USER_POST).hasRole("USER")
				.antMatchers(HttpMethod.PUT, USER_PUT).hasRole("USER")
				.antMatchers(HttpMethod.PATCH, USER_PATCH_DELETE).hasRole("USER")
				.antMatchers(HttpMethod.DELETE, USER_PATCH_DELETE).hasRole("USER")
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