# build.ps1
$ErrorActionPreference = "Stop"

# Set paths
$srcDir = "src"
$buildDir = "build"
$libDir = "lib"
$classpath = ".;$libDir\*"

# Create build directory if it doesn't exist
if (-not (Test-Path $buildDir)) {
    New-Item -ItemType Directory -Path $buildDir | Out-Null
}

# Compile Java files
Write-Host "Compiling Java files..."
javac -d $buildDir -cp $classpath $srcDir\com\smartdriverentals\model\*.java $srcDir\com\smartdriverentals\dao\*.java $srcDir\com\smartdriverentals\ui\*.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed"
    exit 1
}

Write-Host "Build completed successfully!"
