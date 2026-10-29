package com.kkokko.closer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CloserApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloserApplication.class, args);
	}

}
