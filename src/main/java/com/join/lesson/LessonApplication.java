package com.join.lesson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LessonApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessonApplication.class, args);
	}

}
