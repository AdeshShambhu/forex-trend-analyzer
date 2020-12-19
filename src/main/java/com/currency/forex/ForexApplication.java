package com.currency.forex;

import java.util.Collection;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * Spring Boot Microservice to query historical currency forex rates.
 * @author adesh
 *
 */
@SpringBootApplication
@EnableSwagger2
public class ForexApplication {
	//Create a bean for rest template
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.currency.forex"))
				.build()
				.apiInfo(apiDetails());
	}
	
	private ApiInfo apiDetails() {
		return new ApiInfo("Forex Trend Analyzer", "A Spring Boot Microservice to fetch and analyze " +
				"the currency forex rates. \n Forex Information is obtained from https://exchangeratesapi.io/." +
				"\n The code repository can be found at: https://github.com/AdeshShambhu/forex-trend-analyzer",
				"1.0"
				, null, null, null, null, Collections.emptyList());

	}
	public static void main(String[] args) {
		SpringApplication.run(ForexApplication.class, args);
	}

}
