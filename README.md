# 🌐 Urlefy - URL Shortener

A fullstack URL Shortener built with **Spring Boot**, **MongoDB**, **Redis**, and **React (Vite)**. Works seamlessly in **GitHub Codespaces**.



## 🔧 Project Structure



url-shortener/
├── backend/          # Spring Boot backend (Java 11)
├── frontend/         # React frontend (Vite + TypeScript + SWC)
└── README.md





## 🚀 Getting Started (in GitHub Codespaces)

### ✅ 1. Clone & Open Codespace

bash
gh repo clone your-username/url-shortener


Then **open it in a GitHub Codespace**.



### ✅ 2. Run Backend

bash
cd backend
mvn clean spring-boot:run


Runs on port 8080.

> Make sure MongoDB is running (localhost:27017) and Redis is installed if caching is used.



### ✅ 3. Run Frontend

bash
cd frontend
npm install
npm run dev


Vite runs on port 5173 (or as exposed by Codespaces).



### 🧠 Backend API

* POST /api/shorten → Accepts { url, customAlias? } → Returns shortUrl
* GET /{shortCode} → Redirects to original URL if valid, otherwise 404



### ⚙️ Environment Setup (Optional)

#### MongoDB

Install if not present:

bash
sudo apt update && sudo apt install -y mongodb
sudo service mongodb start


#### Redis (optional)

bash
sudo apt install redis
sudo service redis-server start




## 🛠 Build Commands

### Backend (Maven)

bash
cd backend
mvn clean package


### Frontend (Vite + React)

bash
cd frontend
npm run build




## 🧼 Lint & Format (Frontend)

bash
npm run lint
npm run format




## 🌍 Deployment Ready

* Backend: Can be containerized using Docker
* Frontend: Can be deployed via Netlify/Vercel
* Supports port redirection fixes in Codespaces



## 📁 Notes

* React automatically adapts localhost:8080 to Codespaces proxy URL
* React frontend served separately from backend



## 📸 UI Snapshot

> Add screenshots here after polishing



## 👨‍💻 Author

Made with ❤️ by [Satyam Shishodiya](https://www.linkedin.com/in/satyam-shishodiya/)

