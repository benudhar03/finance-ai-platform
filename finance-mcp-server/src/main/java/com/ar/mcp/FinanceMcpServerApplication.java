package com.ar.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class FinanceMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceMcpServerApplication.class, args);
	}

}
