# Backend

This is the backend for a multi-tenant fleet management system, built with NestJS, Prisma, and PostgreSQL.

## Features

*   **Multi-tenancy:** The application is designed to serve multiple tenants, with data isolation between them.
*   **User Authentication:** Secure user authentication using JWT (JSON Web Tokens).
*   **User & Role Management:** Different user roles (SUPER_ADMIN, ADMIN, DRIVER) with corresponding permissions.
*   **Fleet Management:**
    *   Manage trucks, including their plate numbers, models, and status.
    *   Manage drivers, including their license numbers and contact information.
*   **Operations Management:**
    *   Track trips with details like origin, destination, price, and status.
    *   Record fuel expenses for each truck.
    *   Log maintenance records for each truck.
    *   Manage other expenses related to the fleet.
*   **Customizable Settings:** Tenants can configure their own settings, such as distance units, fuel units, timezones, and currency.

## Technologies Used

*   **Framework:** [NestJS](https://nestjs.com/)
*   **ORM:** [Prisma](https://www.prisma.io/)
*   **Database:** [PostgreSQL](https://www.postgresql.org/)
*   **Authentication:** [Passport](http://www.passportjs.org/) with JWT

## Getting Started

### Prerequisites

*   Node.js (v18 or higher)
*   npm
*   PostgreSQL

### Installation

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2.  Navigate to the backend directory:
    ```bash
    cd backend
    ```
3.  Install dependencies:
    ```bash
    npm install
    ```
4.  Set up your environment variables by creating a `.env` file in the `backend` directory. You will need to provide a `DATABASE_URL`.
    ```
    DATABASE_URL="postgresql://user:password@localhost:5432/mydatabase"
    ```
5.  Run database migrations:
    ```bash
    npx prisma migrate dev
    ```

## Compile and run the project

```bash
# development
$ npm run start

# watch mode
$ npm run start:dev

# production mode
$ npm run start:prod
```

## Run tests

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```
