package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPostResDto {

	private Long postId;
	private int questionId;
	private String oneLine;

	@Schema(description = "선명한 사진 URL (5분 뒤 만료)")
	private String imageUrl;

	@Schema(description = "흐린 좌표 (저장된 그대로)")
	private double lat;

	private double lng;
	private List<TagResDto> tags;

	@Schema(description = "좋아요 수. 작성자 본인에게만 내려간다")
	private int likeCount;

	private LocalDateTime createdAt;
}
