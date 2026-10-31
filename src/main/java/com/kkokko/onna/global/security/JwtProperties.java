package com.kkokko.onna.global.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

// HS256 서명 키는 32바이트 이상이어야 해서 짧은 값은 기동할 때 막는다.
@Validated
@ConfigurationProperties("app.jwt")
public record JwtProperties(
		@NotNull @Size(min = 32) String secret,
		@NotNull Duration accessExpiration,
		@NotNull Duration refreshExpiration
) {
}
