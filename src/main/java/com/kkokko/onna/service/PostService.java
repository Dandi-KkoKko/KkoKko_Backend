package com.kkokko.onna.service;

import com.kkokko.onna.dto.PostCreateReqDto;
import com.kkokko.onna.dto.PostCreateResDto;
import com.kkokko.onna.dto.TagResDto;
import com.kkokko.onna.dto.TagSuggestionReqDto;
import com.kkokko.onna.dto.TagSuggestionResDto;
import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import com.kkokko.onna.global.util.BlurProperties;
import com.kkokko.onna.repository.TagRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO 더미: 프론트가 Swagger로 먼저 붙여 볼 수 있게 고정값을 돌려준다. 할 일 5~9번에서 실제 구현으로 바꾼다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	// 더미에서 얼굴 감지 에러를 보여주는 oneLine 값
	private static final String DUMMY_FACE = "face";

	private final TagRepository tagRepository;
	private final BlurProperties blurProperties;

	// 실제 순서: 검증 → 얼굴 감지 → 축소본·S3 → 위치 흐림 → zone 배정 → publishAt → 저장. 지금은 앞의 검증만 한다.
	public PostCreateResDto create(Long userId, PostCreateReqDto req) {
		if (req.getImage().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		if (req.getAccuracy() > blurProperties.maxAccuracyM()) {
			throw new CustomException(ErrorCode.RETRY_LOCATION);
		}
		if (DUMMY_FACE.equals(req.getOneLine())) {
			throw new CustomException(ErrorCode.FACE_DETECTED);
		}
		return new PostCreateResDto(1L, LocalDateTime.now().plusHours(2));
	}

	// 실제로는 AI 서버가 고르고, 실패하면 내 선호 태그로 대체한다. 지금은 사전 앞쪽 3개.
	public TagSuggestionResDto suggestTags(TagSuggestionReqDto req) {
		return new TagSuggestionResDto(tagRepository.findAll().stream()
				.limit(3)
				.map(TagResDto::from)
				.toList());
	}
}
