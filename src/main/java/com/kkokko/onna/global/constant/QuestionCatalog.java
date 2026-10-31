package com.kkokko.onna.global.constant;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

// 남기기 질문 목록. 문구만 바뀌고 따로 관리할 화면이 없어서 테이블 대신 코드 상수로 둔다.
// post.question_id가 이 id를 참조하므로 한 번 쓴 id는 다른 질문으로 바꾸지 않는다.
public final class QuestionCatalog {

	// TODO 임시값: 기획에서 질문 목록을 받으면 교체한다.
	private static final List<Question> QUESTIONS = List.of(
			new Question(1, "여기서 누가 챙겨줬어?"),
			new Question(2, "말없이 마음이 놓였던 순간은?"),
			new Question(3, "여기 오면 늘 생각나는 사람은?"),
			new Question(4, "처음 왔는데도 편했던 이유는?"),
			new Question(5, "무뚝뚝했지만 고마웠던 한마디는?"));

	private QuestionCatalog() {
	}

	public static Question random() {
		return QUESTIONS.get(ThreadLocalRandom.current().nextInt(QUESTIONS.size()));
	}

	public static Optional<Question> get(int id) {
		return QUESTIONS.stream().filter(q -> q.id() == id).findFirst();
	}

	public record Question(int id, String text) {
	}
}
