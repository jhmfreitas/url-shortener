# URL Shortener

This repository contains a URL shortener service implemented in Java. The service allows users to shorten long URLs and retrieve the original URLs from the shortened versions.

## Features

- Shorten a long URL
- Retrieve the original URL from a shortened URL
- Deployed using Docker
- Built with Maven + Spring Boot

## Requirements

- Java 17 or higher
- Docker
- Docker Compose
- Maven

## Architecture
The URL Shortener service is designed with a modular and scalable architecture, ensuring ease of development, deployment, and maintenance. Below is an overview of the main components:

1. **Controller Layer**
   - **Purpose:** Handles HTTP requests and responses.
   - **Components:**
       - **UrlShortenerController:** Manages endpoints for creating and retrieving shortened URLs.
2. **Service Layer**
   - **Purpose:** Contains the business logic of the application.
   - **Components:**
      - **UrlShortenerService:** Handles URL shortening logic and retrieval of original URLs.
3. **Data Access Layer**
   - **Purpose:** Manages data persistence and retrieval.
   - **Components:**
      - **UrlRepository:** Interface for database operations using Spring Data JPA.
4. **Database**
   - **Purpose:** Stores original and shortened URLs.
   - **Technology:** Uses MongoDB for data persistence.
5. **Docker Configuration**
   - **Purpose:** Containerizes the application for consistent deployment across environments.
   - **Components:**
     - **Dockerfile:** Defines the application’s Docker image.
     - **docker-compose.yml:** Sets up the application and database in a multi-container Docker environment.
6. **Maven Configuration**
   - **Purpose:** Manages project dependencies, builds, and tests.
   - **Components:**
     - **pom.xml:** Configures Maven dependencies, plugins, and project metadata.

## Flow Diagram
```
+-----------+     +-----------+     +-------------+
|  Client   | --> | Controller| --> |  Service    |
+-----------+     +-----------+     +-------------+
                                            |
                                            v
                                   +-----------------+
                                   | Data Access Layer|
                                   +-----------------+
                                            |
                                            v
                                    +-----------------+
                                    |    Database     |
                                    +-----------------+
```
## Sequence of Operations

1. **Shorten URL:**

- Client sends a POST request with the original URL.
- Controller forwards the request to the Service layer.
- Service generates a unique identifier for the URL and stores it in the database via the Data Access Layer.
- The shortened URL is returned to the client.

2. **Retrieve Original URL:**

- Client sends a GET request with the shortened URL identifier.
- Controller forwards the request to the Service layer.
- Service retrieves the original URL from the database via the Data Access Layer.
- The original URL is returned to the client.
- This architecture ensures a clear separation of concerns, making the system easier to maintain and extend. It also leverages Docker for consistent deployment and Maven for dependency management, making the development process more efficient.

## Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/jhmfreitas/url-shortener.git
    cd url-shortener
    ```

2. **Run the service using Docker Compose building the url shortener backend docker image:**

    ```sh
    docker-compose up --build
    ```

## Usage

Once the service is up and running, you can use the following endpoints:

1. **Shorten a URL:**

    - **Endpoint:** `POST /api/shorten`
    - **Request Body:**
      ```json
      {
          "url": "https://www.example.com"
      }
      ```
    - **Response:**
      ```json
      {
          "shortenedUrl": "http://localhost:8080/abc123"
      }
      ```

2. **Retrieve the original URL:**

    - **Endpoint:** `GET /api/{shortenedUrlId}`
    - **Example:** `GET /api/abc123`
    - **Response:**
      ```json
      {
          "url": "https://www.example.com"
      }
      ```

## Project Structure

- `src/main/java`: Contains the Java source files.
- `src/test/java`: Contains the test files.
- `Dockerfile`: Docker configuration for the application.
- `docker-compose.yml`: Docker Compose configuration for running the service.
- `pom.xml`: Maven configuration file.

## Building and running the backend on the host

1. **Build the project using Maven:**

    ```sh
    ./mvnw clean install
    ```

2. **Run the application:**

    ```sh
    ./mvnw spring-boot:run
    ```