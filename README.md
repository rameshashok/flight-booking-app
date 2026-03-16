# SkyBook — Flight Booking App

A full-stack flight booking web application built with **Spring Boot**, **Angular 18**, **Angular Material**, **PostgreSQL**, and **Docker Compose**.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 18, Angular Material, TypeScript |
| Backend | Spring Boot 3.3, Spring Data JPA, Java 21 |
| Database | PostgreSQL 16 |
| Containerization | Docker, Docker Compose |
| Web Server | Nginx (serves Angular + proxies API) |
| Monitoring | Prometheus, Grafana, Loki, Jaeger (OTel) |

---

## Features

- Search flights by origin, destination, and date
- View available flights with price, duration, and seat count
- Book a flight via a dialog form (name, email, seats)
- Look up all bookings by email
- Cancel a booking (automatically restores seat availability)
- Auto-seeded sample flight data on first startup

---

## Project Structure

```
flight-booking-app/
├── backend/                        Spring Boot application
│   ├── src/main/java/com/example/flightbooking/
│   │   ├── config/                 CORS configuration
│   │   ├── controller/             REST controllers
│   │   │   ├── FlightController    GET /api/flights, GET /api/flights/search
│   │   │   └── BookingController   POST /api/bookings, GET /api/bookings, PUT /api/bookings/{id}/cancel
│   │   ├── model/                  JPA entities (Flight, Booking)
│   │   ├── repository/             Spring Data JPA repositories
│   │   └── service/                Business logic + seed data
│   ├── src/main/resources/
│   │   └── application.yaml        App configuration
│   └── Dockerfile
├── frontend/                       Angular application
│   ├── src/app/
│   │   ├── components/
│   │   │   ├── flight-search/      Home page search form
│   │   │   ├── flight-list/        Search results with Book button
│   │   │   ├── booking-dialog/     Booking modal form
│   │   │   └── my-bookings/        View & cancel bookings by email
│   │   ├── models/                 TypeScript interfaces
│   │   └── services/               HTTP client service
│   ├── nginx.conf                  Nginx config (SPA + API proxy)
│   ├── proxy.conf.json             Dev proxy to backend
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Node.js 20+](https://nodejs.org/) *(for local dev only)*
- [Java 21+](https://adoptium.net/) *(for local dev only)*

---

### Run with Docker (Recommended)

```bash
docker-compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost:4200 |
| Backend API | http://localhost:8080/api |
| PostgreSQL | localhost:5432 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (admin / admin) |
| Jaeger UI | http://localhost:16686 |
| Loki | http://localhost:3100 |

To stop:
```bash
docker-compose down
```

To stop and remove the database volume:
```bash
docker-compose down -v
```

---

### Run Locally (Development)

**1. Start PostgreSQL only via Docker:**
```bash
docker-compose up postgres
```

**2. Run the backend:**
```bash
cd backend
./mvnw spring-boot:run
```

**3. Run the frontend:**
```bash
cd frontend
npm install
npm start
```

The Angular dev server proxies `/api` requests to `http://localhost:8080` via `proxy.conf.json`.

Open http://localhost:4200

---

## API Reference

### Flights

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/flights` | Get all flights from DB |
| `GET` | `/api/flights/search?depIata=&arrIata=` | Search real-time flights via aviationstack |

**Search example:**
```
GET /api/flights/search?depIata=JFK&arrIata=LAX
```

> Flight search uses the [aviationstack](https://aviationstack.com/) free tier API (`/v1/flights`). Date-based search requires a paid plan. Requires a valid `AVIASTACK_API_KEY`.

### Bookings

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/bookings` | Create a booking |
| `GET` | `/api/bookings?email=` | Get bookings by passenger email |
| `PUT` | `/api/bookings/{id}/cancel` | Cancel a booking |

**Create booking request body:**
```json
{
  "flightId": 1,
  "passengerName": "John Doe",
  "passengerEmail": "john@example.com",
  "seats": 2
}
```

---

## Configuration

Environment variables used by the backend (set in `docker-compose.yml` or your environment):

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_NAME` | `flightdb` | Database name |
| `DB_USER` | `postgres` | Database username |
| `DB_PASS` | `postgres` | Database password |
| `AVIASTACK_API_KEY` | *(required)* | aviationstack API key — get one free at [aviationstack.com](https://aviationstack.com/) |

---

## Screenshots

### Search Flights
> Enter origin, destination, and travel date to find available flights.

### Flight Results
> Browse results showing flight number, times, duration, price, and available seats.

### Book a Flight
> Fill in passenger details and number of seats in the booking dialog.

### My Bookings
> Enter your email to view all bookings and cancel if needed.

---

## License

This project is licensed under the [MIT License](LICENSE).
