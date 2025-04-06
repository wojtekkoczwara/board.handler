package com.hublocal.board.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class BoardHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardHandlerApplication.class, args);
	}

}
