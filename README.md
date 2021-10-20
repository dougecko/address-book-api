# address-book-api

To get dependencies and compile:

    $ ./gradlew build  

To run unit tests:

    $ ./gradlew test

To start the application:

    $ ./gradlew bootRun

To build Docker image:

    $ docker build --build-arg JAR_FILE=build/libs/\*.jar -t shine/aba .

To run docker container:

    $ docker run -p 9999:9999 -t shine/aba