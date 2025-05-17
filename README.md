# Event Booking System API

Welcome to the Event Booking System API! This document will guide you through setting up the project and understanding its features. Our goal is to provide a clear and simple guide for you.

## Table of Contents

1.  [About the Project](#about-the-project)
2.  [Core Features](#core-features)
3.  [API Endpoints](#api-endpoints)
    *   [Authentication](#authentication-api)
    *   [Users](#user-api)
    *   [Events](#event-api)
    *   [Bookings](#booking-api)
    *   [Categories](#category-api)
4.  [Setup Guide](#setup-guide)
    *   [Prerequisites](#prerequisites)
    *   [Database Setup (PostgreSQL)](#database-setup-postgresql)
    *   [Application Configuration](#application-configuration)
    *   [Running the Application](#running-the-application)
5.  [Third-Party Service Configuration](#third-party-service-configuration)
    *   [Mailtrap Setup (for Emailing)](#mailtrap-setup-for-emailing)
    *   [Azure Blob Storage Setup (for Image Uploads)](#azure-blob-storage-setup-for-image-uploads)

## About the Project

This project is a backend API for an Event Booking System. It allows users to register, login, view events, book events, and manage their profiles. Administrators have additional rights to manage events and categories. The system uses JWT for secure authentication and integrates with services like Mailtrap for email notifications and Azure Blob Storage for storing images.

## Core Features

Here are the main functionalities of the API:

*   **User Authentication & Authorization:**
    *   Secure user registration with email verification.
    *   JWT-based authentication for accessing protected API routes.
    *   Role-based access control (User and Admin roles).
*   **User Management:**
    *   Users can update their profiles.
    *   Users can upload profile images.
*   **Event Management (Admin):**
    *   Admins can create, read, update, and delete events.
    *   Admins can upload images for events.
    *   Events can be filtered by category, date range, and status.
*   **Category Management (Admin):**
    *   Admins can create, read, update, and delete event categories.
*   **Booking Management:**
    *   Authenticated users can create bookings for events.
    *   Users can view their own bookings.
    *   Users can update their bookings.
*   **Email Notifications:**
    *   Sends verification emails upon user registration using Mailtrap.
*   **Image Handling:**
    *   Supports uploading and storing images (e.g., user profiles, event banners) using Azure Blob Storage.

## API Endpoints

All API endpoints are prefixed with `/api`.

### Authentication API

Base Path: `/api/auth`

*   **`POST /login`**
    *   **Purpose:** Allows a registered user to log in.
    *   **Request:** `Authorization: Basic <base64_encoded_username:password>` header.
    *   **Response:** JWT token upon successful authentication.
*   **`POST /signup`**
    *   **Purpose:** Registers a new user.
    *   **Request Body:** User registration details (e.g., username, email, password).
    *   **Response:** Confirmation message. Sends a verification email.
*   **`GET /verify?token=<verification_token>`**
    *   **Purpose:** Verifies a user's email address using the token sent during registration.
    *   **Response:** Success or failure message.
*   **`POST /logout`**
    *   **Purpose:** Logs out the currently authenticated user.
    *   **Response:** Success message.

### User API

Base Path: `/api/user`
(Requires authentication: User or Admin role)

*   **`PUT /profile`**
    *   **Purpose:** Allows an authenticated user to update their profile information.
    *   **Request Body:** Fields to update (e.g., name, contact information).
    *   **Response:** Success message or updated user details.
*   **`POST /uploadImage`**
    *   **Purpose:** Allows an authenticated user to upload or update their profile image.
    *   **Request:** Multipart form data with an image file.
    *   **Response:** URL of the uploaded image or success message.

### Event API

Base Path: `/api/events`

*   **`POST /create`** (Admin role required)
    *   **Purpose:** Creates a new event.
    *   **Request Body:** Event details (e.g., title, description, date, location, capacity, category).
    *   **Response:** Details of the created event.
*   **`GET /{id}`** (User or Admin role required)
    *   **Purpose:** Retrieves details of a specific event by its ID.
    *   **Response:** Event details.
*   **`DELETE /{id}`** (Admin role required)
    *   **Purpose:** Deletes a specific event by its ID.
    *   **Response:** No content or success message.
*   **`PUT /{id}`** (Admin role required)
    *   **Purpose:** Updates an existing event by its ID.
    *   **Request Body:** Fields to update for the event.
    *   **Response:** Details of the updated event.
*   **`GET /allEvents`** (User or Admin role required)
    *   **Purpose:** Retrieves a list of all events. Supports filtering (by category, date, status) and pagination.
    *   **Query Parameters:** `categoryName`, `categoryId`, `startTime`, `endTime`, `eventState`, `page`, `size`, `sort`.
    *   **Response:** A paginated list of events.
*   **`POST /{id}/uploadEventImage`** (Admin role required)
    *   **Purpose:** Uploads an image for a specific event.
    *   **Request:** Multipart form data with an image file.
    *   **Response:** URL of the uploaded image or success message.

### Booking API

Base Path: `/api/bookings`
(Requires authentication: User or Admin role)

*   **`POST /create`**
    *   **Purpose:** Allows an authenticated user to create a new booking for an event.
    *   **Request Body:** Booking details (e.g., event ID, number of tickets/capacity).
    *   **Response:** Details of the created booking.
*   **`GET /{id}`**
    *   **Purpose:** Retrieves details of a specific booking by its ID. (User can only get their own bookings, Admin can get any).
    *   **Response:** Booking details.
*   **`GET /all`**
    *   **Purpose:** Retrieves all bookings made by the authenticated user.
    *   **Response:** A list of the user's bookings.
*   **`PUT /{id}`**
    *   **Purpose:** Allows a user to update their booking (e.g., change number of tickets if allowed).
    *   **Request Body:** Fields to update for the booking.
    *   **Response:** Details of the updated booking.

### Category API

Base Path: `/api/category`

*   **`GET /{id}`** (User or Admin role required)
    *   **Purpose:** Retrieves details of a specific category by its ID.
    *   **Response:** Category details.
*   **`GET /all`** (User or Admin role required)
    *   **Purpose:** Retrieves a list of all event categories.
    *   **Response:** A list of categories.
*   **`POST /create`** (Admin role required)
    *   **Purpose:** Creates a new event category.
    *   **Request Body:** Category details (e.g., name, description).
    *   **Response:** Details of the created category.
*   **`POST /patch`** (Admin role required)
    *   **Purpose:** Batch creates multiple event categories.
    *   **Request Body:** A list of category details.
    *   **Response:** A list of created categories.
*   **`DELETE /{id}`** (Admin role required)
    *   **Purpose:** Deletes a specific category by its ID.
    *   **Response:** No content or success message.
*   **`PUT /`** (Admin role required)
    *   **Purpose:** Updates an existing category.
    *   **Request Body:** Category ID and fields to update.
    *   **Response:** Details of the updated category.

## Setup Guide

Follow these steps to get the project running on your local machine.

### Prerequisites

*   **Java Development Kit (JDK):** Version 17 or higher.
*   **Maven:** For building the project and managing dependencies.
*   **PostgreSQL:** As the database for the application.
*   **Git:** For cloning the repository (if applicable).
*   An IDE like IntelliJ IDEA or VS Code is recommended.

### Database Setup (PostgreSQL)

1.  **Install PostgreSQL:** If you don't have it, download and install PostgreSQL from [its official website](https://www.postgresql.org/download/).
2.  **Create a Database:**
    *   Open `psql` or a GUI tool like pgAdmin.
    *   Create a new database for the project. For example, `event_booking_db`.
    *   ```sql
        CREATE DATABASE event_booking_db;
        ```
3.  **Insert example data as you need**

### Application Configuration

1.  **Clone the Repository (if you haven't):**
    ```bash
    git clone <repository_url>
    cd EventBookingSystem
    ```
2.  **Configure `application.properties`:**
    This file is located at `src/main/resources/application.properties`. You will need to update it with your specific settings.

    ```properties
    # Spring Datasource
    spring.datasource.url=jdbc:postgresql://localhost:5432/event_booking_db
    spring.datasource.username=event_user
    spring.datasource.password=your_strong_password
    spring.datasource.driver-class-name=org.postgresql.Driver

    # JPA Properties
    spring.jpa.hibernate.ddl-auto=update # Use 'validate' or 'none' in production
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.properties.hibernate.format_sql=true

    # JWT Settings
    jwt.secret=your-very-strong-and-long-jwt-secret-key # Change this to a strong secret
    jwt.expiration.ms=3600000 # Token validity in milliseconds (e.g., 1 hour)
    jwt.refresh.token.expiration.ms=86400000 # Refresh token validity (e.g., 24 hours)

    # Mail Settings (Mailtrap)
    spring.mail.host=smtp.mailtrap.io
    spring.mail.port=2525 # Or 587, 465, 25
    spring.mail.username=your_mailtrap_username # Your Mailtrap API token (often used as username)
    spring.mail.password=your_mailtrap_password # Your Mailtrap API token password (if applicable)
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true # Use true if your Mailtrap port requires TLS
    app.email.sender=noreply@example.com # Your application's sender email address
    app.email.verification.link.baseurl=http://localhost:8080/api/auth/verify?token= # Base URL for verification links

    # Azure Blob Storage Settings
    azure.storage.connection-string=YOUR_AZURE_STORAGE_CONNECTION_STRING
    azure.storage.container-name=YOUR_AZURE_BLOB_CONTAINER_NAME # e.g., event-images

    # Server Port
    server.port=8080
    ```

    **Important Notes for `application.properties`:**
    *   Replace placeholders like `your_strong_password`, `your-very-strong-and-long-jwt-secret-key`, `your_mailtrap_username`, `your_mailtrap_password`, `YOUR_AZURE_STORAGE_CONNECTION_STRING`, and `YOUR_AZURE_BLOB_CONTAINER_NAME` with your actual credentials and values.
    *   For `jwt.secret`, use a long, random, and strong string.
    *   `spring.jpa.hibernate.ddl-auto=update` is convenient for development as it automatically updates the schema. For production, consider using `validate` or managing schema changes with migration tools like Flyway or Liquibase.

### Running the Application

1.  **Build the Project (Optional, as `spring-boot:run` often handles this):**
    Open a terminal in the project's root directory and run:
    ```bash
    mvn clean install
    ```
2.  **Run the Application:**
    You can run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```
    Or, if you have built a JAR file, you can run it directly:
    ```bash
    java -jar target/EventBookingSystem-0.0.1-SNAPSHOT.jar
    ```
3.  The API should now be running on `http://localhost:8080`.

## Third-Party Service Configuration

### Mailtrap Setup (for Emailing)

Mailtrap is used for testing email sending during development.

1.  **Create a Mailtrap Account:** Go to [Mailtrap.io](https://mailtrap.io/) and sign up for a free account.
2.  **Get Your Credentials:**
    *   Once logged in, go to your "Inboxes".
    *   Select an inbox (or create a new one).
    *   Under "SMTP Settings" or "Integrations", you will find your `Host`, `Port`, `Username`, and `Password`.
3.  **Update `application.properties`:**
    *   Copy these credentials into the `spring.mail.*` properties in your `application.properties` file as shown in the [Application Configuration](#application-configuration) section.
    *   `app.email.sender` should be an email address you want to appear as the sender.
    *   `app.email.verification.link.baseurl` is the base URL that will be prepended to the verification token in the emails sent by the application. Ensure this points to your running application's verification endpoint.

### Azure Blob Storage Setup (for Image Uploads)

Azure Blob Storage is used to store uploaded images like user profile pictures and event banners.

1.  **Create an Azure Account:** If you don't have one, sign up for a free Azure account at [azure.microsoft.com](https://azure.microsoft.com/).
2.  **Create a Storage Account:**
    *   In the Azure portal, search for and select "Storage accounts".
    *   Click "+ Create" to create a new storage account.
    *   Fill in the required details (Subscription, Resource group, Storage account name, Region, Performance, Redundancy).
    *   Once created, navigate to your storage account.
3.  **Get Connection String:**
    *   In your storage account menu, go to "Access keys" (under Security + networking).
    *   Copy one of the connection strings (e.g., `key1` connection string).
4.  **Create a Blob Container:**
    *   In your storage account menu, go to "Containers" (under Data storage).
    *   Click "+ Container".
    *   Give your container a name (e.g., `event-images` or `user-profile-images`). This name should be unique within the storage account.
    *   Set the "Public access level". For most web applications, you might set it to "Blob (anonymous read access for blobs only)" if you want images to be publicly viewable via their URL. Adjust based on your security needs.
5.  **Update `application.properties`:**
    *   Paste the copied connection string into `azure.storage.connection-string`.
    *   Enter the container name you created into `azure.storage.container-name`.

    Example:
    ```properties
    azure.storage.connection-string=DefaultEndpointsProtocol=https...AccountKey=YOUR_KEY;EndpointSuffix=core.windows.net
    azure.storage.container-name=event-images
    ```

---

This guide should help you get the Event Booking System API up and running. If you encounter any issues, please double-check your configurations and ensure all prerequisites are met.
