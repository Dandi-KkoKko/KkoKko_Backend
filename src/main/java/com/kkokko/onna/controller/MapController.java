package com.kkokko.onna.controller;

import com.kkokko.onna.dto.map.PostPreviewResDto;
import com.kkokko.onna.dto.map.ZoneResDto;
import com.kkokko.onna.global.util.BlurStage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "지도", description = "구역 묶음 지도와 구역 안 사진 목록")
@RestController
@RequestMapping("/api/v1")
public class MapController {

	// TODO 더미: 프론트가 응답 형태로 먼저 붙도록 고정값을 준다. 구현할 때 MapService 호출로 바꾼다.
	// 구역·태그는 data.sql 임시값과 맞췄다.
	private static final List<ZoneResDto> DUMMY_ZONES = List.of(
			new ZoneResDto(1L, "임시 구역 A", 35.8326, 128.7540, false, 3, List.of(
					new PostPreviewResDto(12L, BlurStage.S1, "https://example.com/dummy/12/s1.jpg",
							List.of("말없이 챙겨준 사람"), false, 84))),
			new ZoneResDto(2L, "임시 구역 B", 35.8335, 128.7555, false, 0, List.of()),
			new ZoneResDto(3L, "임시 랜드마크 C", 35.8318, 128.7560, true, 5, List.of(
					new PostPreviewResDto(31L, BlurStage.OPEN, "https://example.com/dummy/31/open.jpg",
							List.of("늘 그 자리에 있는 곳"), true, 210),
					new PostPreviewResDto(7L, BlurStage.S0, "https://example.com/dummy/7/s0.jpg",
							List.of("처음 와도 편한 곳", "무뚝뚝한 안부"), false, 230))));

	private static final List<PostPreviewResDto> DUMMY_ZONE_POSTS = List.of(
			new PostPreviewResDto(12L, BlurStage.S1, "https://example.com/dummy/12/s1.jpg",
					List.of("말없이 챙겨준 사람"), false, 84),
			new PostPreviewResDto(15L, BlurStage.S2, "https://example.com/dummy/15/s2.jpg",
					List.of("계산 없이 내어준 마음"), false, 41),
			new PostPreviewResDto(18L, BlurStage.OPEN, "https://example.com/dummy/18/open.jpg",
					List.of("무뚝뚝한 안부"), false, 12));

	@Operation(summary = "지도 구역 조회 (더미)",
			description = "반경 안 구역별 사진 수와 대표 사진. 사진이 없는 구역도 내려준다. 랜드마크는 대표 사진 최대 2장.")
	@GetMapping("/map/zones")
	public ResponseEntity<List<ZoneResDto>> getZones(
			@RequestParam double lat,
			@RequestParam double lng,
			@RequestParam double accuracy,
			@RequestParam(defaultValue = "500") int radius) {
		return ResponseEntity.ok(DUMMY_ZONES);
	}

	@Operation(summary = "구역 사진 목록 (더미)",
			description = "내 위치에서 사진까지 거리에 맞는 단계의 이미지만 내려준다. 작성자와 좋아요 수는 없다.")
	@GetMapping("/zones/{zoneId}/posts")
	public ResponseEntity<List<PostPreviewResDto>> getZonePosts(
			@PathVariable Long zoneId,
			@RequestParam double lat,
			@RequestParam double lng,
			@RequestParam double accuracy) {
		return ResponseEntity.ok(DUMMY_ZONE_POSTS);
	}
}
