package com.daenerys.lndservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.daenerys.lndservice.proxy")
public class LdnServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdnServiceApplication.class, args);
	}

}
