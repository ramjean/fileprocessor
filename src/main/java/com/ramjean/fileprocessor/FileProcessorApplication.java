package com.ramjean.fileprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@SpringBootApplication
public class FileProcessorApplication {
	private static final Logger logger = LoggerFactory.getLogger(FileProcessorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(FileProcessorApplication.class, args);
		logger.info("######## All Set ######## ");
	}

}
