# Virtual Power Plant System

### A Spring Boot REST API for a virtual power plant system that aggregates distributed power sources.

## Features

- Register List of Batteries at once.
- Search batteries by their post code range.

## Tech Stack
- Spring Boot
- Java (JDK 21)
- PostgreSQL 

## API Endpoints

**POST** `/api/batteries`

### Request Body (Example)
```json
[
  {
    "name": "Battery 1",
    "postcode": "2000",
    "wattCapacity": 200
  },
  {
    "name": "Battery 2",
    "postcode": "2100",
    "wattCapacity": 300
  }
]
```
## Query By Postcode Range

**GET** `/api/batteries?startCode=2000&endCode=7000&minWattCapacity=200&maxWattCapacity=300`

### Params 
- startCode (required) - start of post code range
- endCode (required) - end of post code range
- minWattCapacity (optional)
- maxWattCapacity (optional)


## Running the Application

### Run Application
```bash
    mvn spring-boot:run
```

### Run Tests
```bash
    mvn test 
```

### Author: Meeraj Adhikari (atifmeeraj62@gmail.com)