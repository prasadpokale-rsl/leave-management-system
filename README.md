# Leave Management API

## Project Overview

Leave Management API is a Spring Boot REST application used to manage employee leave requests.

The application allows users to create new leave requests, view existing leave records, update leave details, and delete leave records. Each leave record contains details such as employee ID, leave type, start date, end date, reason, and leave status.

The project follows a layered structure with separate Controller, Service, Repository, Model, DTO, Configuration, and Exception packages. The Controller handles API requests, the Service contains the business logic, and the Repository manages the leave data.

The application uses an in-memory repository, so no external database setup is required. Leave data is stored while the application is running and is cleared when the application is restarted.

Request validation is added to check the input data before processing requests. Global exception handling is also included to handle cases such as a leave record not being found, invalid input, and other application errors.

## Project Setup

### Prerequisites

Make sure the following are installed:

- Java
- Maven

### Build the Project

Open a terminal in the project directory and run:

```bash
./mvnw clean install
```

This will build the application and run the available tests.

### Run the Application

Start the application using:

```bash
./mvnw spring-boot:run
```

The application uses the `dev` Spring profile and runs on port `8081`.

The base URL is:

```text
http://localhost:8081
```

Once the application is running, the APIs can be tested using curl or any REST API client.

## API Endpoints

### 1. Get All Leaves

**GET** `/leaves`

Returns all leave records currently available in the application.

Example:

```bash
curl http://localhost:8081/leaves
```

A successful request returns the list of leave records.

### 2. Get Leave by ID

**GET** `/leaves/{id}`

Returns a specific leave record using its ID.

Example:

```bash
curl http://localhost:8081/leaves/1
```

If the leave ID does not exist, the API returns a `404 Not Found` response.

### 3. Create Leave

**POST** `/leaves`

Creates a new leave request.

Example:

```bash
curl -X POST http://localhost:8081/leaves \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 101,
    "leaveType": "PAID",
    "startDate": "2026-10-05",
    "endDate": "2026-10-07",
    "reason": "Personal work"
  }'
```

A successful request creates a new leave record with the `APPLIED` status.

### 4. Update Leave

**PUT** `/leaves/{id}`

Updates the details of an existing leave request.

Example:

```bash
curl -X PUT http://localhost:8081/leaves/1 \
  -H "Content-Type: application/json" \
  -d '{
    "leaveType": "CASUAL",
    "startDate": "2026-10-10",
    "endDate": "2026-10-12",
    "reason": "Updated personal work"
  }'
```

If the leave ID does not exist, the API returns a `404 Not Found` response.

### 5. Delete Leave

**DELETE** `/leaves/{id}`

Deletes an existing leave record using its ID.

Example:

```bash
curl -X DELETE http://localhost:8081/leaves/1
```

A successful deletion returns `204 No Content`.

If the leave ID does not exist, the API returns a `404 Not Found` response.

## Validation and Error Handling

The API uses Bean Validation to validate incoming requests.

For example, required fields are checked before creating or updating a leave. The application also checks that the end date is not before the start date.

Global exception handling is implemented using `@RestControllerAdvice`.

Common responses include:

- `400 Bad Request` - Invalid request or validation failure
- `404 Not Found` - Leave record does not exist
- `500 Internal Server Error` - Unexpected application error

Example:

```bash
curl http://localhost:8081/leaves/999
```

If the leave does not exist, the API returns a `404 Not Found` response with the error details.

## Testing Instructions

Run all tests using:

```bash
./mvnw test
```

The project contains:

- Service layer unit tests using JUnit and Mockito
- REST controller tests using MockMvc
- Repository tests

The tests cover the main operations as well as error and validation scenarios.
