package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDto {

	@Schema(example = "onna@yu.ac.kr")
	@NotBlank
	private String email;

	@Schema(example = "password123")
	@NotBlank
	private String password;
}
