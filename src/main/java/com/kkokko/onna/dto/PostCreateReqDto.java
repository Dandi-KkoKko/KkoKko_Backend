package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

// multipart 폼 값은 setter로 바인딩된다.
@Getter
@Setter
public class PostCreateReqDto {

	@Schema(description = "인앱 카메라로 찍은 사진 (최대 10MB)")
	@NotNull
	private MultipartFile image;

	@Schema(example = "35.8326")
	@NotNull
	private Double lat;

	@Schema(example = "128.7540")
	@NotNull
	private Double lng;

	@Schema(description = "위치 오차(m). 50 초과면 RETRY_LOCATION", example = "12")
	@NotNull
	@PositiveOrZero
	private Double accuracy;

	@Schema(example = "1")
	@NotNull
	private Integer questionId;

	@Schema(example = "말없이 우산을 씌워 준 선배가 있었다")
	@NotBlank
	@Size(max = 100)
	private String oneLine;

	@Schema(description = "고른 태그 id, 최대 3개", example = "[1, 2]")
	@Size(max = 3)
	private List<Long> tagIds;
}
