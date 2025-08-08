# 🎮 Game Library API – Spring Boot + MongoDB

Game Library API is a **Java Spring Boot REST application** that manages players, games, collections, and player-game statuses, with **MongoDB** for persistence.
Built for demonstrating **backend API design, database integration, and validation**.

---

## 🚀 Key Features

* **Players Management** – Create, read, update, delete player profiles.
* **Games Management** – CRUD for games with genre, platform, release date.
* **Collections** – Player-specific game collections with add/remove support.
* **Player-Game Status** – Track progress (Not Started, Playing, Completed, Abandoned).
* **Validation** – Jakarta Bean Validation for request data integrity.
* **Centralized Error Handling** – Consistent API error responses.
* **Unit Testing** – JUnit 5 + Mockito for service layer.

---

## 🛠 Tech Stack

**Backend:** Java 17, Spring Boot 3.x, Spring Data MongoDB, Jakarta Bean Validation
**Database:** MongoDB
**Testing:** JUnit 5, Mockito
**Version Control:** Git & GitHub

---

## 🗄 Database Collections

**players** – Player accounts with username, email, birth date
**games** – Game records with title, genre, platform, release date
**collections** – Player-specific named game lists
**playerGames** – Player-game status mappings

---

## 📂 Structure

src/main/java/…/player – Player, GameCollection, PlayerGame entities, repos, services, controllers
src/main/java/…/game – Game entity, repo, service, controller
src/main/java/…/exception – Global and validation exception handlers
src/test/java – Unit tests
resources/ – application.yml, sample JSON data

---

## ⚙️ Setup

1. Clone repo → `git clone https://github.com/YOUR_USERNAME/game-library-api.git`
2. Configure `application.yml` MongoDB URI
3. (Optional) Import sample JSON data using `mongoimport`
4. Run with `./mvnw spring-boot:run`
5. Access API at `http://localhost:8080`

---

## 📸 Sample Endpoints

**List Players**
`GET /api/players`
**Add Game**
`POST /api/games`

---

## 🔄 Flow

Client (HTTP Request) → Spring Boot Controller → Service Layer → MongoDB Repository → ResponseEntity (JSON).

---

**Author:** Anas Sadek
