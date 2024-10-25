
# Customer API

## Overview

The **Customer API** is a Spring Boot-based REST API for managing customer data, including addresses and contacts. It supports basic CRUD operations, validation of phone numbers, and data integrity checks. The application is integrated with a MySQL database to persist customer information.

## Features
- Create, Read, Update, and Delete (CRUD) operations for customers
- Address and contact management for customers
- Validates data integrity (e.g., phone number formats, unique email addresses)
- RESTful endpoints for customer management
- Integrated with MySQL for data persistence
- Dockerized for easy deployment and setup
- Global exception handling
- Swagger API documentation

## Prerequisites

- **Java 17**
- **Maven 3.8+**
- **Docker** and **Docker Compose**
- **MySQL 8.0+**

## Running the Application

### Option 1: Running with Docker

You can run the application and MySQL database using Docker and Docker Compose. Follow the steps below to get the application up and running.

### Steps:

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-repo/customer-api.git
   cd customer-api
   ```

2. **Build and Run using Docker Compose:**

   This command will build and start both the application and the MySQL database.

   ```bash
   docker-compose up --build
   ```

    - The application will be available at:
    - endpoint: `http://localhost:8080`.
    - Swagger API Documentation: `http://localhost:8080/swagger-ui/index.html`.


3. **Access MySQL Database:**

   MySQL will be running at `localhost:3306`, and you can connect using:

    - **Username:** `root`
    - **Password:** `password`
    - **Database:** `customerdb`

   Example connection URL: `jdbc:mysql://localhost:3306/customerdb`

4. **Stopping the Application:**

   To stop the containers, run:

   ```bash
   docker-compose down
   ```

### Option 2: Running from an IDE

If you prefer running the application from an IDE like IntelliJ IDEA or Eclipse, follow these steps.

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-repo/customer-api.git
   cd customer-api
   ```

2. **Set up MySQL Database:**

    - Install and configure MySQL locally or use Docker for MySQL.
    - Create a database named `customerdb`:

      ```sql
      CREATE DATABASE customerdb;
      ```

3. **Update `application.properties`:**

   Configure your `src/main/resources/application.properties` file with the correct MySQL credentials:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/customerdb
   spring.datasource.username=root
   spring.datasource.password=password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

4. **Run the Application:**

    - Open the project in your IDE.
    - Run the `CustomerApiApplication` class.


    - Open the project in your IDE.
    - Run the `CustomerApiApplication` class.
   - The application will be available at: 
     - endpoint: `http://localhost:8080`.
     - Swagger API Documentation: `http://localhost:8080/swagger-ui/index.html`.

### Testing the API


You can test the API endpoints using `curl`, Postman, or any other HTTP client. Below are some example requests:

1. **Create a Customer:**

   ```bash
   curl -X POST http://localhost:8080/api/customers    -H "Content-Type: application/json"    -d '{
     "firstName": "John",
     "lastName": "Doe",
     "email": "john.doe@example.com",
     "address": {
       "street": "123 Main St",
       "city": "Miami",
       "state": "FL",
       "zip": "33101",
       "addressType": "Home"
     },
     "contact": {
       "phoneNumber": "(123) 456-7890",
       "contactType": "Mobile"
     }
   }'
   ```

2. **Get All Customers:**

   ```bash
   curl http://localhost:8080/api/customers
   ```

3. **Update a Customer:**

   ```bash
   curl -X PUT http://localhost:8080/api/customers/1    -H "Content-Type: application/json"    -d '{
     "firstName": "Jane",
     "lastName": "Doe",
     "email": "jane.doe@example.com",
     "address": {
       "street": "456 Main St",
       "city": "Orlando",
       "state": "FL",
       "zip": "32801",
       "addressType": "Office"
     },
     "contact": {
       "phoneNumber": "(321) 654-0987",
       "contactType": "Work"
     }
   }'
   ```

4. **Delete a Customer:**

   ```bash
   curl -X DELETE http://localhost:8080/api/customers/1
   ```

## Project Structure

```bash
customer-api
├── src
│   ├── main
│   │   ├── java
│   │   │   └── smallr
│   │   │       └── com
│   │   │           └── customer_api
│   │   │               ├── controller    # API endpoints
│   │   │               ├── model         # Data models (Customer, Address, Contact)
│   │   │               ├── repository    # JPA repositories
│   │   │               ├── service       # Business logic
│   │   │               └── exception     # Custom exception handling
│   │   └── resources
│   │       └── application.properties    # Application configuration
├── Dockerfile                             # Docker configuration for the application
├── docker-compose.yml                     # Docker Compose setup for app and MySQL
├── pom.xml                                # Maven dependencies and build configuration
└── README.md                              # Project documentation
```

## Technology Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **MySQL** for the database
- **Docker** for containerization
- **Maven** for build automation

## How to Run Tests

You can run the tests using Maven:

```bash
mvn test
```

The test results will be available in the `target/surefire-reports` directory.

## Contributing

Feel free to contribute by creating pull requests or raising issues. Make sure to follow the project's coding standards and best practices.

