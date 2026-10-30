package com.kkokko.onna.global.util;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

// 단계 경계 거리(m). 현장에서 GPS 오차를 보고 조정하므로 코드에 박지 않는다.
@Validated
@ConfigurationProperties("app.blur")
public record BlurProperties(
		@Positive double s1M,
		@Positive double s2M,
		@Positive double openM,
		@Positive double maxAccuracyM
) {
}
