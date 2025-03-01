# Parent-Teacher Collaboration Platform - Backend ⚙️

This is the backend for the **Parent-Teacher Collaboration Platform**, built using **Spring Boot, MySQL, and WebSocket**. It provides REST APIs for authentication, event management, real-time chat, and report sharing.

## Frontend App Link
[PTCP Frontend App](https://github.com/vinodjatav/ptcp-frontend-app)

## 🚀 Features

✅ **User Authentication** - JWT-based authentication with Spring Security  
✅ **Role-Based Access Control (RBAC)** - Admin, Teacher, Parent permissions  
✅ **Event Scheduling** - Quartz Scheduler for event reminders  
✅ **Real-Time Messaging** - Spring WebSocket for instant chat between users  
✅ **Progress Tracking & Report Sharing** - File upload using Spring Content  
✅ **Email & SMS Notifications** - Send reminders for scheduled events

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, WebSocket, Spring Content
- **Database:** MySQL (or PostgreSQL)
- **Authentication:** JWT (JSON Web Token)
- **Messaging:** Spring WebSocket, STOMP, SockJS
- **Scheduling:** Quartz Scheduler for event reminders
- **File Storage:** Local storage
- **Build Tool:** Maven

## 🔧 Setup & Installation

1️⃣ Clone the frontend repository  
```sh
git clone https://github.com/vinodjatav/ptcp-frontend-app.git
cd ptcp-frontend-app
npm i
npm start
```
2️⃣ Clone the backend repository  
```sh
git clone https://github.com/vinodjatav/ptcp-backend-app.git
cd ptcp-backend-app
# Build and run the backend application  
./mvnw clean install  
./mvnw spring-boot:run 
