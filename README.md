# Urlefy – Distributed URL Shortener

Urlefy is a high-performance, distributed URL shortener built with Java, Spring Boot, Redis, MongoDB, Docker, and Nginx.

## 🚀 Features

- Shorten long URLs with optional custom aliases
- Redirect to original URLs with HTTP 302
- Expiry support for shortened URLs
- Redis caching for high performance (10k+ req/sec)
- Analytics (click tracking, timestamps)
- Fully containerized with Docker
- Nginx reverse proxy for load balancing

## 🧰 Tech Stack

- **Backend**: Java, Spring Boot
- **Caching**: Redis
- **Database**: MongoDB
- **DevOps**: Docker, Docker Compose
- **Proxy**: Nginx

## 📁 Project Structure

```
src/
  main/
    java/com/urlefy/
      controller/
      model/
      service/
      repository/
  resources/
    application.yml
Dockerfile
docker-compose.yml
nginx.conf
```

## 🛠️ Build and Run Locally

### Prerequisites
- Java 17+
- Maven
- Docker & Docker Compose

### Steps
1. Build the project:
    ```bash
    mvn clean package
    ```
2. Run with Docker:
    ```bash
    docker-compose up --build
    ```

## 📦 API Endpoints

### POST `/api/shorten`
- **Body**:
  ```json
  {
    "url": "https://example.com",
    "customAlias": "myalias"
  }
  ```
- **Response**:
  ```
  http://localhost:8080/myalias
  ```

### GET `/{code}`
- Redirects to original URL if code is valid.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
