package com.kkokko.onna.global.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GeoUtilsTest {

	// 기준값은 WGS84 타원체 측지선 거리(geographiclib)로 계산했다. 하버사인은 구면 근사라 1% 오차까지 허용한다.
	@ParameterizedTest
	@CsvSource({
			"35.8325, 128.7544, 35.8360, 128.7580, 506.57",
			"35.8325, 128.7544, 35.8327, 128.7544, 22.19",
			"35.8325, 128.7544, 35.8325, 128.7547, 27.11"
	})
	void 거리는_측지선_거리와_1퍼센트_안에서_맞는다(double lat1, double lng1, double lat2, double lng2, double expectedM) {
		double actual = GeoUtils.distance(lat1, lng1, lat2, lng2);

		assertThat(actual).isCloseTo(expectedM, within(expectedM * 0.01));
	}

	@Test
	void 같은_좌표의_거리는_0이고_방향과_무관하다() {
		assertThat(GeoUtils.distance(35.8325, 128.7544, 35.8325, 128.7544)).isZero();
		assertThat(GeoUtils.distance(35.8325, 128.7544, 35.8360, 128.7580))
				.isEqualTo(GeoUtils.distance(35.8360, 128.7580, 35.8325, 128.7544));
	}

	@Test
	void 스냅한_좌표는_원래_좌표에서_격자_대각선_절반_안에_있다() {
		double gridM = 20;
		double maxShiftM = gridM * Math.sqrt(2) / 2;

		for (int i = 0; i < 1000; i++) {
			double lat = 35.83 + i * 0.000013;
			double lng = 128.75 + i * 0.000017;
			LatLng snapped = GeoUtils.snap(lat, lng, gridM);

			// 경도 간격을 스냅된 위도로 잡기 때문에 아주 조금 넘을 수 있어 0.1m 여유를 둔다.
			assertThat(GeoUtils.distance(lat, lng, snapped.lat(), snapped.lng())).isLessThanOrEqualTo(maxShiftM + 0.1);
		}
	}

	@Test
	void 같은_칸_안의_좌표는_같은_점으로_스냅된다() {
		LatLng a = GeoUtils.snap(35.832500, 128.754400, 20);
		LatLng b = GeoUtils.snap(35.832520, 128.754420, 20);

		assertThat(b).isEqualTo(a);
	}

	@Test
	void 스냅을_두_번_해도_결과가_같다() {
		LatLng once = GeoUtils.snap(35.8331, 128.7552, 20);
		LatLng twice = GeoUtils.snap(once.lat(), once.lng(), 20);

		assertThat(twice.lat()).isCloseTo(once.lat(), within(1e-9));
		assertThat(twice.lng()).isCloseTo(once.lng(), within(1e-9));
	}

	// 경도 간격은 행의 위도로 정해서 행마다 열 위치가 조금씩 어긋난다. 그래서 행 간격과 같은 행 안의 열 간격을 따로 본다.
	@Test
	void 이웃한_격자점_사이_거리는_격자_간격이다() {
		double gridM = 20;
		double latStepDeg = gridM / (6_371_000 * Math.PI / 180);
		LatLng a = GeoUtils.snap(35.8325, 128.7544, gridM);
		LatLng north = GeoUtils.snap(a.lat() + latStepDeg, a.lng(), gridM);
		double lngStepDeg = latStepDeg / Math.cos(Math.toRadians(a.lat()));
		LatLng east = GeoUtils.snap(a.lat(), a.lng() + lngStepDeg, gridM);

		assertThat(GeoUtils.distance(a.lat(), a.lng(), north.lat(), a.lng())).isCloseTo(gridM, within(0.01));
		assertThat(GeoUtils.distance(a.lat(), a.lng(), east.lat(), east.lng())).isCloseTo(gridM, within(0.01));
	}
}
