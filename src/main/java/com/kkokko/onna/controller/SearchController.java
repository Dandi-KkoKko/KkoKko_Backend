package com.kkokko.onna.controller;

import com.kkokko.onna.dto.HashtagSearchResDto;
import com.kkokko.onna.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색", description = "해시태그 검색 (지금은 더미 응답). 장소 검색은 프론트가 카카오맵으로 한다")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@Operation(summary = "[더미] 해시태그 검색", description = """
			키워드가 들어간 해시태그를 내 위치에서 가까운순으로 준다. 앞의 #은 있어도 없어도 된다.
			주소(경산시 대동)는 lat·lng로 프론트가 변환한다.
			더미: 키워드 "정문" → 정문앞·정문풍경·정문, 키워드 "없음" → 빈 배열
			- 400 INVALID_INPUT: keyword 비었음(# 만 있음 포함), lat·lng 누락""")
	@GetMapping("/hashtags")
	public ResponseEntity<List<HashtagSearchResDto>> searchHashtags(
			@Parameter(example = "정문") @RequestParam String keyword,
			@Parameter(example = "35.8330") @RequestParam double lat,
			@Parameter(example = "128.7550") @RequestParam double lng) {
		return ResponseEntity.ok(searchService.searchHashtags(keyword, lat, lng));
	}
}
