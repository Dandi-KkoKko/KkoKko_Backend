package com.kkokko.onna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResDto {

	private String accessToken;
	private String refreshToken;
}
