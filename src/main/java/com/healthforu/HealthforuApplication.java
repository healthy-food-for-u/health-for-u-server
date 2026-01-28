package com.healthforu;

import com.healthforu.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class HealthforuApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthforuApplication.class, args);
	}

}
