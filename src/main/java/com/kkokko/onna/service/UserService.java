package com.kkokko.onna.service;

import com.kkokko.onna.domain.User;
import com.kkokko.onna.dto.UserResDto;
import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import com.kkokko.onna.repository.UserPreferredTagRepository;
import com.kkokko.onna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final UserPreferredTagRepository userPreferredTagRepository;

	// 토큰은 맞는데 사용자가 없으면(DB 초기화 등) 다시 로그인하게 INVALID_TOKEN으로 응답한다.
	public UserResDto getMe(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
		return UserResDto.from(user, userPreferredTagRepository.existsByUserId(userId));
	}
}
