# 🏋️‍♀️ Fitness Microservice Application

A full-stack fitness tracking platform built with a modern microservices architecture, leveraging Spring Boot, React, and AI for personalized activity insights.

## 📌 Overview

This project demonstrates the development of a scalable, secure, and cloud-ready fitness application using Java and modern web technologies. It includes user management, activity tracking, AI-powered recommendations, and secure authentication.

## 🧰 Tech Stack

- **Backend**: Spring Boot, Spring Cloud, JPA, RabbitMQ, Eureka, Config Server  
- **Frontend**: React.js, Redux Toolkit  
- **Security**: Keycloak, OAuth 2.0 (PKCE Flow)  
- **AI Integration**: Gemini API  
- **Cloud**: AWS, Docker-ready  

## 🧩 Microservices

- **User Service**: Handles user profiles and Keycloak ID synchronization  
- **Activity Service**: Captures fitness activities and streams data to AI service  
- **AI Service**: Processes activity data and generates personalized fitness recommendations  
- **API Gateway**: Centralized routing and security enforcement  
- **Frontend**: React dashboard for activity input, visualization, and insights  

## 🔐 Authentication & Authorization

- Integrated with **Keycloak** for identity and access management  
- Uses **OAuth 2.0 PKCE** flow for secure frontend authentication  
- APIs secured via token-based access  

## 🐇 Running RabbitMQ (Docker)
Access RabbitMQ UI: http://localhost:15672
<pre lang="bash"><code> docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management </code></pre>

## ▶️ Getting Started
  
```bash
# Backend
./mvnw clean install
docker-compose up  # if using Docker for services like RabbitMQ, Keycloak

# Frontend
cd frontend
npm install
npm start

