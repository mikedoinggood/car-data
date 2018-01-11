# car-data
Python Django, Java Spring Boot and Android Demo

- This project is meant to showcase the basics of building a web application that supports both a web browser client and also a native mobile client.
- The data being consumed represents a car with attributes of year, make, model and trim levels.
- There are two versions of the web app, one that utilizes Spring Boot and the other using Django.
- The Android client can interact with either one interchangably since they both authenticate and serve data in the same way, although the data will be different as they are not setup to use the same databases.

More details of the other components being used:

Django Version:
- Django Oauth Toolkit
- MySQL database
- AngularJS
- Bootstrap
- Note: this project also requires additional steps of manually creating the Django user(s) and an OAuth2 client application entry

Spring Boot Version:
- Spring Oauth2
- Spring Rest Controller
- Spring Data Repositories
- MySQL database
- Thymeleaf
- Bootstrap
- jQuery

Android Client:
- Retrofit

Other:
- Python and JUnit Selenium tests for the web ui
