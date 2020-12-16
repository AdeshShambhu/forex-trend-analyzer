package com.currency.forex.model;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Model to consume the Rest API Call between two dates Range
 * @author adesh
 *
 */
@Component
public class ForexDateRangeModel {

	
	
	public ForexDateRangeModel() {
	}
	@JsonProperty("rates")
	public Map<String, Map<String,Double>> rates;
	
	@JsonProperty("base")
	public String base;
	
	@JsonProperty("start_at")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startAt;
	
	@JsonProperty("end_at")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endAt;
	
	public Map<String, Map<String, Double>> getRates() {
		return rates;
	}
	public void setRates(Map<String, Map<String, Double>> rates) {
		this.rates = rates;
	}
	public Date getStartAt() {
		return startAt;
	}
	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public Date getEndAt() {
		return endAt;
	}
	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	
	
	
}
