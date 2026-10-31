package com.kkokko.onna.dto.open;

import java.util.List;

// 열린 사진. imageUrl은 OPEN 단계(1080px) presigned URL이고 5분 뒤 만료된다.
public record OpenResDto(
		Long postId,
		String imageUrl,
		String question,
		String oneLine,
		List<String> tags,
		boolean liked
) {
}
