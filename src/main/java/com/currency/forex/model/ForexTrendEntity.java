package com.currency.forex.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.currency.forex.constants.TrendStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Model to persist the Forex Trend Analysis in the DB
 * @author adesh
 *
 */

@Entity
@Table(name = "forex_trend_history", schema = "forex_schema")
@ApiModel(description = "Forex Trend Model")
@JsonIgnoreProperties(value= {"id", "trendStartDate","trendEndDate"})
public class ForexTrendEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ApiModelProperty(notes = "Requested Date")
	@Column(name = "request_date")
	@JsonProperty("request_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate requestDate;
	
	@ApiModelProperty(notes = "Base currency code")
	@Column(name = "base_currency")
	@JsonProperty("base_currency")
	private String baseCurrency;
	
	@ApiModelProperty(notes = "target currency code")
	@Column(name = "target_currency")
	@JsonProperty("target_currency")
	private String targetCurrency;
	
	@ApiModelProperty(notes = "Forex Rate for the requested date")
	@Column(name = "forex_rate")
	@JsonProperty("forex_rate")
	private double forexRate;
	
	@Column(name = "trend_start_date")
	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate trendStartDate;
	
	@Column(name = "trend_end_date")
	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate trendEndDate;
	
	@ApiModelProperty(notes = "Forex Trend : \n"
			+ "- ASCENDING  => forex rates are in ascending order since last 5 days \n "
			+ "- DESCENDING => forex rates are in descending order since last 5 days \n "
			+ "- CONSTANT   => forex rates are same since last 5 days \n "
			+ "- UNDEFINED  => forex rates are varying since last 5 days \n ")
	@Enumerated(EnumType.STRING)
	@Column(name = "trend")
	private TrendStatus trend;
	
	@Column(name = "historical_average")
	@JsonProperty("average")
	private double historicalAverage;
	
	public ForexTrendEntity() {
		super();
	}
	public ForexTrendEntity(String baseCurrency, String targetCurrency, LocalDate requestDate, double forexRate,
			LocalDate trendStartDate, LocalDate trendEndDate, double historicalAverage, TrendStatus trend) {
		super();
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.requestDate = requestDate;
		this.forexRate = forexRate;
		this.trendStartDate = trendStartDate;
		this.trendEndDate = trendEndDate;
		this.historicalAverage = historicalAverage;
		this.trend = trend;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}
	
	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate date) {
		this.requestDate = date;
	}

	public double getForexRate() {
		return forexRate;
	}

	public void setForexRate(double forexRate) {
		this.forexRate = forexRate;
	}
	public LocalDate getTrendStartDate() {
		return trendStartDate;
	}

	public void setTrendStartDate(LocalDate trendStartDate) {
		this.trendStartDate = trendStartDate;
	}

	public LocalDate getTrendEndDate() {
		return trendEndDate;
	}

	public void setTrendEndDate(LocalDate trendEndDate) {
		this.trendEndDate = trendEndDate;
	}

	public double getHistoricalAverage() {
		return historicalAverage;
	}

	public void setHistoricalAverage(double historicalAverage) {
		this.historicalAverage = historicalAverage;
	}
	@Enumerated(EnumType.STRING)
	public TrendStatus getTrend() {
		return trend;
	}

	public void setTrend(TrendStatus trend) {
		this.trend = trend;
	}

	@Override
	public String toString() {
		return "ForexTrend [baseCurrency=" + baseCurrency + ", targetCurrency=" + targetCurrency + ", requestDate="
				+ requestDate + ", forexRate=" + forexRate + ", trendStartDate=" + trendStartDate + ", trendEndDate="
				+ trendEndDate + ", historicalAverage=" + historicalAverage + ", trend=" + trend + "]";
	}



}
