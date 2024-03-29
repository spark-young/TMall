package com.myTmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@MapperScan("com.myTmall.mapper")
public class MyTmallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTmallApplication.class, args);
	}
}