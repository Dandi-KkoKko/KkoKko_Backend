package com.kkokko.onna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OnnaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnnaApplication.class, args);
	}

}
