package com.personal.iphonehouse;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class IphoneHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(IphoneHouseApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Argentina/Buenos_Aires")));
	}
}
