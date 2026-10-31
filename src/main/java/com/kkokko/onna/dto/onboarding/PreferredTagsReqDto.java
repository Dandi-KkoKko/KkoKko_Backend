package com.kkokko.onna.dto.onboarding;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PreferredTagsReqDto(
		@NotEmpty List<@NotNull Long> tagIds
) {
}
