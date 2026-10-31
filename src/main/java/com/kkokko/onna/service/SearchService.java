package com.kkokko.onna.service;

import com.kkokko.onna.dto.HashtagSearchResDto;
import com.kkokko.onna.dto.PostPinResDto;
import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import com.kkokko.onna.global.util.GeoUtils;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

// TODO 더미: 키워드로 태그 이름을 만들어 고정 좌표에 붙여 준다. 실제로는 공개 기록의 태그를 이름으로 찾아 묶는다.
@Service
public class SearchService {

	// 더미에서 결과 없음 화면을 보여주는 키워드
	private static final String DUMMY_EMPTY = "없음";

	public List<HashtagSearchResDto> searchHashtags(String keyword, double lat, double lng) {
		String name = keyword.strip().replaceFirst("^#", "").strip();
		if (name.isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		if (name.equals(DUMMY_EMPTY)) {
			return List.of();
		}
		return List.of(
						dummy(1L, name + "앞", 35.8326, 128.7540, 9, lat, lng),
						dummy(2L, name + "풍경", 35.8335, 128.7555, 4, lat, lng),
						dummy(3L, name, 35.8318, 128.7560, 2, lat, lng))
				.stream()
				.sorted(Comparator.comparingInt(HashtagSearchResDto::getDistanceM))
				.toList();
	}

	private HashtagSearchResDto dummy(Long tagId, String name, double tagLat, double tagLng, int postCount,
			double lat, double lng) {
		List<PostPinResDto> pins = List.of(
				new PostPinResDto(tagId * 10 + 1, tagLat, tagLng),
				new PostPinResDto(tagId * 10 + 2, tagLat + 0.0004, tagLng + 0.0003));
		int distanceM = (int) Math.round(GeoUtils.distance(lat, lng, tagLat, tagLng));
		return new HashtagSearchResDto(tagId, name, tagLat, tagLng, distanceM, postCount, pins);
	}
}
