package com.currency.forex.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.currency.forex.constants.TrendStatus;
import com.currency.forex.exception.ForexRequestException;
import com.currency.forex.model.ForexDateRangeModel;
import com.currency.forex.model.ForexTrendEntity;
import com.currency.forex.repository.ForexTrendRepository;


/**
 * Service Layer for Forex Controller This class contains the Business Logic for
 * this application.
 * 
 * @author adesh
 *
 */
@Service
public class ForexService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	ForexDateRangeModel forexConsume;
	
	@Autowired
	private ForexTrendRepository trendRepository;
	// Base URL for Forex API
	@Value("${forex-api-url}")
	private String forexURL;

	// Number of days for trend analysis
	@Value("${forex-trend-days}")
	private int forexTrendDays;
	
	private static final Logger logger = LoggerFactory.getLogger(ForexService.class);

	public ForexTrendEntity getForexRate(LocalDate requestDate, String baseCurrency, String targetCurrency){
		
		ForexTrendEntity trend = new ForexTrendEntity();
		
		try {	
			trend = getTrendDateRanges(requestDate);
			//Set the requested parameters into Pojo
			trend.setRequestDate(requestDate);
			trend.setBaseCurrency(baseCurrency);
			trend.setTargetCurrency(targetCurrency);
			
			if(requestDate.isBefore( LocalDate.parse("2000-01-01"))) {
				logger.error("Invalid reqest date");
				throw new ForexRequestException("The Requested Date is earlier than 2000-01-01. "
						+ "This application can only fetch forex data until 2000-01-01 until yesterday");
			}else if(requestDate.isEqual(LocalDate.now()) || requestDate.isAfter(LocalDate.now())) {
				logger.error("Invalid request date");
				throw new ForexRequestException("The Requested Date is today or in future, the data is not yet available. "
						+ "This application can only fetch forex data until 2000-01-01 until yesterday");
			}
		
			logger.debug("forex URL:"+ forexURL + "/history?start_at=" + trend.getTrendStartDate() + "&end_at="
					+ trend.getTrendEndDate() + "&base=" + baseCurrency + "&symbols=" + targetCurrency);
			
			// Rest Call to fetch forex data between start and stop dates
			forexConsume = restTemplate.getForObject(forexURL + "/history?start_at=" + trend.getTrendStartDate() + "&end_at="
									+ trend.getTrendEndDate() + "&base=" + baseCurrency + "&symbols=" + targetCurrency,
							ForexDateRangeModel.class);
			
			
			/*
			 * Set the forexRate on the requested date
			 * If the requested Date is a weekend, Friday's forex data will be displayed
			 * If requested Date is a public holiday, get the last possible forex data  
			 */
			if(forexConsume.getRates().size() > 0 && !forexConsume.getRates().containsKey(trend.getTrendEndDate().toString())) {
				logger.info("Request Date is an public holiday, Hence fetching the previous available result");
				//Forex Rate not available for the requested date ( Maybe Public holiday)
				//Get the latest forex for the requested date
				for(int i = 1; i < forexTrendDays; i++) {
					LocalDate nextAvailableDate = trend.getTrendEndDate().minusDays(i);
					if(forexConsume.getRates().containsKey(nextAvailableDate.toString())) {
						trend.setForexRate(forexConsume.getRates().get(nextAvailableDate.toString()).get(targetCurrency));
						break;
					}
				}
			}
			else if(forexConsume.getRates().size() > 0) {
				//Map always contains the target currency (else error is caught in Rest call)
				trend.setForexRate(forexConsume.getRates().get(trend.getTrendEndDate().toString()).get(targetCurrency));
			}
			//No data collected
			else if(forexConsume.getRates().size() == 0) {
				logger.info("No Data could be fetched for the given parameters");
				throw new ForexRequestException("No Data Found for the requested date.");
			}
			
			trend.setHistoricalAverage(calculateHistoricalAverage(forexConsume.getRates(), targetCurrency));
			trend.setTrend(trendAnalyzer(forexConsume.getRates(), targetCurrency,trend.getTrendStartDate(),trend.getTrendEndDate()));
			logger.debug("Result :" + trend.toString());
			
			}catch(HttpClientErrorException e) {
				if(e.getMessage().contains("Symbols")) {
					logger.error("Currency not supported for the request date");
					throw new ForexRequestException(" Currency is not supported for the requested date, please check "+forexURL
							+ " documentation to check the supported currencies ");
				}
			}
		
		//Since No errors are caught until now => Rest call was successful
		//Persist the analysed data to DB.
		logger.info("Persisting the data in the table");
		this.trendRepository.save(trend);
		
		return trend;

	}
	
	/**
	 * method to retrieve search results by date
	 * @param year
	 * @param month
	 * @param day
	 * @return 
	 */
	public List<ForexTrendEntity> getForexHistoryByDate(int year, int month, int day) {
		return this.trendRepository.findForexHistoryByRequestDate(LocalDate.of(year, month, day));
	}
	/**
	 * method to retrieve search results by year and month
	 * @param year
	 * @param month
	 * @return
	 */
	public List<ForexTrendEntity> getForexHistoryByMonth(int year, int month) {
		return this.trendRepository.findForexHistoryByMonth(year, month);
	}
	/**
	 * method to retrieve search results by year
	 * @param year
	 * @return
	 */
	public List<ForexTrendEntity> getForexHistoryByYear(int year) {
		return this.trendRepository.findForexHistoryByYear(year);
	}
	
	/**
	 * Method to analyze the trend of the currency exchange in last N days 
	 * @param rates Map of <Forex Date, Map of Forex Rates<Target Currency, Rate >>
	 * @param targetCurrency
	 * @param endDate 
	 * @param startDate 
	 * @return
	 */
	public TrendStatus trendAnalyzer(Map<String, Map<String, Double>> rates, String targetCurrency, LocalDate startDate, LocalDate endDate) {
		//Set all flags to false
		boolean isDescending = true;
		boolean isAscending = true;
		boolean isConstant = true;
		//Convert unordered list of data in map to ordered list
		List<Double> forexHistoricalRates = new ArrayList<Double>();
		int dayOffset = 0;
		while( startDate.plusDays(dayOffset).isBefore(endDate) || startDate.plusDays(dayOffset).isEqual(endDate)) {
			String rateDate = startDate.plusDays(dayOffset).toString();
			//Check if data exist for the date
			if(rates.containsKey(rateDate))
				forexHistoricalRates.add(rates.get(rateDate).get(targetCurrency));
			dayOffset++;
		}
		if(forexHistoricalRates.size()==0) {
			return TrendStatus.UNDEFINED;
		}
		
		logger.debug("Forex Rates to analyze the trend=> "+forexHistoricalRates.toString());
		//Check the trend => O(N)
		for (int i = 1; i < forexHistoricalRates.size() && (isDescending || isAscending || isConstant); i++) {
		    if(Double.compare(forexHistoricalRates.get(i), forexHistoricalRates.get(i-1)) <= 0)
		    	isAscending = false;
		    if(Double.compare(forexHistoricalRates.get(i), forexHistoricalRates.get(i-1)) >= 0)
		    	isDescending = false;
		    if(Double.compare(forexHistoricalRates.get(i), forexHistoricalRates.get(i-1)) != 0)
		    	isConstant = false;
		}
		if(isAscending) 
			return TrendStatus.ASCENDING;
		else if(isDescending)
			return TrendStatus.DESCENDING;
		else if(isConstant)
			return TrendStatus.CONSTANT;
		else
			return TrendStatus.UNDEFINED;
	}

	/**
	 * Calculate the average of the forex historical data.
	 * @param rates
	 * @param targetCurrency
	 * @return
	 */
	private double calculateHistoricalAverage(Map<String, Map<String, Double>> rates, String targetCurrency) {
		double sum = 0.0;
		for(String rateDate: rates.keySet()) {
			sum += rates.get(rateDate).get(targetCurrency);
		}
		return sum/rates.keySet().size();
	}
	
	/**
	 * method to adjust the start and end dates for forex trends to avoid weekend data.
	 * @param requestDate
	 * @return
	 */
	private ForexTrendEntity getTrendDateRanges(LocalDate requestDate) {
		ForexTrendEntity trend = new ForexTrendEntity();
		// End Date for Forex Trend Analysis
		LocalDate endDate = requestDate;

		// if requested Date is a Sunday or Saturday, reduce the end day to Friday.
		if (endDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			endDate = endDate.minusDays(2);
		else if (endDate.getDayOfWeek().equals(DayOfWeek.SATURDAY))
			endDate = endDate.minusDays(1);

		// Total number of weekends between start date and end date
		int numberOfWeekends = getTotalNumberOfWeekends(endDate, forexTrendDays);

		// Start Date for Forex Trend Analysis
		LocalDate startDate = endDate.minusDays(forexTrendDays + numberOfWeekends - 1);
		trend.setTrendStartDate(startDate);
		trend.setTrendEndDate(endDate);
		logger.debug("Trend Analysis Start Date:" +startDate +" => "+startDate.getDayOfWeek());
		logger.debug("Trend Analysis End Date:" + endDate +" => "+endDate.getDayOfWeek());
		//System.out.println("Start Date:=>" + endDate.toString() + ", Day=>" + endDate.getDayOfWeek());
		//System.out.println("End Date:=>" + endDate.minusDays(forexTrendDays + numberOfWeekends - 1).toString()
		//		+ ", Day=>" + endDate.minusDays(forexTrendDays + numberOfWeekends - 1).getDayOfWeek());
		//System.out.println("Number of weekends between start date and end date:" + numberOfWeekends);
		logger.debug("Number of weekends between start and stop date:" + numberOfWeekends);

		return trend;

	}

	/**
	 * Iterate between start date and end date backwards and count the number of
	 * weekends in the range. O(N) => There might be a better solution for this.
	 * 
	 * @param startDate
	 * @param minusDays
	 * @return
	 */
	private int getTotalNumberOfWeekends(LocalDate endDate, int minusDays) {
		int numberOfWeekends = 0;
		for (int i = 1; i < minusDays; i++) {
			if (endDate.minusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY
					|| endDate.minusDays(i).getDayOfWeek() == DayOfWeek.SUNDAY)
				numberOfWeekends++;
		}
		return numberOfWeekends;
	}

}
