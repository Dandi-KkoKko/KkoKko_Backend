package com.kkokko.onna.dto.map;

import java.util.List;

// 사진이 0개인 구역도 내려서 빈 구역으로 보여 준다. 대표 사진은 일반 구역 1장, 랜드마크 최대 2장.
public record ZoneResDto(
		Long zoneId,
		String name,
		double lat,
		double lng,
		boolean isLandmark,
		int postCount,
		List<PostPreviewResDto> previews
) {
}
