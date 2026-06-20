# CollegeConnect Backend

CollegeConnect is a complete social media backend application built with **Spring Boot** tailored for university/college campuses. It enables students to connect with college peers, share documents/study materials, ask questions, list and claim textbooks, create polls, check out fests, and view official circulars.

---

## Technical Stack & Architecture

- **Core Framework**: Java 21 & Spring Boot 3.3.0
- **Security**: Spring Security + JWT Authentication
- **Database**: PostgreSQL (Containerized)
- **Caching**: Redis Cache (Containerized)
- **Local Storage**: Local filesystem directory with Docker Volume mapping (for document uploads)
- **Containerization**: Docker & Docker Compose

```
Client (request.http) ──[REST API]──> Spring Boot App
                                        ├── [JPA] ────> PostgreSQL
                                        ├── [Cache] ──> Redis Cache
                                        └── [Upload] ─> ./uploads (Docker Volume)
```

---

## Core Features

1. **User Authentication & Profiles**: Security using JWT, password hashing via BCrypt, and customizable academic profiles.
2. **Peer Connections**: Request-response connection system allowing students to build networks.
3. **Forum (Ask for Help)**: Category-filtered Q&A forum where students can post help requests and discuss responses.
4. **Document Share**: Safe note/material uploads and downloads that preserve original file names.
5. **Textbook Exchange**: Marketplace for listing books for sale/sharing and marking them as claimed.
6. **Peer Polls**: Voting system that prevents duplicate votes and automatically aggregates tallies.
7. **Fest & Circulars**: Notice board entries backed by Redis caching to ensure quick lookup times.

---

## Getting Started (Single-Command Run)

Make sure you have **Docker** and **Docker Compose** installed on your system.

To spin up the entire ecosystem (PostgreSQL, Redis, and the Spring Boot application itself), run the following command from the project root:

```bash
docker compose up --build
```

### What this command does:
1. Boots up a `postgres:15-alpine` container, initializes the `collegeconnect` database, and runs database health checks.
2. Boots up a `redis:7-alpine` container for caching.
3. Runs a multi-stage Docker build to build the Spring Boot application JAR using Maven and Java 21, then runs the JRE-optimized runtime container exposing port `8080`.
4. Creates a local folder named `./uploads` mapping it into the container so document uploads persist across builds.

---

## Testing API Endpoints

A comprehensive integration test suite is provided in the **[request.http](file:///Users/deepawasthi/Developer/CollegeConnect/request.http)** file.

### How to use:
1. Open the [request.http](file:///Users/deepawasthi/Developer/CollegeConnect/request.http) file in your IDE (IntelliJ IDEA, VS Code with REST Client, or similar).
2. Execute the requests sequentially:
   - **Step 1**: Register two peers, Alice and Bob.
   - **Step 2**: Log in as Alice and Bob. The client script automatically parses the JSON response and stores the authentication JWT tokens (`userA_token`, `userB_token`) globally.
   - **Step 3**: Test connection requests, accepting connections, discussion comments, file uploading and downloading, book listing and claiming, poll voting (with duplicate validation checks), and fests/circulars (with Redis caching logs).

---

## REST Endpoints Reference

| Endpoint | Method | Security | Description |
|---|---|---|---|
| `/api/auth/register` | `POST` | Public | Register a new user |
| `/api/auth/login` | `POST` | Public | Authenticate user & return JWT |
| `/api/auth/me` | `GET` | Authenticated | Retrieve current user profile |
| `/api/peers/profile/{id}` | `GET` | Authenticated | View other student's profile |
| `/api/peers/connect/{id}` | `POST` | Authenticated | Send peer connection request |
| `/api/peers/accept/{id}` | `POST` | Authenticated | Accept a pending peer request |
| `/api/peers/reject/{id}` | `POST` | Authenticated | Reject a pending peer request |
| `/api/peers/connections` | `GET` | Authenticated | Fetch list of your connections |
| `/api/peers/requests` | `GET` | Authenticated | Fetch list of your pending requests |
| `/api/posts` | `POST` | Authenticated | Create a discussion/help request |
| `/api/posts` | `GET` | Authenticated | Get all posts (optional `?category=`) |
| `/api/posts/{id}/comments` | `POST` | Authenticated | Answer/Comment on a post |
| `/api/posts/{id}/comments` | `GET` | Authenticated | List comments for a post |
| `/api/documents/upload` | `POST` | Authenticated | Upload a file (multipart-form) |
| `/api/documents` | `GET` | Authenticated | Retrieve shared documents |
| `/api/documents/download/{id}` | `GET` | Authenticated | Download original shared file |
| `/api/books` | `POST` | Authenticated | List a textbook for sale/share |
| `/api/books` | `GET` | Authenticated | Get available textbooks |
| `/api/books/{id}/claim` | `POST` | Authenticated | Claim/Buy a textbook listing |
| `/api/polls` | `POST` | Authenticated | Create a multiple choice poll |
| `/api/polls` | `GET` | Authenticated | Get all active polls |
| `/api/polls/{id}/vote` | `POST` | Authenticated | Vote on a poll choice |
| `/api/fests` | `POST` | Authenticated | Add a college fest detail |
| `/api/fests` | `GET` | Authenticated | Get fests list (Cached in Redis) |
| `/api/circulars` | `POST` | Authenticated | Add a college circular |
| `/api/circulars` | `GET` | Authenticated | Get circulars list (Cached in Redis) |
