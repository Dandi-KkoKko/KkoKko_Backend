package com.kkokko.onna.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BlurStageTest {

	// application.properties의 기본값과 같다.
	private final BlurProperties props = new BlurProperties(150, 60, 25, 50);

	@ParameterizedTest
	@CsvSource({
			"0, OPEN",
			"25, OPEN",
			"25.1, S2",
			"60, S2",
			"60.1, S1",
			"150, S1",
			"150.1, S0",
			"1000, S0"
	})
	void 거리에_따라_단계가_정해지고_경계값은_가까운_단계에_들어간다(double distanceM, BlurStage expected) {
		assertThat(BlurStage.of(distanceM, 10, props)).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({
			"50, OPEN",
			"50.1, S2"
	})
	void 위치_오차가_기준보다_크면_가까워도_열린_사진을_주지_않는다(double accuracyM, BlurStage expected) {
		assertThat(BlurStage.of(10, accuracyM, props)).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({
			"40, S2",
			"100, S1",
			"200, S0"
	})
	void 위치_오차는_열기_전_단계에는_영향을_주지_않는다(double distanceM, BlurStage expected) {
		assertThat(BlurStage.of(distanceM, 500, props)).isEqualTo(expected);
	}
}
