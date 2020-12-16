package com.currency.forex.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.currency.forex.model.ForexTrendEntity;
import com.currency.forex.service.ForexService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/exchange-rate/")
public class ForexController {

	private static final Logger logger = LoggerFactory.getLogger(ForexController.class);
	@Autowired
	private ForexService forexService;

	@GetMapping("{requestDate}/{baseCurrency}/{targetCurrency}")
	@ResponseBody
	@ApiOperation(value = "Gets the Forex Rate Trend for the request date", notes = "Provide a date,base and target currencies to get the trend", response = ForexTrendEntity.class)
	public ForexTrendEntity getExchangeRate(
			@ApiParam(value = "Request Date", required = true, format = "yyy-MM-dd") @PathVariable("requestDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate requestDate,
			@ApiParam(value = "3 char currency code", required = true) @PathVariable("baseCurrency") String baseCurrency,
			@ApiParam(value = "3 char currency code", required = true) @PathVariable("targetCurrency") String targetCurrency) {
		logger.info("Forex Trend Analysis requested for " + requestDate + "  with base currency:" + baseCurrency
				+ " and target currency:" + targetCurrency);
		return forexService.getForexRate(requestDate, baseCurrency, targetCurrency);
	}

	@ApiOperation(value = "Gets historic forex searches by date")
	@GetMapping("history/daily/{yyyy}/{MM}/{dd}")
	public List<ForexTrendEntity> getHistoryByDate(
			@ApiParam(value = "Year", required = true, format = "yyyy") @PathVariable("yyyy") int year,
			@ApiParam(value = "Month", required = true, format = "MM") @PathVariable("MM") int month,
			@ApiParam(value = "Day", required = true, format = "dd") @PathVariable("dd") int day) {
		logger.info("Retrieve search results for day:" + year + "-" + month + "-" + day);
		return forexService.getForexHistoryByDate(year, month, day);
	}

	@ApiOperation(value = "Gets historic forex searches by year and month")
	@GetMapping("history/monthly/{yyyy}/{MM}")
	public List<ForexTrendEntity> getHistoryByMonth(
			@ApiParam(value = "Year", required = true, format = "yyyy") @PathVariable("yyyy") int year,
			@ApiParam(value = "Month", required = true, format = "MM")  @PathVariable("MM") int month) {
		logger.info("Retrieve search results for month:" + year + "-" + month);
		return forexService.getForexHistoryByMonth(year, month);
	}

	@ApiOperation(value = "Gets historic forex searches by year")
	@GetMapping("history/yearly/{yyyy}")
	public List<ForexTrendEntity> getHistoryByYear(
			@ApiParam(value = "Year", required = true, format = "yyyy") @PathVariable("yyyy") int year) {
		logger.info("Retrieve search results for year:" + year);
		return forexService.getForexHistoryByYear(year);
	}

}