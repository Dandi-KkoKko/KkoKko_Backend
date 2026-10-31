package com.kkokko.onna.dto.open;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record OpenReqDto(
		@NotNull Double lat,
		@NotNull Double lng,
		@NotNull @PositiveOrZero Double accuracy
) {
}
