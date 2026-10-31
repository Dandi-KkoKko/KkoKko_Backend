package com.kkokko.onna.dto.recommend;

import java.util.List;

// 걸어서 들를 사진 순서. AI 추천이 실패하면 가까운 순으로 대체한다.
public record RouteResDto(
		String title,
		List<Stop> stops
) {

	public record Stop(Long postId, double lat, double lng) {
	}
}
