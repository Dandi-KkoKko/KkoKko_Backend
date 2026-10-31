package com.kkokko.onna.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kkokko.onna.global.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

// 프론트가 Swagger로 붙여 볼 더미 API가 약속한 응답·에러 형식을 지키는지 확인한다. 실제 구현으로 바꾸면 지운다.
@SpringBootTest
@AutoConfigureMockMvc
class DummyApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtProvider jwtProvider;

	private String bearer;

	@BeforeEach
	void setUp() {
		bearer = "Bearer " + jwtProvider.createAccessToken(1L);
	}

	@Test
	void 업로드하면_201과_postId() throws Exception {
		upload(uploadRequest("12", "한 줄")).andExpect(status().isCreated())
				.andExpect(jsonPath("$.postId").value(1))
				.andExpect(jsonPath("$.publishAt").isString());
	}

	@Test
	void 위치_오차가_크면_RETRY_LOCATION() throws Exception {
		upload(uploadRequest("80", "한 줄")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("RETRY_LOCATION"));
	}

	@Test
	void 한_줄이_face면_FACE_DETECTED() throws Exception {
		upload(uploadRequest("12", "face")).andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.code").value("FACE_DETECTED"));
	}

	@Test
	void 사진이_없으면_INVALID_INPUT() throws Exception {
		upload(multipart("/api/v1/posts").param("lat", "35.83").param("lng", "128.75")
				.param("accuracy", "12").param("questionId", "1").param("oneLine", "한 줄"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	@Test
	void 태그를_4개_고르면_INVALID_INPUT() throws Exception {
		upload(uploadRequest("12", "한 줄").param("tagIds", "1", "2", "3", "4"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	@Test
	void 내_기록을_준다() throws Exception {
		mockMvc.perform(get("/api/v1/users/me/posts").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].postId").isNumber())
				.andExpect(jsonPath("$[0].imageUrl").isString());
	}

	@Test
	void 삭제는_204_더미_id로_404와_403() throws Exception {
		mockMvc.perform(delete("/api/v1/posts/1").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(status().isNoContent());
		mockMvc.perform(delete("/api/v1/posts/404").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("POST_NOT_FOUND"));
		mockMvc.perform(delete("/api/v1/posts/403").header(HttpHeaders.AUTHORIZATION, bearer))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.code").value("NOT_OWNER"));
	}

	@Test
	void 태그_후보를_준다() throws Exception {
		mockMvc.perform(post("/api/v1/posts/tag-suggestions").header(HttpHeaders.AUTHORIZATION, bearer)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"questionId\":1,\"oneLine\":\"한 줄\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tags").isArray());
	}

	@Test
	void 해시태그_검색은_가까운순() throws Exception {
		mockMvc.perform(get("/api/v1/search/hashtags").header(HttpHeaders.AUTHORIZATION, bearer)
						.param("keyword", "#정문").param("lat", "35.8326").param("lng", "128.7540"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("정문앞"))
				.andExpect(jsonPath("$[0].distanceM").value(0))
				.andExpect(jsonPath("$[0].posts").isArray());
	}

	@Test
	void 해시태그_검색어가_샵뿐이면_INVALID_INPUT() throws Exception {
		mockMvc.perform(get("/api/v1/search/hashtags").header(HttpHeaders.AUTHORIZATION, bearer)
						.param("keyword", "#").param("lat", "35.8326").param("lng", "128.7540"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	@Test
	void 해시태그_검색에_위치가_없으면_INVALID_INPUT() throws Exception {
		mockMvc.perform(get("/api/v1/search/hashtags").header(HttpHeaders.AUTHORIZATION, bearer)
						.param("keyword", "정문"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	private MockHttpServletRequestBuilder uploadRequest(String accuracy, String oneLine) {
		MockMultipartFile image = new MockMultipartFile("image", "photo.jpg", "image/jpeg", new byte[] {1, 2, 3});
		return multipart("/api/v1/posts").file(image)
				.param("lat", "35.8326").param("lng", "128.7540").param("accuracy", accuracy)
				.param("questionId", "1").param("oneLine", oneLine).param("tagIds", "1", "2");
	}

	private ResultActions upload(MockHttpServletRequestBuilder request) throws Exception {
		return mockMvc.perform(request.header(HttpHeaders.AUTHORIZATION, bearer));
	}
}
