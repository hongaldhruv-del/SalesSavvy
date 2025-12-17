@echo off
echo Starting SalesSavvy Backend Application...
echo.
echo Make sure MySQL is running and the database 'salessavvy' exists.
echo.
cd /d "%~dp0"
call mvnw.cmd spring-boot:run
pause

