# 📦 Spring Boot Notes Application

A simple Spring Boot application for managing user notes.

## 🚀 Features

- RESTful APIs for creating and managing users and notes
- JPA/Hibernate integration with PostgreSQL
- Modular entity design with one-to-many relationships

## 🛠️ Requirements

- Java 21+
- Maven
- PostgreSQL

## ⚙️ Environment Variables

Before running the application, set the following environment variables:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/your_db
export DB_USERNAME=your_db_user
export DB_PASSWORD=your_db_password
```

or,

### Set environment variables in IntelliJ

- Go to Run → Edit Configurations
- Select your Spring Boot run configuration
- In the Environment variables field, add:
  DB_URL=your_url;DB_USERNAME=your_db_user;DB_PASSWORD=your_db_password
- Click Apply

## ▶️ Running the Application

```bash
./mvnw spring-boot:run
```




