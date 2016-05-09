package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class StudentRoomApplication {//extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(StudentRoomApplication.class, args);
	}
	//for the purpose of building war files 
//	@Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(StudentRoomApplication.class);
//    }
}
