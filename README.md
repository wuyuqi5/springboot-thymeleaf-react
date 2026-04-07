# Spring Boot Thymeleaf React

A starter application that combines Spring Boot 4, Thymeleaf, htmx, Alpine.js, and React with Vite. It is set up as a modular monolith and includes authentication, server-rendered pages, React-powered screens, Flyway migrations, PostgreSQL, and Docker support.

## What is in this project

- Spring Boot 4 on Java 21
- Thymeleaf layouts for server-rendered pages
- htmx and Alpine.js for lightweight interactivity
- React 19 + Vite for richer screens
- Spring Security with form login and JWT cookies
- Flyway database migrations and seed data
- PostgreSQL for local and containerized development

## Current screens

- `/login` - login page
- `/` - dashboard page
- `/todo` - server-rendered todo board with htmx updates
- `/todo/react` - React-based todo screen

## Prerequisites

- Java 21
- Node.js 20
- npm 10+
- PostgreSQL 16 recommended

If you use `fnm` on Windows to manage Node.js 20, see [`FNM_WINDOWS.md`](/Users/dutianze/github/springboot-thymeleaf-react/FNM_WINDOWS.md).

## Local development

The default profile is `dev`.

### 1. Start PostgreSQL

The development profile expects:

- host: `localhost`
- port: `5432`
- database: `postgres`
- username: `postgres`
- password: `root`

You can change these values in [`src/main/resources/application-dev.yml`](/Users/dutianze/github/springboot-thymeleaf-react/src/main/resources/application-dev.yml).

### 2. Install frontend dependencies

```bash
npm install
```

### 3. Start the Spring Boot app

```bash
./mvnw spring-boot:run
```

The app runs on `http://localhost:8080`.

### 4. Start the Vite dev server

In a second terminal:

```bash
npm run dev
```

This enables React/Vite assets to load in development mode.

## Seed data

Flyway creates a seeded admin user in [`src/main/resources/db/migration/R__002_seed_init.sql`](/Users/dutianze/github/springboot-thymeleaf-react/src/main/resources/db/migration/R__002_seed_init.sql) with username `admin`.

If you want a known local password, update the seed file with your own BCrypt hash before first startup, or reset the row in the database after migration.

## Build

### Backend + frontend production build

```bash
./mvnw package
```

The Maven build is configured to install Node/npm tooling and run the frontend build through `frontend-maven-plugin`.

### Frontend-only build

```bash
npm run build
```

## Docker

The Docker setup runs the application with the `prod` profile and starts PostgreSQL in a separate container.

### Start containers

```bash
docker compose up -d
```

### Stop containers

```bash
docker compose down
```

The app is exposed on `http://localhost:8080`.

## Makefile commands

- `make build` - build the Docker image
- `make up` - start containers
- `make deploy` - rebuild and restart the app container
- `make down` - stop containers
- `make logs` - tail app logs
- `make exec` - open a shell in the app container
- `make clean` - remove unused Docker images

## Project structure

```text
springboot-thymeleaf-react/
├─ src/main/java/io/github/dutianze/springbootthymeleafreact/
│  ├─ modules/
│  │  ├─ auth/
│  │  ├─ dashboard/
│  │  ├─ todo/
│  │  └─ account/
│  └─ shared/
├─ src/main/resources/
│  ├─ db/migration/
│  ├─ static/
│  ├─ templates/
│  ├─ application.yml
│  ├─ application-dev.yml
│  └─ application-prod.yml
├─ package.json
├─ pom.xml
├─ docker-compose.yml
├─ Dockerfile
└─ Makefile
```

## Notes

- `application.yml` currently contains a JWT secret for local use. Treat it as development-only configuration.
- `docker-compose.yml` uses `postgres/root` for the local database container.
- The repository contains both server-rendered and React-driven examples on purpose, so it can serve as a reference for hybrid UI development.
