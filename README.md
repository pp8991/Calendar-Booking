# Calendar Booking System

This is a Calendar Booking System built with **Java 21**, **Spring Boot**, and **MySQL**. The system allows calendar owners to define available time slots, and invitees to book appointments. The project also includes a Docker setup for easy deployment.

## Project Structure
- **Java Version**: 21
- **Spring Boot Version**: 3.x
- **MySQL Version**: 8.0

### Features:
- User Roles: Calendar Owner and Invitee.
- Time Slot Management: Calendar owners can create available time slots.
- Appointment Booking: Invitees can book appointments in available time slots.
- Service Layer Design Pattern: Separation of business logic from the controllers.
- Docker Integration for easier setup and deployment.

## Technologies Used
- **Java 21**: Programming language.
- **Spring Boot**: Framework used for creating the REST API.
- **MySQL**: Relational database for managing data.
- **Hibernate**: ORM for database interaction.
- **Docker**: Containerization for running the application and database in isolated environments.

## Prerequisites

Make sure to have the following installed:
- **Java 21**: Required to build and run the application.
- **Maven**: Used for dependency management.
- **MySQL**: Database for storing user, time slot, and appointment data.
- **Docker** (Optional for local development, but included for easier setup).

## How to Run the Application

### 1. Running Locally without Docker

To run the application locally, ensure that you have MySQL running on your local machine. Update the `application.properties` file with the correct database credentials.
