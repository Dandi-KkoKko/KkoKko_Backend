package com.kkokko.onna.service;

import com.kkokko.onna.domain.Tag;
import com.kkokko.onna.dto.MyPostResDto;
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
import java.util.List;
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
	// 실제로는 S3 presigned URL. 프론트가 화면에 그려 볼 수 있게 아무 사진이나 나오는 주소를 쓴다.
	private static final String DUMMY_IMAGE_URL = "https://picsum.photos/seed/onna";

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

	// 실제로는 삭제 안 된 내 기록 전부를 최신순으로, zoneId가 있으면 그 구역만. 지금은 고정 2개.
	public List<MyPostResDto> getMyPosts(Long userId, Long zoneId) {
		List<String> tags = tagRepository.findAll().stream().limit(2).map(Tag::getName).toList();
		LocalDateTime now = LocalDateTime.now();
		return List.of(
				new MyPostResDto(2L, 2, "늘 같은 자리에서 인사해 주던 경비 아저씨", DUMMY_IMAGE_URL + "2/1080/1080",
						35.8335, 128.7555, tags, 3, now.minusHours(1)),
				new MyPostResDto(1L, 1, "말없이 우산을 씌워 준 선배가 있었다", DUMMY_IMAGE_URL + "1/1080/1080",
						35.8326, 128.7540, tags.subList(0, Math.min(1, tags.size())), 0, now.minusDays(1)));
	}

	// 실제로는 본인 확인 후 소프트 삭제. 더미: postId 404 → POST_NOT_FOUND, 403 → NOT_OWNER
	public void delete(Long userId, Long postId) {
		if (postId == 404) {
			throw new CustomException(ErrorCode.POST_NOT_FOUND);
		}
		if (postId == 403) {
			throw new CustomException(ErrorCode.NOT_OWNER);
		}
	}

	// 실제로는 AI 서버가 고르고, 실패하면 내 선호 태그로 대체한다. 지금은 사전 앞쪽 3개.
	public TagSuggestionResDto suggestTags(TagSuggestionReqDto req) {
		return new TagSuggestionResDto(tagRepository.findAll().stream()
				.limit(3)
				.map(TagResDto::from)
				.toList());
	}
}
