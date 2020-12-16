package com.currency.forex.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Forex Model to consume REST API of one request date
 * @author adesh
 *
 */

public class ForexModel {

	private Map<String, Double> rates;
	private String base;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;


	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
