package com.kkokko.onna.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkokko.onna.global.exception.ErrorCode;
import com.kkokko.onna.global.exception.ErrorDto;
import com.kkokko.onna.global.security.JwtFilter;
import com.kkokko.onna.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// /error는 열어 둬야 404·405 같은 응답이 401로 바뀌지 않는다.
						.requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
						.anyRequest().authenticated())
				.exceptionHandling(e -> e.authenticationEntryPoint(this::writeUnauthorized))
				.addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 필터에서 난 인증 실패는 GlobalExceptionHandler까지 가지 않으므로 같은 ErrorDto 형식으로 직접 쓴다.
	private void writeUnauthorized(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException {
		ErrorCode errorCode = request.getHeader(HttpHeaders.AUTHORIZATION) == null
				? ErrorCode.UNAUTHORIZED
				: ErrorCode.INVALID_TOKEN;
		response.setStatus(errorCode.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		objectMapper.writeValue(response.getWriter(), ErrorDto.from(errorCode));
	}
}
