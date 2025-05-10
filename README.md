# Event Booking System

Welcome to our Event Booking System! This easy-to-use platform helps you find and book events that interest you.

## What This System Does

This system lets you:
- Find and book events you want to attend
- Create an account and log in securely
- If you're an admin, you can create and manage events

## Technologies We Use

We built this system using:
1. Spring Boot - a tool that makes creating web applications easier
2. JWT (JSON Web Tokens) - a secure way to handle user logins
3. Maven - a tool that helps manage the project's parts

## How to Set Up the System

Follow these simple steps to get started:

1. Download the project files to your computer
2. Make sure you have Java version 11 or newer and Maven installed
3. Open your command prompt and go to the project folder
4. Type `mvn clean install` and press Enter to build the project
5. Type `mvn spring-boot:run` and press Enter to start the application
6. Open your web browser and go to `http://localhost:8080`

## Main Features

Our system offers:
- User accounts: Create an account and log in securely
- Event management: Browse events, see details, and book tickets
- Booking system: Reserve your spot at events you want to attend
- Location management: Find events based on their location

## How to Use the API

If you're a developer, you can use these endpoints:

### Authentication Endpoints
- `/api/auth/signup` - Create a new user account
- `/api/auth/login` - Log in and get your access token
- `/api/auth/logout` - Log out from your current session

### Event Management Endpoints (Admin Only)
- `/api/events/create` - Create a new event
- `/api/events/{id}` - Get details of a specific event
- `/api/events/{id}` - Update an existing event (PUT method)
- `/api/events/{id}` - Delete an event (DELETE method)

## Technical Details

For developers, our system includes:
- Secure user authentication with JWT
- Clean and organized API design
- Proper error handling to help solve problems

We hope you enjoy using our Event Booking System! If you have questions or need help, please let us know.
