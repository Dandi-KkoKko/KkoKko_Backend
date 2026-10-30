package com.kkokko.onna.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

class BlurPropertiesTest {

	// s1-m 같은 키가 s1M 필드에 붙는지 확인한다. 숫자와 한 글자 단위가 섞인 이름이라 바인딩이 어긋나기 쉽다.
	@Test
	void 설정_키가_필드에_바인딩된다() {
		MapConfigurationPropertySource source = new MapConfigurationPropertySource(Map.of(
				"app.blur.s1-m", "150",
				"app.blur.s2-m", "60",
				"app.blur.open-m", "25",
				"app.blur.max-accuracy-m", "50"
		));

		BlurProperties props = new Binder(source).bind("app.blur", BlurProperties.class).get();

		assertThat(props).isEqualTo(new BlurProperties(150, 60, 25, 50));
	}
}
