package com.kkokko.onna.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.kkokko.onna.domain.User;
import com.kkokko.onna.global.constant.QuestionCatalog;
import com.kkokko.onna.global.security.JwtProvider;
import com.kkokko.onna.repository.UserPreferredTagRepository;
import com.kkokko.onna.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

// 태그 id는 data.sql 임시값(1~5)을 쓴다.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OnboardingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserPreferredTagRepository userPreferredTagRepository;

	private Long userId;
	private String bearer;

	@BeforeEach
	void setUp() {
		userId = userRepository.save(User.builder()
				.email("onboarding@yu.ac.kr").password("encoded").nickname("온나").build()).getId();
		bearer = "Bearer " + jwtProvider.createAccessToken(userId);
	}

	@Test
	void 태그_사전을_id와_이름으로_준다() throws Exception {
		mockMvc.perform(get("/api/v1/tags").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").isNumber())
				.andExpect(jsonPath("$[0].name").isString());
	}

	@Test
	void 선호_태그를_저장하면_onboarded가_true() throws Exception {
		saveTags("[1, 2]").andExpect(status().isNoContent());

		mockMvc.perform(get("/api/v1/users/me").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(jsonPath("$.onboarded").value(true));
	}

	@Test
	void 다시_저장하면_기존_선호_태그를_바꾼다() throws Exception {
		saveTags("[1, 2]");

		saveTags("[3, 3]").andExpect(status().isNoContent());

		assertThat(userPreferredTagRepository.findTagIdsByUserId(userId)).containsExactly(3L);
	}

	@Test
	void 없는_태그가_있으면_TAG_NOT_FOUND() throws Exception {
		saveTags("[1, 9999]")
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("TAG_NOT_FOUND"));
	}

	@Test
	void 빈_목록은_INVALID_INPUT() throws Exception {
		saveTags("[]")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	@Test
	void 랜덤_질문은_목록에_있는_질문() throws Exception {
		String body = mockMvc.perform(get("/api/v1/questions/random").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		int id = JsonPath.read(body, "$.id");
		String text = JsonPath.read(body, "$.text");
		assertThat(QuestionCatalog.get(id)).hasValueSatisfying(q -> assertThat(q.text()).isEqualTo(text));
	}

	private ResultActions saveTags(String tagIdsJson) throws Exception {
		return mockMvc.perform(put("/api/v1/users/me/tags").header(HttpHeaders.AUTHORIZATION, bearer)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"tagIds\": %s}".formatted(tagIdsJson)));
	}
}
