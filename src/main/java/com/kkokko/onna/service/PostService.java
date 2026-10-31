package com.kkokko.onna.service;

import com.kkokko.onna.dto.TagResDto;
import com.kkokko.onna.dto.TagSuggestionReqDto;
import com.kkokko.onna.dto.TagSuggestionResDto;
import com.kkokko.onna.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO 더미: 프론트가 Swagger로 먼저 붙여 볼 수 있게 고정값을 돌려준다. 할 일 5~9번에서 실제 구현으로 바꾼다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final TagRepository tagRepository;

	// 실제로는 AI 서버가 고르고, 실패하면 내 선호 태그로 대체한다. 지금은 사전 앞쪽 3개.
	public TagSuggestionResDto suggestTags(TagSuggestionReqDto req) {
		return new TagSuggestionResDto(tagRepository.findAll().stream()
				.limit(3)
				.map(TagResDto::from)
				.toList());
	}
}
