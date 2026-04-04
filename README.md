# QMA Nexus

QMA Nexus is a full-stack quantity measurement project made by **ANURAG**.

It includes:

- an Angular frontend
- an API Gateway
- an Auth Service
- a Quantity Measurement Service

The project allows users to:

- sign up and log in
- use Google OAuth
- convert quantities
- compare quantities
- add, subtract, and divide compatible measurements

## Project Structure

```text
Playground/
|-- frontend/
|-- api-gateway/
|-- auth-service/
|-- qma-service/
|-- pom.xml
|-- .env.example
```

## Services

### `frontend`

Angular application for the UI.

### `api-gateway`

Single public backend entry point.

- runs on `8080`
- validates JWT for protected APIs
- routes requests to auth and qma services
- exposes Swagger UI for backend APIs through the gateway

### `auth-service`

Authentication service.

- runs on `8081`
- handles register and login
- supports Google OAuth
- generates JWT tokens
- stores user data in database

### `qma-service`

Business logic service for quantity operations.

- runs on `8082`
- handles convert, compare, add, subtract, and divide

## Branch Info

Current repository status:

- `master` - main working branch right now

At the moment, no other local branches are present in this repository.

## Tech Stack

- Angular
- TypeScript
- Spring Boot
- Spring Security
- Spring Cloud Gateway
- Spring Data JPA
- JWT
- Google OAuth2
- MySQL / H2
- Maven

## How To Run Locally

### Prerequisites

- Java 17+
- Maven
- Node.js and npm

### 1. Backend setup

From the project root, set the required environment variables from `.env.example`.

Important:

- `JWT_SECRET` must be the same for `auth-service` and `api-gateway`
- add `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` only if you want Google login

### 2. Run backend services

Open separate terminals and run:

```bash
mvn -pl auth-service spring-boot:run
```

```bash
mvn -pl qma-service spring-boot:run
```

```bash
mvn -pl api-gateway spring-boot:run
```

### 3. Run frontend

Go to the frontend folder:

```bash
cd frontend
```

Install packages if needed:

```bash
npm install
```

Start the frontend:

```bash
npm start
```

## Main Local URLs

- Frontend: `http://localhost:4200`
- API Gateway: `http://localhost:8080`
- Auth Service: `http://localhost:8081`
- QMA Service: `http://localhost:8082`
- Gateway Swagger UI: `http://localhost:8080/swagger-ui.html`

## Main API Routes

- `POST /auth/register`
- `POST /auth/login`
- `GET /auth/me`
- `POST /api/quantity/add`
- `POST /api/quantity/subtract`
- `POST /api/quantity/compare`
- `POST /api/quantity/divide`
- `POST /api/quantity/convert`

## Deployment

- Frontend: Vercel
- Backend: Railway

## Notes

- Runtime frontend config is read from `frontend/public/app-config.js`
- OAuth redirect behavior depends on correct Railway and Google Console configuration
- Swagger is available through the API Gateway
