package com.currency.forex.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.currency.forex.model.ForexTrendEntity;

@Repository
public interface ForexTrendRepository extends JpaRepository<ForexTrendEntity, Long>{
	
	@Query(value = "SELECT * FROM forex_schema.forex_trend_history u WHERE request_date = :requestDate", nativeQuery = true)
	List<ForexTrendEntity> findForexHistoryByRequestDate(@Param("requestDate") LocalDate requestDate);
	
	@Query(value = "SELECT * FROM forex_schema.forex_trend_history u "
			+ "WHERE extract(year from request_date) = :reqYear and EXTRACT(month from request_date)  = :reqMonth", nativeQuery = true)
	List<ForexTrendEntity> findForexHistoryByMonth(@Param("reqYear") int year,@Param("reqMonth") int month);
	
	@Query(value = "SELECT * FROM forex_schema.forex_trend_history u "
			+ "WHERE extract(year from request_date) = :reqYear", nativeQuery = true)
	List<ForexTrendEntity> findForexHistoryByYear(@Param("reqYear")int year);
}
