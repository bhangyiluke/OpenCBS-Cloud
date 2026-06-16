# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Architecture

**Monorepo** with:
- `server/opencbs-server/` - Java/Spring Boot backend (Maven)
- `client/` - Angular frontend (Angular CLI)
- `templates/` - JasperReports compiled outputs

### Backend (Java 21)
- Spring Boot 4.0.1, Spring Modulith (modular)
- PostgreSQL + Flyway migrations
- QueryDSL, MapStruct, Lombok
- JWT auth, Spring Security
- JasperReports for document generation

### Frontend (Angular)
- NgRx state management
- SLDS UI library
- Reactive forms

## Commands

### Build
```bash
# Backend (skip tests)
mvn -f server/opencbs-server clean package -DskipTests=true

# Frontend
ng build --configuration production

# Docker
./build.sh
```

### Run
```bash
# Backend (dev)
mvn -f server/opencbs-server spring-boot:run

# Docker
./run.sh
```

### Test
```bash
# Backend
mvn -f server/opencbs-server test

# Frontend
ng test
```

## File Structure

### Backend
```
server/opencbs-server/src/main/java/com/opencbs/
  ├── core/          # Core modules
  ├── borrowings/    # Loans
  ├── savings/       # Savings accounts
  ├── accounting/    # Chart of accounts
  ├── reports/       # Reporting
  └── officedocuments/# Documents
```

### Frontend
```
client/src/app/
  ├── containers/    # Pages
  ├── components/    # UI components
  ├── core/          # Shared services
  ├── store/         # NgRx
  └── shared/        # Shared modules
```

## Key Rules

### Backend
1. Services @Service, Repositories extend JpaRepository
2. @Transactional for DB operations
3. DTOs for API responses
4. Custom exceptions extending RuntimeException
5. Flyway for all schema changes

### Frontend
1. Modular components
2. NgRx patterns for state
3. Reactive forms
4. TypeScript strict typing

## Common Tasks

### Deploy
```bash
docker-compose up --build
```

### Clean
```bash
./cleans.sh
```

### Env
- `server/opencbs-server/.env` - Backend config
- `client/.env` - Frontend config

## Context

See `context.md` for current task state and recent changes.

## Workflow Rule: Iterative Chunking

### When processing large files:
1. Process ONLY the next logical chunk (e.g., 1 section or 2000 lines).
2. Write the output for that chunk to a file or display it.
3. IMMEDIATELY ask me: "Chunk [X] complete. Shall I proceed to Chunk [X+1]?"
4. Do not stop until the entire file is processed or I say "stop".