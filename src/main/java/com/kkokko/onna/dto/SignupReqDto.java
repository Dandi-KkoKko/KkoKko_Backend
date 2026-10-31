package com.kkokko.onna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupReqDto {

	@Schema(example = "onna@yu.ac.kr")
	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	@Schema(example = "password123")
	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

	@Schema(example = "온나")
	@NotBlank
	@Size(min = 2, max = 10)
	private String nickname;
}
