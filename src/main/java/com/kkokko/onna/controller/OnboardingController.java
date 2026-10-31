package com.kkokko.onna.controller;

import com.kkokko.onna.dto.TagResDto;
import com.kkokko.onna.dto.onboarding.PreferredTagsReqDto;
import com.kkokko.onna.dto.onboarding.QuestionResDto;
import com.kkokko.onna.service.OnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "온보딩·태그·질문", description = "태그 사전, 선호 태그 저장, 남기기 질문")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OnboardingController {

	private final OnboardingService onboardingService;

	@Operation(summary = "태그 사전", description = "온보딩과 기호 설정에서 고를 태그 전체. 지금은 data.sql 임시값 5개.")
	@GetMapping("/tags")
	public ResponseEntity<List<TagResDto>> getTags() {
		return ResponseEntity.ok(onboardingService.getTags());
	}

	@Operation(summary = "선호 태그 저장", description = """
			고른 태그로 통째로 바꾼다. 저장하면 GET /users/me의 onboarded가 true가 된다.
			- 400 INVALID_INPUT: tagIds가 비었음
			- 404 TAG_NOT_FOUND: 없는 태그 id""")
	@PutMapping("/users/me/tags")
	public ResponseEntity<Void> savePreferredTags(@AuthenticationPrincipal Long userId,
			@Valid @RequestBody PreferredTagsReqDto req) {
		onboardingService.savePreferredTags(userId, req.tagIds());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "남기기 질문 랜덤", description = "업로드할 때 이 id를 questionId로 보낸다. 지금은 임시 질문 5개 중 하나.")
	@GetMapping("/questions/random")
	public ResponseEntity<QuestionResDto> getRandomQuestion() {
		return ResponseEntity.ok(onboardingService.getRandomQuestion());
	}
}
