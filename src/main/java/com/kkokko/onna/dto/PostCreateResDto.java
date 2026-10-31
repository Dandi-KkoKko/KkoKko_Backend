package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResDto {

	private Long postId;

	@Schema(description = "남에게 보이기 시작하는 시각. 내 기록에는 바로 보인다")
	private LocalDateTime publishAt;
}
