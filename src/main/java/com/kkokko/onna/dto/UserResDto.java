package com.kkokko.onna.dto;

import com.kkokko.onna.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResDto {

	private Long userId;
	private String email;
	private String nickname;

	@Schema(description = "선호 태그를 골랐는지. false면 온보딩 화면으로 보낸다")
	private boolean onboarded;

	public static UserResDto from(User user, boolean onboarded) {
		return new UserResDto(user.getId(), user.getEmail(), user.getNickname(), onboarded);
	}
}
