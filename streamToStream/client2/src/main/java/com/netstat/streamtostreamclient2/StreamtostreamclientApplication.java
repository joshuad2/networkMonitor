package com.netstat.streamtostreamclient2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StreamtostreamclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamtostreamclientApplication.class, args);
	}
}
