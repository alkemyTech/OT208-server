package com.alkemy.ong.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Configuracion para H2
		http.authorizeRequests().antMatchers("/h2-console/**").permitAll().and().csrf()
				.ignoringAntMatchers("/h2-console/**").and().headers().frameOptions().sameOrigin();

		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/auth/**").permitAll()
				.antMatchers("/api/**").permitAll().anyRequest().permitAll()
				// .and()
				// .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// http.addFilterBefore(jwtTokenFilter(),
		// UsernamePasswordAuthenticationFilter.class); }
	}
}
