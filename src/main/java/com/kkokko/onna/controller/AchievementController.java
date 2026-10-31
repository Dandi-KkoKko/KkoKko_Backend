package com.kkokko.onna.controller;

import com.kkokko.onna.dto.achievement.AchievementResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "발견 업적 (P1)")
@RestController
public class AchievementController {

	@Operation(summary = "[더미] 발견 업적", description = "연 사진 수, 발견한 구역 수, 처음으로 연 사진 수.")
	@GetMapping("/api/v1/users/me/achievements")
	public ResponseEntity<AchievementResDto> getAchievements(@AuthenticationPrincipal Long userId) {
		// TODO 더미: 구현할 때 post_open 집계로 바꾼다.
		return ResponseEntity.ok(new AchievementResDto(7, 3, 1));
	}
}
