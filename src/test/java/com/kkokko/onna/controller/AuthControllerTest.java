package com.kkokko.onna.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

	private static final String EMAIL = "test@yu.ac.kr";
	private static final String PASSWORD = "password123";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void 가입하면_201과_userId() throws Exception {
		signup(EMAIL, PASSWORD, "온나")
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").isNumber());
	}

	@Test
	void 같은_이메일로_가입하면_EMAIL_DUPLICATED() throws Exception {
		signup(EMAIL, PASSWORD, "온나");

		signup(EMAIL, PASSWORD, "온나2")
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.code").value("EMAIL_DUPLICATED"));
	}

	@Test
	void 비밀번호가_8자보다_짧으면_INVALID_INPUT() throws Exception {
		signup(EMAIL, "short", "온나")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	@Test
	void JSON이_깨지면_INVALID_INPUT() throws Exception {
		mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content("{"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
	}

	@Test
	void 비밀번호가_틀리면_LOGIN_FAILED() throws Exception {
		signup(EMAIL, PASSWORD, "온나");

		login(EMAIL, "wrongpass1")
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("LOGIN_FAILED"));
	}

	@Test
	void 없는_이메일도_LOGIN_FAILED() throws Exception {
		login("none@yu.ac.kr", PASSWORD)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("LOGIN_FAILED"));
	}

	@Test
	void 로그인한_Access로_내_정보를_본다() throws Exception {
		signup(EMAIL, PASSWORD, "온나");
		String access = tokens().get("accessToken").asText();

		mockMvc.perform(get("/api/v1/users/me").header(HttpHeaders.AUTHORIZATION, "Bearer " + access))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value(EMAIL))
				.andExpect(jsonPath("$.nickname").value("온나"))
				.andExpect(jsonPath("$.onboarded").value(false));
	}

	@Test
	void Refresh로_재발급하면_예전_Refresh는_못_쓴다() throws Exception {
		signup(EMAIL, PASSWORD, "온나");
		String oldRefresh = tokens().get("refreshToken").asText();
		// 같은 초에 만들면 토큰 문자열이 같아져서 재사용 판정을 확인할 수 없다.
		Thread.sleep(1000);

		refresh(oldRefresh)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").isString());
		refresh(oldRefresh)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
	}

	@Test
	void Access로는_재발급할_수_없다() throws Exception {
		signup(EMAIL, PASSWORD, "온나");
		String access = tokens().get("accessToken").asText();

		refresh(access)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
	}

	private ResultActions signup(String email, String password, String nickname) throws Exception {
		return postJson("/api/v1/auth/signup",
				"{\"email\":\"%s\",\"password\":\"%s\",\"nickname\":\"%s\"}".formatted(email, password, nickname));
	}

	private ResultActions login(String email, String password) throws Exception {
		return postJson("/api/v1/auth/login", "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password));
	}

	private ResultActions refresh(String refreshToken) throws Exception {
		return postJson("/api/v1/auth/refresh", "{\"refreshToken\":\"%s\"}".formatted(refreshToken));
	}

	private JsonNode tokens() throws Exception {
		String body = login(EMAIL, PASSWORD).andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(body);
	}

	private ResultActions postJson(String url, String json) throws Exception {
		return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
	}
}
