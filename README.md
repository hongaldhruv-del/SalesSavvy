# SalesSavvy - Run Instructions

## Prerequisites

- Java 17 or higher
- Node.js and npm
- MySQL Server
- Maven (included via Maven Wrapper)

## Database Setup

1. Ensure MySQL Server is running
2. Create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS salessavvy;
   ```
3. Run the database setup script:
   - Open MySQL Workbench or MySQL command line
   - Execute the script: `SalesSavvyBackend-main/database_setup.sql`
   - This will create all required tables

4. Update database credentials in `SalesSavvyBackend-main/src/main/resources/application.properties`:
   - `spring.datasource.username` - Your MySQL username
   - `spring.datasource.password` - Your MySQL password

## Running the Application

### Backend (Spring Boot)

1. Navigate to the backend directory:
   ```bash
   cd SalesSavvyBackend-main
   ```

2. Start the backend server:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
   
   Or use the batch file:
   ```bash
   .\start_backend.bat
   ```

3. The backend will start on `http://localhost:9090`

### Frontend (React/Vite)

1. Navigate to the frontend directory:
   ```bash
   cd SALESSAVVY-main
   ```

2. Install dependencies (first time only):
   ```bash
   npm install
   ```

3. Start the frontend development server:
   ```bash
   npm run dev
   ```
   
   Or use the batch file:
   ```bash
   .\start_frontend.bat
   ```

4. The frontend will start on `http://localhost:5174`

## Access the Application

- **Frontend**: Open your browser and navigate to `http://localhost:5174`
- **Backend API**: Available at `http://localhost:9090`

## Notes

- Ensure MySQL is running before starting the backend
- The backend must be running for the frontend to function properly
- Both servers should be running simultaneously for the application to work

