# run.ps1
$ErrorActionPreference = "Stop"

# Set paths
$buildDir = "build"
$libDir = "lib"
$classpath = "$buildDir;$libDir\*"

# Create data directory if it doesn't exist
if (-not (Test-Path "data")) {
    New-Item -ItemType Directory -Path "data" | Out-Null
}

# Run the application
Write-Host "Starting SmartDrive Rentals Car Management System..."
java -cp $classpath com.smartdriverentals.ui.CarForm
