# Stable Reserve - Backend API

The backend REST API for Stable Reserve, built with Java and Spring Boot. It handles data persistence, JWT authentication, role-based authorization, and automated background tasks.

## 🚀 Tech Stack
*   **Framework:** Spring Boot 3.x
*   **Language:** Java 17+ 
*   **Security:** Spring Security + JWT
*   **Data Access:** Spring Data JPA / Hibernate
*   **Utilities:** Lombok, JavaMailSender
*   **Task Scheduling:** Spring `@Scheduled`

## ⚙️ Core Services
*   **Email Service:** Asynchronously sends invitations, password resets, and automated overdue horse alerts.
*   **Notification Scheduler:** A midnight cron job that scans all stables, checks custom user preferences, and compiles a bundled list of overdue Coggins/Farrier alerts for stable managers.
*   **Invite System:** Handles the generation and validation of invites for users to join specific stables with assigned roles.

## 🛠️ Local Development Setup

### Prerequisites
*   Java 17 or higher
*   Maven
*   PostgreSQL

### Environment Variables
Configure your `application.properties` or `application.yml` with the following variables before starting the application:

```properties
# Database Config
spring.datasource.url=jdbc:mysql://localhost:3306/stable_manager
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# Frontend URL
frontend.url=your_frontend_url

# JWT Secret Key
jwt.secret=your_super_secret_jwt_key

# Mail Server Config (e.g., Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_app_password
```

### Running the Application
Run the application using Maven from the root directory of the backend:
```bash
./mvnw spring-boot:run
```

## 📚 API Endpoints

> **Note:** Unless otherwise specified, all endpoints require a valid **JWT bearer token** for authorization.

---

### 🔐 Authentication
**Base path:** `/api/auth`
*(Public endpoints — no token required)*

* **`POST`** `/register` — Register a new user account.
* **`POST`** `/login` — Authenticate a user and return a JWT token.
* **`POST`** `/forgot-password` — Send a password reset email.
* **`POST`** `/reset-password` — Reset a user's password using a valid reset token.

### 👤 Users
**Base path:** `/api/users`

* **`GET`** `/` — Retrieve all users.
* **`GET`** `/me` — Get the authenticated user's profile.
* **`PUT`** `/me` — Update the authenticated user's account information.
* **`DELETE`** `/{id}` — Delete a user by ID.

### 🛖 Stables
**Base path:** `/api/stables`

* **`GET`** `/` — Retrieve all stables the authenticated user belongs to.
* **`POST`** `/` — Create a new stable.
* **`GET`** `/{id}` — Retrieve a stable by ID.
* **`PUT`** `/{id}` — Update an existing stable.
* **`DELETE`** `/{id}` — Delete a stable.
* **`GET`** `/{stableId}/all` — Retrieve all users belonging to a stable.
* **`POST`** `/invites/{stableId}` — Send an invitation to join a stable.
* **`GET`** `/invites/me` — Retrieve all pending invitations for the authenticated user.
* **`PUT`** `/invites/{stableId}/accept` — Accept or decline a stable invitation.
* **`DELETE`** `/kick/{userId}/{stableId}` — Remove a user from a stable.

### 🐎 Horses
**Base path:** `/api/horses`

* **`GET`** `/{stableId}` — Retrieve all horses in a stable.
* **`POST`** `/{stableId}` — Create a new horse in a stable.
* **`GET`** `/{stableId}/{id}` — Retrieve a specific horse by ID.
* **`PUT`** `/{stableId}/{id}` — Update an existing horse.
* **`DELETE`** `/{stableId}/{id}` — Delete a horse from a stable.