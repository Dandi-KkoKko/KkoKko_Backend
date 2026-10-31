package com.kkokko.onna.dto.map;

import com.kkokko.onna.global.util.BlurStage;
import java.util.List;

// 아직 열지 않은 사진 미리보기. 익명성 때문에 작성자와 좋아요 수는 아예 두지 않는다.
public record PostPreviewResDto(
		Long postId,
		BlurStage stage,
		String imageUrl,
		List<String> tags,
		boolean isTeaser,
		int distanceM
) {
}
