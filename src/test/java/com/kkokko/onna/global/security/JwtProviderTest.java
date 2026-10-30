package com.kkokko.onna.global.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

	private static final String SECRET = "test-secret-test-secret-test-secret-1234";

	private final JwtProvider jwtProvider = provider(SECRET, Duration.ofHours(1));

	@Test
	void Access_토큰에서_userId를_꺼낸다() {
		String token = jwtProvider.createAccessToken(7L);

		assertThat(jwtProvider.getUserIdFromAccessToken(token)).isEqualTo(7L);
	}

	@Test
	void Refresh_토큰에서_userId를_꺼낸다() {
		String token = jwtProvider.createRefreshToken(7L);

		assertThat(jwtProvider.getUserIdFromRefreshToken(token)).isEqualTo(7L);
	}

	@Test
	void Refresh_토큰은_Access_자리에_쓸_수_없다() {
		String refresh = jwtProvider.createRefreshToken(7L);

		assertInvalid(() -> jwtProvider.getUserIdFromAccessToken(refresh));
	}

	@Test
	void Access_토큰은_Refresh_자리에_쓸_수_없다() {
		String access = jwtProvider.createAccessToken(7L);

		assertInvalid(() -> jwtProvider.getUserIdFromRefreshToken(access));
	}

	@Test
	void 만료된_토큰은_거부한다() {
		String expired = provider(SECRET, Duration.ofSeconds(-1)).createAccessToken(7L);

		assertInvalid(() -> jwtProvider.getUserIdFromAccessToken(expired));
	}

	@Test
	void 다른_키로_서명한_토큰은_거부한다() {
		String forged = provider("other-secret-other-secret-other-secret-12", Duration.ofHours(1))
				.createAccessToken(7L);

		assertInvalid(() -> jwtProvider.getUserIdFromAccessToken(forged));
	}

	@Test
	void 형식이_깨진_토큰은_거부한다() {
		assertInvalid(() -> jwtProvider.getUserIdFromAccessToken("not-a-jwt"));
		assertInvalid(() -> jwtProvider.getUserIdFromAccessToken(""));
	}

	private static JwtProvider provider(String secret, Duration expiration) {
		return new JwtProvider(new JwtProperties(secret, expiration, expiration));
	}

	private static void assertInvalid(Runnable call) {
		assertThatThrownBy(call::run)
				.isInstanceOf(CustomException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_TOKEN);
	}
}
