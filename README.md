# Job Tracker API

Spring Boot backend for a job application tracker. The frontend is in a separate repo: [jobtracker-frontend](https://github.com/arbinita/jobtracker-frontend). Screenshots of the app are in that repo's README.

I built this to practice Spring Boot by making a full app with a database, login, and a React frontend.

## What it does

- Sign up and log in (JWT)
- Create, read, update and delete job applications
- Each user only sees and edits their own applications
- Passwords are hashed with BCrypt

## Stack

Java 17+, Spring Boot, Spring Data JPA, Spring Security, JWT (jjwt), MySQL, Maven.

## Endpoints

```
POST   /api/auth/signup
POST   /api/auth/login
GET    /api/applications
POST   /api/applications
PUT    /api/applications/{id}
DELETE /api/applications/{id}
```

Signup and login are public. The `/api/applications` endpoints need a token: `Authorization: Bearer <token>`.

## Running it locally

You need Java 17 or newer and MySQL.

```bash
git clone https://github.com/arbinita/jobtracker.git
cd jobtracker
```

Create the database:

```sql
CREATE DATABASE jobtracker_db;
```


The database settings are read from environment variables. Only the password is required, the other two have defaults:

| Variable | Default |
|---|---|
| `DB_PASSWORD` | none, you must set it |
| `DB_USER` | `root` |
| `DB_URL` | `jdbc:mysql://localhost:3306/jobtracker_db` |

Run it:

```bash
DB_PASSWORD=yourpassword ./mvnw spring-boot:run
```

It starts on `localhost:8080` and creates the tables automatically.

## Structure

```
controller/   ApplicationController, AuthController
model/        Application, User, Status
repository/   Spring Data JPA repositories
security/     JWT utility, auth filter, security config
```

## Known limitations

- Tokens expire after 24 hours and there is no refresh
- The JWT signing key is generated at startup, so restarting the server logs everyone out
- No input validation, for example an empty email or password is not rejected properly
- Login and signup errors come back as a normal 200 response with an `error` field instead of a 4xx status
- Only the generated `contextLoads` test exists, no real tests yet
- Not deployed, it runs locally only
