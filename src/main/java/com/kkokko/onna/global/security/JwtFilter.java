package com.kkokko.onna.global.security;

import com.kkokko.onna.global.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// Access 토큰이 맞으면 userId를 인증 정보로 넣는다. 컨트롤러에서는 @AuthenticationPrincipal Long userId로 받는다.
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith(BEARER)) {
			try {
				Long userId = jwtProvider.getUserIdFromAccessToken(header.substring(BEARER.length()));
				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(userId, null, List.of()));
			} catch (CustomException e) {
				// 인증 없이 넘긴다. 로그인이 필요한 경로면 SecurityConfig의 entry point가 401을 낸다.
			}
		}
		chain.doFilter(request, response);
	}
}
