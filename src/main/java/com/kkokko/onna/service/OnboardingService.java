package com.kkokko.onna.service;

import com.kkokko.onna.domain.Tag;
import com.kkokko.onna.domain.User;
import com.kkokko.onna.domain.UserPreferredTag;
import com.kkokko.onna.dto.TagResDto;
import com.kkokko.onna.dto.onboarding.QuestionResDto;
import com.kkokko.onna.global.constant.QuestionCatalog;
import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import com.kkokko.onna.repository.TagRepository;
import com.kkokko.onna.repository.UserPreferredTagRepository;
import com.kkokko.onna.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingService {

	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final UserPreferredTagRepository userPreferredTagRepository;

	public List<TagResDto> getTags() {
		return tagRepository.findAll().stream().map(TagResDto::from).toList();
	}

	// 온보딩과 기호 수정이 같은 API라서 기존 선호 태그를 지우고 새로 저장한다.
	@Transactional
	public void savePreferredTags(Long userId, List<Long> tagIds) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
		List<Long> distinctIds = tagIds.stream().distinct().toList();
		List<Tag> tags = tagRepository.findAllById(distinctIds);
		if (tags.size() != distinctIds.size()) {
			throw new CustomException(ErrorCode.TAG_NOT_FOUND);
		}
		userPreferredTagRepository.deleteAllByUserId(userId);
		userPreferredTagRepository.saveAll(tags.stream()
				.map(tag -> UserPreferredTag.builder().user(user).tag(tag).build())
				.toList());
	}

	public QuestionResDto getRandomQuestion() {
		return QuestionResDto.from(QuestionCatalog.random());
	}
}
