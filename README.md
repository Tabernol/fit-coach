# Fit-coach CRM System

This project is a REST-based application designed to manage trainee and trainer profiles, as well as training sessions, within a fit-coach(gym) CRM system. It features profile registration, login functionality, and various profile management operations. The system includes JWT authentication, and advanced logging for transactions and REST calls.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Logging](#logging)
- [Authentication](#authentication)
- [Setup Instructions](#setup-instructions)

## Project Overview

The Fit-Coach(gym) CRM system provides the following key features:
- Registration of **trainee** and **trainer** profiles.
- Login functionality using JWT-based authentication.
- Profile management, including updates and deletions, with strict role-based validation.
- Management of training sessions, such as adding, listing, and filtering based on date, type, or participant details.

The application is designed with REST architecture and makes extensive use of Spring Framework features, including AOP (Aspect-Oriented Programming) for transaction logging and interceptors for REST call and authentication logging.

### Key Services
- **TrainerService**: Handles creating, updating, and retrieving trainer profiles.
- **TraineeService**: Manages creation, update, and deletion of trainee profiles.
- **TrainingService**: Adds and retrieves training sessions based on filtering criteria.

Most services require authentication except for profile creation.

## Features

### Profile Management
- **Trainee Registration** (POST): Register new trainees with optional details such as date of birth and address.
- **Trainer Registration** (POST): Register new trainers with specialization.
- **Profile Updates** (PUT): Update trainee or trainer details.
- **Profile Deletion** (DELETE): Hard deletion of trainee profiles with cascading deletion of relevant trainings.

### Authentication and Security
- **Login** (GET): JWT-based login, issuing tokens upon successful authentication.
- **JWT Token Authentication**: All actions (except registration) require a valid JWT token, which is handled by a custom authentication interceptor.
- **Profile Activation/Deactivation** (PATCH): Change the active status of trainee or trainer profiles (non-idempotent).

### Training Management
- **Add Training** (POST): Schedule new training sessions.
- **Retrieve Training List** (GET): Get training sessions filtered by trainee/trainer name, date range, or training type.
- **Trainee-Trainer Management**: Assign or remove trainers from a trainee’s profile.

### Training Types Management
- **Fixed Training Types**: A fixed list of training types is provided, which cannot be updated from the application.

## Technologies Used
- **Spring Framework**: Core dependency injection, RESTful APIs, transaction management, and AOP.
- **Hibernate**: ORM for managing database entities.
- **H2 Database**: In-memory database used for development and testing.
- **JWT**: For secure token-based authentication.
- **JUnit & Mockito**: For unit testing and mocking dependencies.
- **Jackson**: For JSON serialization and deserialization.

## Logging

Two levels of logging have been implemented:

### 1. Transaction Logging
Transaction-level logging is handled using AOP with a custom `@Aspect` class. Each transaction is assigned a unique `transactionId`, allowing tracking of all operations related to the transaction, including persistence and service-layer operations.

The logging captures:
- The start and end of each transaction.
- Any exceptions that occur during the transaction.

### 2. REST Logging
REST logging is managed via a custom interceptor. The interceptor logs details of each incoming request, including:
- The endpoint that was called.
- The request payload.
- The response status and message (either `200 OK` or the error message).

Both general logging and specialized logs for transactions and REST requests are stored in separate log files.

## Authentication

Authentication is handled via JWT tokens. A custom authentication interceptor validates the token for all REST calls except for the registration of new profiles and the login endpoint.

### How Authentication Works:
1. During login, a valid JWT token is issued and sent back to the client.
2. For subsequent requests, this token must be provided in the `Authorization` header (using the `Bearer` schema).
3. The custom interceptor verifies the validity of the token for each request before allowing further processing.

## Setup Instructions

### Clone the Repository

```bash
git clone https://github.com/Tabernol/fit-coach
cd fit-coach
