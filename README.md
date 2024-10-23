# Calendar Booking System

This is a **Calendar Booking System** built with Java 21, Spring Boot, and MySQL. It allows calendar owners to define available time slots and invitees to book appointments. The system is designed using service-layer patterns for scalability and flexibility and includes Docker for simplified deployment.

## Project Structure

- **Java Version**: 21
- **Spring Boot Version**: 3.x
- **MySQL Version**: 8.0
- **Docker**: Containerization for seamless setup and deployment.

## Key Features

- **User Roles**: Calendar owners can set their availability, and invitees can book time slots.
- **Dynamic Time Slot Management**: Owners can specify availability, and bookable slots are generated dynamically.
- **Appointment Booking**: Invitees can book available slots, and the system checks for slot availability.
- **Design Patterns**: Service Layer, Global Exception Handling, and others (see below for a detailed explanation).
- **Global Exception Handling**: `@ControllerAdvice` is used to handle exceptions globally.
- **Dynamic Time Slot Generation**: Time slots are created dynamically based on availability without saving all slots in the database.
- **Validation**: Prevent overlapping time slots when setting availability.

## Technologies Used

- **Java 21**: Programming language for building the application.
- **Spring Boot**: Backend framework for creating the RESTful API.
- **MySQL**: Database for persisting user, availability, and appointment data.
- **Hibernate**: ORM for database operations.
- **Docker**: Containerization of the application and database.

## Design Patterns & Architecture

1. **Service Layer Design Pattern**  
   The project separates business logic into services like `TimeSlotService`, `AvailabilityService`, `UserService`, and `AppointmentService`. This keeps controllers lightweight and focused on handling HTTP requests and responses.

2. **Global Exception Handling**  
   `@ControllerAdvice` is used to handle exceptions across all controllers. This improves maintainability by centralizing exception handling and sending meaningful error messages to the client.

3. **Dynamic Slot Creation**  
   Instead of saving each time slot in the database, the system dynamically generates slots based on the owner's availability. This is more efficient for large-scale usage. The time_slot is only saved for the slots where the Appointment is done

4. **Validation Pattern**  
   Custom validation ensures no overlapping time slots when setting availability, ensuring consistency in the booking process.

## Prerequisites

Ensure you have the following installed:

- **Java 21**: Required for building and running the application.
- **Maven**: For dependency management and building the project.
- **MySQL**: For managing relational data.
- **Docker**: For easy setup and deployment (optional for local development).

## How to Run the Application

### 1. Running Locally Without Docker

**Setup MySQL**:  
Ensure MySQL is running on your local machine and update the `src/main/resources/application.properties` file with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/calendarbooking
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

**Run SQL Scripts:**
Execute the SQL script:
located in `src/main/resources/calendarBooking.sql` to create the required database schema:
```
mysql -u root -p < src/main/resources/calendarBooking.sql
```

***Build and Run the Application:***
```
mvn clean install
mvn spring-boot:run
```
### 2. Running with Docker

To simplify the setup, Docker is used to containerize both the application and the MySQL database.

**Steps:**
**1.Build Docker Images and Run Containers:**

Use the provided docker-compose.yml file and run the following command:
```
docker-compose up --build
```
This will create and run:
- A MySQL container with your database.
- A Spring Boot container for the backend application.
**Database Initialization:**
The `calendarBooking.sql` script will automatically run inside the MySQL container and set up the schema.

**Docker Configuration:**
Your MySQL data is persisted using Docker volumes, and the SQL initialization scripts are copied into the MySQL container via:
```
volumes:
  - ./src/main/resources:/docker-entrypoint-initdb.d
```

##Access the Application:

**Swagger API Documentation:**

http://localhost:8080/api/v1/swagger-ui/index.html

*Backend runs on:* 
`http://localhost:8080`
