# Forex Trend Analyzer
Forex Trend Analyzer is a spring boot microservice used to get international currency forex information and analyze the 
trends of the forex rates.

The historical forex data is obtained from ExchangeRatesAPI() REST APIs. 
Our application can be used to query the forex information for a particular day and also helps in understanding the 
trend of the forex for the rates for past 5 days. The microservice gives following information:
- Exchange rate of the requested date
- Average of the forex rates in last 5 days from the requested date.
- Forex rates trend:
  - **DESCENDING**: If the forex rates from last 5 days are in a downwards trend.
  - **ASCENDING**: If the forex rates from last 5 days are in a upward trend.
  - **CONSTANT**: If the forex rates are constant for 5 last days.
  - **UNDEFINED**: If the forex rates from last 5 days are varying.
---
**Limitations**:
- The Application only supports request date between 2000-01-01 until yesterday (As the ExchangeRatesAPI.io does not provide real time data). 
- For the Average and Trend analysis, the weekend(Saturday and Sunday) days are skipped and previous weeks data is included to fill the 5 days trend
- The forex rates are not available on bank holidays, hence if the 5 days trend window has a public holiday in it, the data is skipped to avoid bias.   
---
## Technology Stack
- Spring Boot 2.4.1
- Java 11
- PostgreSQL 13.1
- Hibernate, Spring JPA
- Apache Maven 
- Swagger UI
- Flyway
- *TODO: Docker* 



## How to Execute the Microservice
### Prerequisite
To run this microservices, please check if you have following tools/software in your local environment:
- Java 11 JDK
- PostgreSQL DB (docker/local setup/remote) => The application creates a separate schema for data persistence in the DB. 
- Java/Spring IDE
### Execution
1. Please clone this repository into your IDE. (Or import it to your IDE, if the application is delivered as a zip file)
2. Configure the connections and ports:
    Please make sure to alter the following configurations in `src/main/resources/application.yml` if you do not want the default setup.
   - `server.port`: port for the embedded tomcat (Default 8080)
   - `datasource.url` and `flyway.url` : JDBC URL for the DB connections
   - `datasource.user` and `flyway.url` : Credentials for DB
   - `datasource.password` and `flyway.password` : Credentials for DB
3. Run the application
4. Check if the application is running by checking the URL : `http:/localhost:<server.port>/healthcheck`

## Usage
The Swagger UI gives an overall picture of the list of APIs supported this application. To access Swagger UI, please 
go the URL : `http:/localhost:<server.port>/swagger-ui.html`. 

The microservices support following APIs:

|                            URL                            |                               Description                              |
|:---------------------------------------------------------:|:----------------------------------------------------------------------:|
| /api/exchange-rate/{date}/{baseCurrency}/{targetCurrency} | Trend Analysis for a requested date between 2000-01-01 until yesterday |
| /api/exchange-rate/history/daily/{yyyy}/{MM}/{dd}         | Get the historic forex searches by date                                |
| /api/exchange-rate/history/monthly/{yyyy}/{MM}            | Get the historic forex searches by month                               |
| /api/exchange-rate/history/yearly/{yyyy}                  | Get the historic forex searches by year                                |
| /healthcheck                                              | Simple health check end point                                          |
| /swagger-ui.html                                          | Swagger UI                                                             |

You can execute your queries in Swagger UI/Browsers/PostMan.

  