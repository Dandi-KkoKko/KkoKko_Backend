package com.kkokko.onna.controller;

import com.kkokko.onna.dto.recommend.RouteResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "경로 추천 (P1)")
@RestController
public class RecommendController {

	@Operation(summary = "[더미] 산책 경로 추천", description = "아직 안 연 사진 중 취향에 맞는 곳을 이어 걷는 순서.")
	@GetMapping("/api/v1/recommendations/route")
	public ResponseEntity<RouteResDto> getRoute(
			@AuthenticationPrincipal Long userId,
			@RequestParam double lat,
			@RequestParam double lng,
			@RequestParam(defaultValue = "20") int minutes) {
		// TODO 더미: 구현할 때 AI /ai/route 호출, 실패하면 가까운 순으로 대체한다.
		return ResponseEntity.ok(new RouteResDto("이 세 자리를 잇는 20분 길", List.of(
				new RouteResDto.Stop(12L, 35.8326, 128.7540),
				new RouteResDto.Stop(31L, 35.8318, 128.7560),
				new RouteResDto.Stop(7L, 35.8335, 128.7555))));
	}
}
