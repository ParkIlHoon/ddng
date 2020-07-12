# DDNG
A personal project begins July 2020 and based on MSA(Micro Service Architecture).
![Project Architecture](https://user-images.githubusercontent.com/32320659/87249065-e89e0f80-c497-11ea-9c10-5a60f1d59c6f.png)

## Technics
1. Spring Boot
2. Spring JPA
3. Spring Cloud Eureka
4. Spring Cloud Zuul


## Services
### gate-way
* application name : ddng-gate-way
* port : 8090
* routes
  * /api/user/** : ddng-user-api
  * /api/customer/** : ddng-customer-api
  * /api/stock/** : ddng-stock-api
  * /api/sale/** : ddng-sale-api
  * /api/schedule/** : ddng-schedule-api
  * /api/statistic/** : ddng-statistic-api
### eureka-server
* application name : ddng-eureka-server
* port : 8761
### web-site
### user-api
* application name : ddng-user-api
* port : 8000
### customer-api
* application name : ddng-customer-api
* port : 8005
### stock-api
* application name : ddng-stock-api
* port : 8010
### sale-api
* application name : ddng-sale-api
* port : 8015
### schedule-api
* application name : ddng-schedule-api
* port : 8020
### statistic-api
* application name : ddng-statistic-api
* port : 8025
