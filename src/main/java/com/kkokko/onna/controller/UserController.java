package com.kkokko.onna.controller;

import com.kkokko.onna.dto.MyPostResDto;
import com.kkokko.onna.dto.UserResDto;
import com.kkokko.onna.service.PostService;
import com.kkokko.onna.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자", description = "내 정보·내 기록")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PostService postService;

	@Operation(summary = "내 정보", description = """
			- 401 UNAUTHORIZED: 토큰 없음
			- 401 INVALID_TOKEN: 만료·위조 토큰, 또는 없는 사용자""")
	@GetMapping("/me")
	public ResponseEntity<UserResDto> getMe(@Parameter(hidden = true) @AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(userService.getMe(userId));
	}

	@Operation(summary = "[더미] 내 기록", description = """
			내가 남긴 기록 최신순. 공개 전 기록도 보인다. zoneId를 주면 그 구역만. 지금은 고정 2개를 준다.
			- 401 UNAUTHORIZED / INVALID_TOKEN""")
	@GetMapping("/me/posts")
	public ResponseEntity<List<MyPostResDto>> getMyPosts(@Parameter(hidden = true) @AuthenticationPrincipal Long userId,
			@RequestParam(required = false) Long zoneId) {
		return ResponseEntity.ok(postService.getMyPosts(userId, zoneId));
	}
}
