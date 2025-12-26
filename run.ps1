# run.ps1 - Simple run script for Car Management System
$ErrorActionPreference = "Stop"

# Run the application
Write-Host "Starting SmartDrive Rentals Car Management System..."
java -cp build Main
