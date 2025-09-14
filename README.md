# Mysterria API Gateway

Spring Cloud Gateway application built with Gradle that routes traffic to main and archive backends with advanced features.

## Features

- **Route Management**: Routes `/api/*` to main backend, `/archive/*` to archive backend
- **Rate Limiting**: IP-based rate limiting (100 requests/second, 200 burst)
- **Circuit Breaker**: Automatic failover with fallback endpoints
- **Request/Response Logging**: Comprehensive request tracking with unique IDs
- **Authentication**: Basic auth for admin endpoints
- **Health Checks**: Application and service monitoring
- **Metrics**: Prometheus metrics available

## Quick Start

1. Configure environment variables in `.env`
2. Run with Docker Compose:
   ```bash
   docker-compose up -d
   ```

## Configuration

Edit `.env` file:
- `MAIN_BACKEND_URL`: Main API backend URL
- `ARCHIVE_BACKEND_URL`: Archive API backend URL
- `REDIS_PASSWORD`: Redis authentication password
- `ADMIN_USERNAME/ADMIN_PASSWORD`: Admin credentials

## Endpoints

- `/api/**` → Routes to main backend
- `/archive/**` → Routes to archive backend
- `/actuator/health` → Health check (public)
- `/actuator/**` → Admin metrics (requires auth)

## Monitoring

- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Prometheus: `http://localhost:8080/actuator/prometheus`

## Circuit Breaker

Fallback responses available at:
- `/fallback/main` → Main service unavailable
- `/fallback/archive` → Archive service unavailable