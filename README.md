# Forex Trend Analyzer
Forex Trend Analyzer is a spring boot microservice used to get international currency forex information and analyze the
trends of the forex rates.

The historical forex data is obtained from [ExchangeRatesAPI](https://exchangeratesapi.io/) REST APIs.
This application can be used to query the forex information for a particular day and also helps in understanding the trend of the forex for the rates for the past 5 days. The microservice gives the following information:
- Exchange rate of the requested date
- Average of the forex rates in the last 5 days from the requested date.
- Forex rates trend:
    - **DESCENDING**: If the forex rates from the last 5 days are in a downwards trend.
    - **ASCENDING**: If the forex rates from the last 5 days are in an upward trend.
    - **CONSTANT**: If the forex rates are constant for 5 last days.
    - **UNDEFINED**: If the forex rates from the last 5 days are varying.
---
**Limitations**:
- The Application only supports request dates between 2000-01-01 until yesterday (as the [ExchangeRatesAPI](https://exchangeratesapi.io/) does not provide real-time data).
- For the Average and Trend analysis, the weekend(Saturday and Sunday) days are skipped and previous week's data is included to fill the 5 days trend.
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
- Docker



## How to Execute the Microservice
The application can be executed on a JAVA IDE or in docker containers.

To run the application on docker containers, please refer to the [Forex-Trend-Analyzer-Docker](https://github.com/AdeshShambhu/forex-trend-analyzer-docker-compose) documentation.
### Prerequisite
To run this microservice, please check if you have the following tools/software in your local environment:
- Java 11 JDK
- PostgreSQL DB (docker/local setup/remote) => The application creates a separate schema for data persistence in the DB.
- Java/Spring IDE
### Execution
1. Please clone this repository into your IDE. (Or import it to your IDE, if the application is delivered as a zip file)
2. Configure the connections and ports:
   Please make sure to alter the following configurations in `src/main/resources/application.yml` if you do not want the default setup.
    - `server.port` : port for the embedded tomcat (Default 8080)
    - `db.name`: database name  (default postgres)
    - `db.host`: JDBC URL for the DB connections
    - `db.port`: JDBC URL for the DB connections
    - `db.username` : Credentials for DB
    - `db.password` : Credentials for DB
3. Run the application
4. Check if the application is running by checking the URL : `http:/localhost:<server.port>/healthcheck`

## Usage
The [Swagger UI](https://swagger.io/tools/swagger-ui/) gives an overall picture of the list of APIs supported by this application. To access Swagger UI, please
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

  
