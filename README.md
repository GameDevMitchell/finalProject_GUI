# SmartDrive Rentals - Car Management System

A Java Swing application for managing a car rental system with a graphical user interface.

## Features

- Add new cars to the system
- View all registered cars
- Search for cars by registration number or name
- Clean and intuitive user interface
- Persistent storage using Apache Derby database

## Prerequisites

- Java Development Kit (JDK) 8 or later
- PowerShell (included with Windows)

## Project Structure

```
finalProject_GUI/
├── build/           # Compiled .class files
├── data/            # Database files
├── lib/             # External libraries (Apache Derby)
├── src/             # Source code
│   └── com/
│       └── smartdriverentals/
│           ├── dao/            # Data Access Objects
│           ├── model/          # Data models
│           └── ui/             # User interface
├── build.ps1        # Build script
├── run.ps1          # Run script
└── README.md        # This file
```

## Building and Running

1. **Build the application**:
   ```powershell
   .\build.ps1
   ```

2. **Run the application**:
   ```powershell
   .\run.ps1
   ```

## Usage

1. **Add a new car**:
   - Fill in the car details in the form
   - Click the "Add Car" button

2. **View all cars**:
   - Click the "View All Cars" button to see all registered cars

3. **Search for a car**:
   - Enter a registration number or car name in the search field
   - Click the "Search" button

4. **Clear the form**:
   - Click the "Clear Form" button to reset all input fields

## Database

The application uses Apache Derby as an embedded database. The database files are stored in the `data/` directory and are automatically created when you run the application for the first time.

## License

This project is licensed under the MIT License.
