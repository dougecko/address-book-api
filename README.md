# Address Book API

This address book application allows a branch manager to keep track of their customer contacts.

The application exposes endpoints for:
- add contact (POST to /contacts)
- remove contact (DELETE to /contacts/{id})
- get single contact (GET to /contacts/{id})
- get all contacts (GET to /contacts)

The following utility endpoints are also available: 
- swagger-ui API spec is served from /api-docs
- H2 database console is served from /db (user = sa, pass = password)

Standard Spring Boot actuator endpoints include:
- health check: /actuator/health
- app info: /actuator/info

## Instructions

### Build and test

To download dependencies and compile:

    $ ./gradlew build  

To run unit tests:

    $ ./gradlew test

Reports are generated in:

- build/reports/tests/test/index.html (results)
- build/reports/jacoco/test/html/index.html (coverage)

### Run

To start the application:

    $ ./gradlew bootRun

### Docker

To build the application jar:

    $ ./gradlew assemble

To build Docker image:

    $ ./gradlew assemble
    $ mkdir -p build/dependency
    $ cd build/dependency
    $ jar -xf ../libs/aba-0.0.1-SNAPSHOT.jar
    $ cd ../..
    $ docker build --build-arg JAR_FILE=build/libs/\*.jar -t shine/aba .

or (on Mac/Linux):

    $ chmod u+x docker-build.sh
    $ ./docker-build.sh

To run docker container:

    $ docker run -p 9999:9999 -t shine/aba

## Logging

Application logs can be found in `/logs/spring-boot-logger.log`, containing
HTTP access, error details and other normal Spring logs.

## Continuous Integration

When code is pushed to the main branch, this pipeline builds and runs tests:

https://github.com/dougecko/address-book-api/actions

## Assumptions

- both name and phone number must be populated
- phone number must be valid to be dialled from Australia
- adding an existing contact should fail
- updating a non-existent contact should fail
- a simple error code is sufficient (rather than a detailed JSON error response)
- “print” all contacts means “return all contacts in JSON response”

## Design Choices

- use constructor injection for required fields, setter injection for optional fields (none), and not use field injection for anything
- use separate packages for controller, service, repository for future extensibility
- use Google phone validation lib rather than a regex
- builder doesn't include id because it's autogenerated (other than in test data)
- no need for spring profiles in this spike
- no need to separate response objects from JPA DTOs
- use swagger-ui for API spec, autogenerated
- slightly more complicated dockerfile using exploded jar in layers, allows improved caching

## Testing
- unit tests over Service, SpringContext loaded, high coverage
- no tests required over Repository as it's pure spring-data-jpa
- no complex business logic exists that requires unit testing without Spring Boot
- integration tests over Controller
  - happy path
  - exceptions converted to HTTP codes

## Possible Extensions (not implemented)
- Automated linting, spotbugs, static code analysis and/or security / vulnerability checks
- Security (user authorisation)
- Prod release pipeline: synthetic tests, canary deployments
