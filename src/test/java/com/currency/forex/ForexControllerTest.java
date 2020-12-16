package com.currency.forex;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.currency.forex.constants.TrendStatus;
import com.currency.forex.model.ForexModel;
import com.currency.forex.model.ForexTrendEntity;
import com.currency.forex.service.ForexService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ForexControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	// Number of days for trend analysis
	@Value("${forex-trend-days}")
	private int forexTrendDays;
	
	@Autowired
	private ForexService forexService;

	/**
	 * To test with forex rate for a specific date (2010-01-12)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForexRateByDate() throws Exception {
		// Get Forex date for a specific date
		double forexRate = this.restTemplate
				.getForObject("https://api.exchangeratesapi.io/2010-01-12?base=EUR&symbols=INR", ForexModel.class)
				.getRates().get("INR");
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/exchange-rate/2010-01-12/EUR/INR",
				ForexTrendEntity.class).getForexRate()).isEqualByComparingTo(forexRate);
	}

	/**
	 * To test with forex average for a specific week (11-12-2020 to 07-12-2020)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForexAverageByWeek() throws Exception {
		if (forexTrendDays == 5) {
			// Get Forex date for a specific week
			double forexRate1 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-11?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate2 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-10?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate3 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-09?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate4 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-08?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate5 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-07?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double weekAvg = (forexRate1 + forexRate2 + forexRate3 + forexRate4 + forexRate5) / 5;

			assertThat(
					this.restTemplate.getForObject("http://localhost:" + port + "/api/exchange-rate/2020-12-11/EUR/INR",
							ForexTrendEntity.class).getHistoricalAverage()).isEqualByComparingTo(weekAvg);
		}
	}

	/**
	 * To test with forex average for a specific week including weekend (15-12-2020
	 * to 09-12-2020) - Two weekends
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForexAverageByWeekWithWeekends() throws Exception {
		if (forexTrendDays == 5) {
			// Get Forex date for a specific week
			double forexRate1 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-15?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate2 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-14?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate3 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-11?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate4 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-10?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double forexRate5 = this.restTemplate
					.getForObject("https://api.exchangeratesapi.io/2020-12-09?base=EUR&symbols=INR", ForexModel.class)
					.getRates().get("INR");
			double weekAvg = (forexRate1 + forexRate2 + forexRate3 + forexRate4 + forexRate5) / 5;

			assertThat(
					this.restTemplate.getForObject("http://localhost:" + port + "/api/exchange-rate/2020-12-15/EUR/INR",
							ForexTrendEntity.class).getHistoricalAverage()).isEqualByComparingTo(weekAvg);
		}
	}
	
	/**
	 * Test if the trend analysis works correctly - Descending
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTrendAnalysisDescending() throws Exception {
		Map<String,Map<String,Double>> rates = new HashMap<String, Map<String,Double>>();
		Map<String,Double> ratePerDay1 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay2 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay3 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay4 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay5 =  new HashMap<String, Double>();
		ratePerDay1.put("USD",1.0);
		rates.put("2020-12-15",ratePerDay1);
		ratePerDay2.put("USD",2.0);
		rates.put("2020-12-14",ratePerDay2);
		ratePerDay3.put("USD",3.0);
		rates.put("2020-12-13",ratePerDay3);
		ratePerDay4.put("USD",4.0);
		rates.put("2020-12-12",ratePerDay4);
		ratePerDay5.put("USD",5.0);
		rates.put("2020-12-11",ratePerDay5);
		
		String targetCurrency="USD";
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");

		assertThat(forexService.trendAnalyzer(rates, targetCurrency, startDate, endDate)).isEqualTo(TrendStatus.DESCENDING);
	}
	
	/**
	 * Test if the trend analysis works correctly - Ascending
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTrendAnalysisAscending() throws Exception {
		Map<String,Map<String,Double>> rates = new HashMap<String, Map<String,Double>>();
		Map<String,Double> ratePerDay1 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay2 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay3 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay4 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay5 =  new HashMap<String, Double>();
		ratePerDay1.put("USD",15.0);
		rates.put("2020-12-15",ratePerDay1);
		ratePerDay2.put("USD",10.0);
		rates.put("2020-12-14",ratePerDay2);
		ratePerDay3.put("USD",7.0);
		rates.put("2020-12-13",ratePerDay3);
		ratePerDay4.put("USD",6.0);
		rates.put("2020-12-12",ratePerDay4);
		ratePerDay5.put("USD",5.0);
		rates.put("2020-12-11",ratePerDay5);
		
		String targetCurrency="USD";
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		System.out.println(rates.toString());
		assertThat(forexService.trendAnalyzer(rates, targetCurrency, startDate, endDate)).isEqualTo(TrendStatus.ASCENDING);
	}
	
	/**
	 * Test if the trend analysis works correctly - Constant
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTrendAnalysisConstant() throws Exception {
		Map<String,Map<String,Double>> rates = new HashMap<String, Map<String,Double>>();
		Map<String,Double> ratePerDay1 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay2 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay3 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay4 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay5 =  new HashMap<String, Double>();
		
		ratePerDay1.put("USD",15.0);
		rates.put("2020-12-15",ratePerDay1);
		ratePerDay2.put("USD",15.0);
		rates.put("2020-12-14",ratePerDay2);
		ratePerDay3.put("USD",15.0);
		rates.put("2020-12-13",ratePerDay3);
		ratePerDay4.put("USD",15.0);
		rates.put("2020-12-12",ratePerDay4);
		ratePerDay5.put("USD",15.0);
		rates.put("2020-12-11",ratePerDay5);
		
		String targetCurrency="USD";
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");

		assertThat(forexService.trendAnalyzer(rates, targetCurrency, startDate, endDate)).isEqualTo(TrendStatus.CONSTANT);
	}
	
	/**
	 * Test if the trend analysis works correctly - Undefined
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTrendAnalysisUndefined() throws Exception {
		Map<String,Map<String,Double>> rates = new HashMap<String, Map<String,Double>>();
		Map<String,Double> ratePerDay1 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay2 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay3 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay4 =  new HashMap<String, Double>();
		Map<String,Double> ratePerDay5 =  new HashMap<String, Double>();
		ratePerDay1.put("USD",15.0);
		rates.put("2020-12-15",ratePerDay1);
		ratePerDay2.put("USD",15.0);
		rates.put("2020-12-14",ratePerDay2);
		ratePerDay3.put("USD",13.0);
		rates.put("2020-12-13",ratePerDay3);
		ratePerDay4.put("USD",15.0);
		rates.put("2020-12-12",ratePerDay4);
		ratePerDay5.put("USD",25.0);
		rates.put("2020-12-11",ratePerDay5);
		
		String targetCurrency="USD";
		LocalDate startDate = LocalDate.parse("2020-12-15");
		LocalDate endDate = LocalDate.parse("2020-12-11");

		assertThat(forexService.trendAnalyzer(rates, targetCurrency, startDate, endDate)).isEqualTo(TrendStatus.UNDEFINED);
	}

}
