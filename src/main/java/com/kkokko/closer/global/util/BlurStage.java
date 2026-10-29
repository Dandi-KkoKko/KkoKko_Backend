package com.kkokko.closer.global.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 사용자와 사진 사이 거리에 따른 공개 단계. 가까울수록 해상도가 높은 이미지를 준다.
@Getter
@RequiredArgsConstructor
public enum BlurStage {
	S0(24),
	S1(64),
	S2(160),
	OPEN(1080);

	private final int sizePx;

	// 경계값은 가까운 단계에 넣는다. 예: 정확히 open-m이면 OPEN.
	public static BlurStage of(double distanceM, double accuracyM, BlurProperties props) {
		if (distanceM <= props.openM()) {
			// 위치 오차가 크면 실제로는 멀리 있을 수 있으므로 열린 사진을 주지 않는다.
			return accuracyM > props.maxAccuracyM() ? S2 : OPEN;
		}
		if (distanceM <= props.s2M()) {
			return S2;
		}
		if (distanceM <= props.s1M()) {
			return S1;
		}
		return S0;
	}
}
