package ru.hse.diplom.cafe_recommend_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CafeRecommendBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeRecommendBackendApplication.class, args);
	}

}
