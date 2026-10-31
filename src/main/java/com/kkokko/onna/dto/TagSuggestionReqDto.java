package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagSuggestionReqDto {

	@Schema(example = "1")
	@NotNull
	private Integer questionId;

	@Schema(example = "말없이 우산을 씌워 준 선배가 있었다")
	@NotBlank
	@Size(max = 100)
	private String oneLine;
}
