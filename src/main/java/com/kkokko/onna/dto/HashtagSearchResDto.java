package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HashtagSearchResDto {

	private Long tagId;

	@Schema(description = "# 없이 준다", example = "정문앞")
	private String name;

	@Schema(description = "내 위치에서 가장 가까운 기록 좌표. 목록 주소·상세 지도 중심으로 쓴다")
	private double lat;

	private double lng;

	@Schema(description = "내 위치에서 lat·lng까지 거리(m)", example = "520")
	private int distanceM;

	@Schema(description = "이 태그로 남겨진 공개 기록 수", example = "9")
	private int postCount;

	@Schema(description = "지도에 찍을 기록 핀")
	private List<PostPinResDto> posts;
}
