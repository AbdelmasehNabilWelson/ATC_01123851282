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

### Setting Up Email Verification with Mailtrap

For development and testing, we use Mailtrap to handle email verification. We've implemented the Mailtrap Java SDK for better integration. Follow these steps to set it up:

1. **Create a Mailtrap Account**
   - Go to [Mailtrap.io](https://mailtrap.io/) and sign up for a free account
   - After signing up, you'll be directed to your dashboard

2. **Get Your Mailtrap API Token and Inbox ID**
   - Go to your Mailtrap dashboard
   - Click on your profile icon in the top-right corner and select "API"
   - Copy your API token
   - Go back to the dashboard and select your inbox
   - Note the inbox ID from the URL (e.g., https://mailtrap.io/inboxes/1234567/messages - here 1234567 is your inbox ID)

3. **Update Your application.properties File**
   - Open `src/main/resources/application.properties`
   - Replace the placeholders with your actual Mailtrap credentials:
     ```properties
     mailtrap.api.token=your-actual-mailtrap-api-token
     mailtrap.inbox.id=your-actual-mailtrap-inbox-id
     mailtrap.sandbox.enabled=true
     ```

4. **Test the Email Verification**
   - Start your Spring Boot application
   - Try the signup process by sending a POST request to `/api/auth/signup`
   - Check your Mailtrap inbox - you should see the verification email appear there

The application uses the Mailtrap Java SDK to send emails, which provides better reliability and more features than the standard SMTP approach. The implementation is based on the [Mailtrap Java SDK](https://github.com/railsware/mailtrap-java) guide.

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
