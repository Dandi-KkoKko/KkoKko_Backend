package com.kkokko.onna.global.security;

import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

	// Refresh 토큰을 Access 자리에 넣어 쓰지 못하게 종류를 같이 싣는다.
	private static final String TYPE = "type";
	private static final String ACCESS = "access";
	private static final String REFRESH = "refresh";

	private final JwtProperties props;
	private final SecretKey key;

	public JwtProvider(JwtProperties props) {
		this.props = props;
		this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(Long userId) {
		return createToken(userId, ACCESS, props.accessExpiration());
	}

	public String createRefreshToken(Long userId) {
		return createToken(userId, REFRESH, props.refreshExpiration());
	}

	public Long getUserIdFromAccessToken(String token) {
		return parseUserId(token, ACCESS);
	}

	public Long getUserIdFromRefreshToken(String token) {
		return parseUserId(token, REFRESH);
	}

	private String createToken(Long userId, String type, Duration expiration) {
		Date now = new Date();
		return Jwts.builder()
				.subject(String.valueOf(userId))
				.claim(TYPE, type)
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expiration.toMillis()))
				.signWith(key)
				.compact();
	}

	// 서명·만료·종류 중 하나라도 맞지 않으면 INVALID_TOKEN.
	private Long parseUserId(String token, String type) {
		try {
			var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
			if (!type.equals(claims.get(TYPE, String.class))) {
				throw new CustomException(ErrorCode.INVALID_TOKEN);
			}
			return Long.valueOf(claims.getSubject());
		} catch (JwtException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}
}
