package com.kkokko.onna.global.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kkokko.onna.global.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

	// 컨트롤러가 없는 경로. 인증을 통과하면 404, 막히면 401이 나온다.
	private static final String PROTECTED = "/api/v1/users/me";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtProvider jwtProvider;

	@Test
	void 토큰이_없으면_UNAUTHORIZED() throws Exception {
		mockMvc.perform(get(PROTECTED))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
	}

	@Test
	void 잘못된_토큰이면_INVALID_TOKEN() throws Exception {
		mockMvc.perform(get(PROTECTED).header(HttpHeaders.AUTHORIZATION, "Bearer wrong"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
	}

	@Test
	void Refresh_토큰으로는_인증되지_않는다() throws Exception {
		String refresh = jwtProvider.createRefreshToken(1L);

		mockMvc.perform(get(PROTECTED).header(HttpHeaders.AUTHORIZATION, "Bearer " + refresh))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
	}

	@Test
	void Access_토큰이면_인증을_통과한다() throws Exception {
		String access = jwtProvider.createAccessToken(1L);

		mockMvc.perform(get(PROTECTED).header(HttpHeaders.AUTHORIZATION, "Bearer " + access))
				.andExpect(status().isNotFound());
	}

	@Test
	void Swagger는_토큰_없이_열린다() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk());
	}

	@Test
	void 허용한_출처의_CORS_사전_요청을_통과시킨다() throws Exception {
		mockMvc.perform(options(PROTECTED)
						.header(HttpHeaders.ORIGIN, "http://localhost:5173")
						.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173"));
	}
}
