# 🍲 Food Waste Reduction System - Backend API

Production-ready REST API built with **Java 21 / Spring Boot 3.5**, **PostgreSQL**, **Spring Data JPA**, and **Spring Security Crypto (BCrypt)**, designed for deployment on **Render**.

[![Render](https://img.shields.io/badge/Deploy-Render-black.svg)](https://render.com/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://www.oracle.com/java/)

---

## 🚀 1-Click Deployment to Render (with PostgreSQL)

### Method 1: Using Render Blueprint (`render.yaml`) - Recommended
1. Go to your [Render Dashboard](https://dashboard.render.com/).
2. Click **New +** $\rightarrow$ **Blueprint**.
3. Connect your repository: `https://github.com/benkireddyabhinaya-ux/food_backend`.
4. Render detects `render.yaml` and provisions:
   - **PostgreSQL Database**: `food-waste-postgres`
   - **Dockerized Web Service**: `food-waste-backend`
5. Click **Apply**.
6. When deployment finishes, copy your live backend URL: `https://food-backend-hix6.onrender.com`.

### Method 2: Manual Web Service + PostgreSQL Setup
1. Create a **PostgreSQL Database** on Render:
   - Click **New +** $\rightarrow$ **PostgreSQL**.
   - Database Name: `food_waste_db`.
   - Copy the **Internal Database URL** (or External URL).
2. Create a **Web Service** on Render:
   - Click **New +** $\rightarrow$ **Web Service**.
   - Connect repository `food_backend`.
   - Environment: `Docker`.
   - Environment Variables:
     - `PORT`: `8081`
     - `DATABASE_URL`: *(Paste your Render PostgreSQL connection string)*
     - `CORS_ALLOWED_ORIGINS`: `*`
3. Click **Deploy**.

---

## 📡 Key Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/health` | Health check probe for Render zero-downtime |
| `GET` | `/swagger-ui.html` | Interactive Swagger / OpenAPI documentation |
| `POST` | `/api/users/login` | User authentication with BCrypt |
| `POST` | `/api/users/register` | Register Donor or NGO organization |
| `GET` | `/api/donations/available` | Live available surplus food catalog |
| `POST` | `/api/donations` | Post surplus food donation |
| `POST` | `/api/pickups` | Schedule pickup with driver assignment |
| `POST` | `/api/pickups/{id}/verify` | Verify donor 4-digit PIN & mark delivery completed |
| `GET` | `/api/stats/summary` | Real-time impact stats (meals saved, kg diverted, $CO_2$) |

---

## 🧪 Local Run
```powershell
mvn spring-boot:run
```
- API Base: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
