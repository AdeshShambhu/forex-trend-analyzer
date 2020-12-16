package com.currency.forex.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class HealthCheckController {

	private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);
	
	@GetMapping("/healthcheck")
	@ResponseBody
	@ApiOperation(value = "A simple rest end point to check if microservice is alive")
	public String checkHealth() {
		logger.info("Health Check Rest End Point Checked");
		return "Hello World! I am Alive :-) " + new Date();
	}
	
}
