# Athlete's Edge - The All-in-One Performance Platform



**Athlete's Edge** is a comprehensive, microservices-based application designed to empower athletes by providing them with tools to track, analyze, and optimize their performance, recovery, and overall well-being. Built with a robust backend using **Java, Spring Boot, and Kafka**, and a dynamic frontend with **React and TypeScript**, this project showcases a modern, scalable, and event-driven architecture.

---

## 🌟 Key Features

- **Personalized Dashboard:** A central hub for athletes to get an "at-a-glance" overview of their daily readiness, stats, and recent activities.
- **AI-Powered Coach:** A smart assistant, powered by the Groq API (Llama3), that provides daily, context-aware training recommendations and a live chat interface.
- **Dynamic Workout Planner:** An intelligent planner that generates weekly, personalized training plans based on the athlete's goals and experience level.
- **Holistic Tracking:**
    - **Workout & Nutrition Logger:** Log detailed workout sessions and daily nutritional intake.
    - **Readiness Score:** Track daily recovery metrics like sleep, soreness, and mood to get a scientific readiness score (0-100%).
- **Gamification System:** An RPG-like experience where athletes choose an avatar, earn XP for activities, level up, and unlock skills.
- **Performance Analysis:**
    - **Personal Records (PRs) Tracker:** A "Trophy Cabinet" to log and celebrate personal bests.
    - **Goal Setting:** Set and track progress against custom goals (e.g., "Complete 20 workouts this month").
- **Interactive Journey Timeline:** A visual, scrollable timeline of the athlete's entire history, showcasing PRs, completed goals, and milestones.
- **Real-time Injury Risk Alerts:** Uses the ACWR (Acute:Chronic Workload Ratio) model to predict injury risk and sends real-time notifications to the UI using Server-Sent Events (SSE).

---

## 🛠️ Tech Stack & Architecture

This project is built on a **microservices architecture**, with each service designed for a specific business capability. The services communicate asynchronously via **Apache Kafka** and are discoverable through a **Eureka Service Registry**.

### Backend (`Java & Spring Boot`)
- **Spring Cloud:** API Gateway, Eureka Discovery
- **Spring Boot:** For building individual microservices
- **Spring Data JPA & PostgreSQL:** Database persistence
- **Spring Security & JWT:** Authentication and authorization
- **Apache Kafka:** Asynchronous, event-driven communication
- **WebClient (WebFlux):** For reactive, non-blocking inter-service communication
- **Groq API (Llama3):** For AI-powered features
- **Docker & Docker Compose:** Containerization and orchestration

### Frontend (`React & TypeScript`)
- **React (Vite):** A fast and modern UI library
- **TypeScript:** For type safety and robust code
- **Tailwind CSS:** For rapid, utility-first styling
- **React Router:** For client-side routing
- **Axios & Fetch API:** For communicating with the backend
- **Recharts:** For interactive data visualization
- **Server-Sent Events (SSE):** For real-time notifications

### Architecture Diagram


---

## 🚀 Getting Started Locally

To run the entire platform on your local machine, you'll need Git, Java 17+, Maven, Node.js, and Docker installed.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/Athletes-Edge.git
    cd Athletes-Edge
    ```
2.  **Set up environment variables:**
    Create a `.env` file in the root directory and add your Groq API key:
    ```
    GROQ_API_KEY=your_groq_api_key_here
    ```
3.  **Build all backend services:**
    Run the Maven package command for each backend microservice to generate the `.jar` files.
    ```bash
    # For each service, e.g., user-service
    (cd user-service && mvn clean package -DskipTests)
    # ... repeat for all 13 backend services
    ```
4.  **Run the entire system with Docker Compose:**
    This single command will build all Docker images and start all containers.
    ```bash
    docker-compose up --build
    ```
5.  **Access the application:**
    - **Frontend:** `http://localhost:3000`
    - **Eureka Discovery Server:** `http://localhost:8761`

---

## 👨‍💻 Author

- **Aaditya**
- **GitHub:** `https://github.com/heyimaaditya`
- **LinkedIn:** `https://linkedin.com/in/aaditya27`