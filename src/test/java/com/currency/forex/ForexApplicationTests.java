package com.currency.forex;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currency.forex.controller.ForexController;
import com.currency.forex.service.ForexService;

@SpringBootTest
class ForexApplicationTests {

	@Autowired
	private ForexController forexController;
	
	@Autowired
	private ForexService forexService;

	@Test
	void contextLoads() throws Exception{
		//Simple Test  to check if contexts are loaded.
		assertThat(forexController).isNotNull();
		assertThat(forexService).isNotNull();
	}

}
