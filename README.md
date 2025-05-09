# Event Management System

## Technology Used
1. Spring boot as a backend framework
2. JWT for authentication
3. Maven for dependency management

## Project Overview
This is an Event Booking System that allows users to book events. Admins to create and manage events The system includes user authentication, event management, and booking functionalities.

## Setup Instructions
1. Clone the repository
2. Make sure you have Java 11+ and Maven installed
3. Navigate to the project directory
4. Run `mvn clean install` to build the project
5. Run `mvn spring-boot:run` to start the application
6. The application will be available at `http://localhost:8080`

## Features
- User authentication (signup, login)
- Event creation and management
- Booking system for events
- Address management for events

## API Endpoints
- `/api/auth/signup` - Register a new user
- `/api/auth/login` - Login and get JWT token

## Backend Features
- Secure authentication using JWT
- RESTful API design
- Exception handling
