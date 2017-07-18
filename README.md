# car-data
Java Spring Boot and Android Demo

- This project is meant to showcase a web app that utilizes Spring Boot for the server side and web client side, and an Android client as well.
- The data being consumed represents a car with attributes of year, make, model and trim levels.
- A Zuul proxy along with shared sessions via Redis is used to integrate authentication for the web client.

Some of the other components being used:

Server:
- Spring Oauth2
- Spring Rest Controller
- Spring Data Repositories
- In-memory H2 database

Web Client:
- Thymeleaf
- Bootstrap
- jQuery

Android Client:
- Retrofit
