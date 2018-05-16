package com.samsonan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RndApplication {

    private static final Logger log = LoggerFactory.getLogger(RndApplication.class);
    
	public static void main(String[] args) {
		SpringApplication.run(RndApplication.class, args);
	}
	
	
}
