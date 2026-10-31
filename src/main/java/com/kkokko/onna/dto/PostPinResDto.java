package com.kkokko.onna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 지도 핀 하나. 흐린 좌표만 준다.
@Getter
@AllArgsConstructor
public class PostPinResDto {

	private Long postId;
	private double lat;
	private double lng;
}
