package com.kkokko.onna.controller;

import com.kkokko.onna.dto.open.OpenReqDto;
import com.kkokko.onna.dto.open.OpenResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "열기·좋아요", description = "가까이 가서 사진 열기, 열린 사진에 좋아요")
@RestController
@RequestMapping("/api/v1/posts/{postId}")
public class OpenController {

	@Operation(summary = "[더미] 사진 열기",
			description = "25m 안이면 사진과 한 줄을 공개한다. 멀면 400 TOO_FAR, 위치 오차가 50m를 넘으면 400 RETRY_LOCATION.")
	@PostMapping("/open")
	public ResponseEntity<OpenResDto> open(
			@AuthenticationPrincipal Long userId,
			@PathVariable Long postId,
			@Valid @RequestBody OpenReqDto request) {
		// TODO 더미: 구현할 때 OpenService에서 거리·accuracy 검증과 post_open 기록을 한다.
		return ResponseEntity.ok(new OpenResDto(postId, "https://picsum.photos/seed/onna" + postId + "/1080",
				"여기서 누가 챙겨줬어?", "사장님이 말없이 고기 더 얹어줌", List.of("말없이 챙겨준 사람"), false));
	}

	@Operation(summary = "[더미] 좋아요", description = "열어 본 사진에만 누를 수 있다. 이미 눌렀으면 그대로 성공. 좋아요 수는 응답하지 않는다.")
	@PostMapping("/likes")
	public ResponseEntity<Void> like(@AuthenticationPrincipal Long userId, @PathVariable Long postId) {
		// TODO 더미: 구현할 때 LikeService 호출로 바꾼다.
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "[더미] 좋아요 취소", description = "누르지 않은 사진이어도 그대로 성공.")
	@DeleteMapping("/likes")
	public ResponseEntity<Void> unlike(@AuthenticationPrincipal Long userId, @PathVariable Long postId) {
		// TODO 더미: 구현할 때 LikeService 호출로 바꾼다.
		return ResponseEntity.noContent().build();
	}
}
