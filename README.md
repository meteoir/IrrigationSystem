# Irrigation system
`Service aimed to prepare and execute irrigation policies and collect reports based on executions. It could be used within
MSA as a pod responsible for managing the irrigation for predefined plot of lands.`

`Also it could be used to deduct better irrigation values on existing setup to reduce costs and select the best configuration
based on daily irrigation reports. Feel free to refactor it what ever you want`.

# API:
` You can find example requests in file test-api.http and run it from Intellij IDEA directly`

There are few controllers which processing the incoming requests:
* UserController serves the user management related stuff
* IrrigationController - allows users prepare the irrigation configs
* JobController - run the batch job via REST-api
* IrrigationReportingJob - prepares the report with aggregated data for each plot with amount of water
* AuthController - issues the JWT token to registered users (supposed that authentication\authorization
  will be implemented on demand)
  
# Database:
please use attached docker-compose to run PostgreSQL db locally, migrations performed with Flyway.

# CI-CD
git-lab pipelines could be maintained through attached ci config

# Features:
* Basic requirements implemented
* CRUD
* Reporting (batch job performs CSV report exporting into S3 storage, just provide your credentials data into configuration)
* Spring, JPA, JPA Criteria API, Hibernate, Spring Batch, Security, Actuator, Multi-threading
* Health checks via standalone endpoint or actuator
* JWT

# Architecture overview
`Currently project follows the REST-full service paradigm, maintain endpoints to provide features and uses the batch processing
for data cleanup and reporting.`

`It could be easily refactored to consume data asynchronously through message broker like Kafka or similar to reduce loading
and make it more scaleable.`

# Geo calculations
* GeoHash used to store plot of land points (the list could have detailed description of area's needs to be irrigated)
* Irrigation area could be calculated as an area within all point locations inside or with some built-in solutions like Postgres GeoJson or miniGeo etc.)
* by geohash we can improve the searching and additional operations between few irrigation area's.

### Authorization
* for external authorization project uses Iam service (not included to distribution). 
  It uses opaque tokens which is reliable enough since it uses trusted server-side token introspection.
* JWT tokens used to call some endpoints without introspection to fetch the data from token itself (permission and email atm). 
  Also it possible to use jwt through inter-services communication in MSA.
  
### Improvements:
* improve test coverage
* debug the flow with scheduling
* implement client authorization flow and AuthorizationManager support
* allow user to download the reports via pre-signed urls (S3Service)
* improve test-coverage and add api-tests
* use the api-first with open-api generators
* implement management panel with built-in monitoring and controllers
* optional: use MapStruct for dto mappings
