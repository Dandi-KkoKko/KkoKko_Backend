package com.kkokko.onna.dto;

import com.kkokko.onna.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResDto {

	private Long userId;

	public static SignupResDto from(User user) {
		return new SignupResDto(user.getId());
	}
}
