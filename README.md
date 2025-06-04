# 🌐 URL Shortener

```
A full-stack URL shortener built with:

-  Backend: Spring Boot + MongoDB + Redis
-  Frontend: React.js + Tailwind CSS + Vite
-  Dockerized environment
-  Developed in GitHub Codespaces
````

---

```
## 🚀 Features

-  Shorten long URLs to short, unique codes
-  Instant redirection using Redis cache
-  Persistent storage with MongoDB
-  Responsive UI using Tailwind CSS
-  Easy containerization with Docker Compose
````
---

## 🛠 Project Structure

```

url-shortener/
├── backend/              # Spring Boot API
├── frontend/             # React UI (Vite + Tailwind)
├── docker-compose.yml    # Combined setup
└── .gitignore            # Clean Git tracking

````

---

## ⚙️ Prerequisites

```
- Docker & Docker Compose
- Node.js (for local frontend dev, optional)
- Java 17 (for standalone backend dev)
````

---

## 🔧 Running the Project

### ✅ Option 1: One Command (Recommended)
```bash
docker-compose up --build -d
````

* Runs MongoDB, Redis, and Spring Boot backend
* Frontend runs on Codespaces preview or localhost

> Visit: `http://localhost:8080` (or your Codespaces frontend preview URL)

---

## 🖥 Frontend (Dev Mode)

```bash
cd frontend
npm install
npm run dev
```

---

## 🧪 Backend (Standalone)

```bash
cd backend
mvn spring-boot:run
```

---

## 📦 API Endpoints

### `POST /api/shorten`

```json
{
  "url": "https://example.com"
}
```

→ Returns: `{ "shortUrl": "http://localhost:8080/abc123" }`

### `GET /{shortCode}`

→ Redirects to the original URL.

---

## ✅ Built With

* Spring Boot 2.7+
* MongoDB & Redis
* React 18 + Vite
* Tailwind CSS
* Docker & Docker Compose

---

## 🙌 Author

Built with ❤️ by [Satyam Shishodiya](https://www.linkedin.com/in/satyam-shishodiya/)

---

## 📄 License

This project is licensed under the MIT License.