# OpenCBS Cloud Sacco Management System - Developer Guide

## Overview
This is a mission-critical open-source sacco (cooperative society) management system built with modern enterprise Java/Spring Boot backend and Angular frontend technologies. The system supports core financial operations including accounting, lending/borrowing, savings, and reporting functionalities essential for cooperative financial institutions.

## Architecture

### Backend (Java/Spring Boot)
- **Framework**: Spring Boot 4.0.1 with Java 21
- **Database**: PostgreSQL with Flyway for migrations
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Security**: Spring Security with JWT authentication
- **Modules**: Modularized using Spring Modulith

### Frontend (Angular)
- **Framework**: Angular with TypeScript
- **UI Library**: Lightning Design System (SLDS)
- **State Management**: NgRx (Redux-like approach)
- **Build Tool**: Angular CLI

## Key Functional Areas

### 1. Accounting & Chart of Accounts
- Full chart of accounts management with hierarchical tree structure
- Account types: Categories, Subgroups, and Balance accounts
- Branch-level account management
- Comprehensive account properties including debit/credit flags, transfer permissions, etc.

### 2. Borrowing/Lending System
- Complete loan lifecycle management
- Installment scheduling with accrual calculations
- Event-based transaction system (disbursement, repayment, interest accrual)
- Detailed borrowing status tracking
- Support for various loan products and configurations

### 3. Reporting & Templates
- JasperReports integration for professional document generation
- Multiple report formats (PDF, Excel, etc.)
- Template-based reporting system with dynamic parameters
- Pre-configured financial reports for sacco operations

### 4. Core Features
- User roles and permissions management
- Branch-level operations and filtering
- Multi-currency support
- Audit trails and event logging
- Integration capabilities

## Development Best Practices

### Code Quality
- Strict adherence to enterprise Java coding standards
- Comprehensive unit and integration testing
- Lombok for reducing boilerplate code
- ModelMapper for object mapping
- Proper exception handling with custom exceptions
- DTO (Data Transfer Objects) pattern for API responses

### Security
- JWT-based authentication and authorization
- Role-based access control (RBAC)
- Input validation and sanitization
- Secure password handling
- CSRF protection

### Performance & Scalability
- Database indexing strategies
- Caching mechanisms where applicable
- Asynchronous processing for long-running operations
- Efficient query patterns with JPA/Hibernate
- Modular architecture for scalability

### Testing Strategy
- Unit tests for business logic
- Integration tests for service layers
- Repository layer tests
- End-to-end tests for critical flows
- Test coverage monitoring

## Technical Rules & Constraints

### Backend Rules
1. All services must be properly annotated with `@Service`
2. Repository interfaces extend Spring Data JPA repositories
3. Entities must be properly mapped with JPA annotations
4. DTOs should be used for API responses to prevent data leakage
5. Business logic must be encapsulated in services, not controllers
6. All database transactions must be explicitly managed with `@Transactional`
7. Custom exceptions should extend `RuntimeException` or specific base exceptions
8. Use of functional programming concepts where appropriate

### Frontend Rules
1. All components must be modular and reusable
2. State management follows NgRx patterns
3. Reactive forms for all user input
4. Type-safe components with TypeScript interfaces
5. Component-driven architecture with proper separation of concerns
6. Internationalization (i18n) support for multi-language environments
7. Responsive design principles

### Database Standards
1. All schema changes must be implemented via Flyway migration scripts
2. Proper foreign key constraints and relationships
3. Indexing strategy for frequently queried columns
4. Schema versioning and backward compatibility
5. Data integrity through constraints and triggers

### Deployment
1. Docker containerization support
2. Environment-specific configuration files
3. Automated build and deployment pipelines
4. Health check endpoints
5. Logging and monitoring integration

## Project Structure

### Server (Backend)
```
server/
├── opencbs-server/
│   ├── src/main/java/com/opencbs/
│   │   ├── core/           # Core modules
│   │   ├── borrowings/     # Loan/borrowing functionality
│   │   ├── savings/        # Savings account functionality  
│   │   ├── accounting/     # Accounting and chart of accounts
│   │   ├── reports/        # Reporting functionality
│   │   └── officedocuments/ # Document generation and templates
│   └── src/main/resources/
│       ├── db/migration/   # Database migrations
│       ├── templates/      # Jasper report templates
│       └── application.yaml # Configuration
```

### Client (Frontend)
```
client/
├── src/app/
│   ├── containers/         # Main application pages
│   ├── components/         # Reusable UI components
│   ├── core/               # Shared services and modules
│   └── store/              # NgRx state management
└── src/assets/             # Static assets and icons
```

## Key Technologies Used

### Backend
- Spring Boot 4.0.1
- Spring Data JPA
- Spring Security
- Spring Modulith (modular architecture)
- Flyway (database migrations)
- JasperReports (reporting)
- MapStruct (object mapping)
- Lombok (code reduction)
- ModelMapper (DTO mapping)
- Jakarta Validation API

### Frontend
- Angular 17+
- TypeScript
- NgRx (state management)
- SLDS (Salesforce Lightning Design System)
- RxJS (reactive programming)
- SCSS (CSS preprocessor)

## Coding Standards

### Java
- Follow Google Java Style Guide
- Consistent naming conventions (camelCase for variables, PascalCase for classes)
- Proper documentation with JavaDoc
- Use of interfaces for loose coupling
- Immutable objects where appropriate
- Proper exception handling

### Angular
- Component-based architecture
- Strict typing with TypeScript
- Reactive programming with RxJS
- Proper component lifecycle management
- Service-oriented architecture
- Modular structure with feature modules

## Error Handling
- Centralized exception handling with `@ControllerAdvice`
- Specific exception types for different scenarios
- Meaningful error messages for debugging
- Logging of errors with appropriate severity levels
- Graceful degradation in case of failures

## Monitoring & Logging
- Structured logging with Logback
- Application metrics collection
- Health endpoint monitoring
- Transaction tracing
- Audit logging for sensitive operations

## Contributing Guidelines
1. All code changes must go through pull requests
2. Code reviews required for all changes
3. Pass all automated tests before merging
4. Maintain backward compatibility where possible
5. Update documentation with code changes
6. Follow semantic versioning for releases
7. Proper commit message conventions (Imperative mood, 50 characters max subject)