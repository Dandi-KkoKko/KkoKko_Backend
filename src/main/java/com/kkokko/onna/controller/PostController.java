package com.kkokko.onna.controller;

import com.kkokko.onna.dto.PostCreateReqDto;
import com.kkokko.onna.dto.PostCreateResDto;
import com.kkokko.onna.dto.TagSuggestionReqDto;
import com.kkokko.onna.dto.TagSuggestionResDto;
import com.kkokko.onna.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사진 남기기", description = "업로드·해시태그 후보·삭제 (지금은 더미 응답)")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@Operation(summary = "[더미] 사진 남기기", description = """
			multipart/form-data. 지금은 저장하지 않고 postId 1, publishAt 2시간 뒤를 준다.
			- 400 INVALID_INPUT: 필수 값 누락, 빈 사진, oneLine 100자 초과, 태그 3개 초과, 10MB 초과
			- 400 RETRY_LOCATION: accuracy가 50 초과
			- 422 FACE_DETECTED: 사진에 얼굴 있음 (더미: oneLine을 "face"로 보내면 발생)""")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostCreateResDto> create(@Parameter(hidden = true) @AuthenticationPrincipal Long userId,
			@Valid @ModelAttribute PostCreateReqDto req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(userId, req));
	}

	@Operation(summary = "[더미] 해시태그 후보", description = """
			질문과 한 줄을 보고 고를 만한 태그 3~5개를 준다. 지금은 태그 사전 앞쪽 3개를 고정으로 준다.
			- 400 INVALID_INPUT: questionId 없음, oneLine 비었거나 100자 초과""")
	@PostMapping("/tag-suggestions")
	public ResponseEntity<TagSuggestionResDto> suggestTags(@Valid @RequestBody TagSuggestionReqDto req) {
		return ResponseEntity.ok(postService.suggestTags(req));
	}
}
