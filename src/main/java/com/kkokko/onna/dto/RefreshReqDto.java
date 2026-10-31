package com.kkokko.onna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshReqDto {

	@NotBlank
	private String refreshToken;
}
