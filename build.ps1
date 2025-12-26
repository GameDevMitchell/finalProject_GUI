# build.ps1 - Simple build script for Car Management System
$ErrorActionPreference = "Stop"

# Create build directory if it doesn't exist
if (-not (Test-Path "build")) {
    New-Item -ItemType Directory -Path "build" | Out-Null
}

# Compile all Java files
Write-Host "Compiling Java files..."
javac -d build *.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed"
    exit 1
}

Write-Host "Build completed successfully!"
