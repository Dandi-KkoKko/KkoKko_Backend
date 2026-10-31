package com.kkokko.onna.dto.onboarding;

import com.kkokko.onna.global.constant.QuestionCatalog.Question;

public record QuestionResDto(int id, String text) {

	public static QuestionResDto from(Question question) {
		return new QuestionResDto(question.id(), question.text());
	}
}
