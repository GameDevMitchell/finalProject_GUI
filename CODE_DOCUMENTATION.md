# Car Rental System - Code Documentation


## Table of Contents
1. [Main.java](#mainjava)
2. [Car.java](#carjava)
3. [CarForm.java](#carformjava)
4. [DatabaseManager.java](#databasemanagerjava)
5. [How to Run](#how-to-run)

---

## Main.java
This is the starting point of our program. When you run the program, this is where it begins.

```java
// Import necessary libraries
import javax.swing.*;  // For creating windows and buttons

public class Main {
    public static void main(String[] args) {
        try {
            // Load the SQLite database driver
            Class.forName("org.sqlite.JDBC");
            // Start the car rental application
            CarForm.main(args);
        } catch (Exception e) {
            // Show error message if something goes wrong
            JOptionPane.showMessageDialog(null, 
                "Failed to start the application. Please check the database connection.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
```

## Car.java
This is  a blueprint for creating car objects. It defines what information each car should have.

```java
public class Car {
    // These are the properties every car will have
    private String registration;  // Like a car's license plate
    private String brand;         // Like Toyota, Honda, etc.
    private String model;         // The specific model name
    private int year;             // When the car was made
    private double price;         // How much it costs to rent per day
    private String color;         // The car's color
    private boolean available;    // Whether it's available to rent

    // Constructor - this is like a special method to create a new car
    public Car(String registration, String brand, String model, int year, double price) {
        this.registration = registration;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.available = true;  // New cars are available by default
        this.color = "";        // Color is optional, so it starts empty
    }

    // Getters and setters
    public String getRegistration() { return registration; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getColor() { return color; }
    public boolean isAvailable() { return available; }
    
    public void setColor(String color) { this.color = color; }
    public void setAvailable(boolean available) { this.available = available; }

    // This creates a nice string representation of the car
    @Override
    public String toString() {
        return String.format("%s %s %s (%d) - $%.2f/day - %s - %s",
            brand, model, year, price, 
            color.isEmpty() ? "No color specified" : color,
            available ? "Available" : "Rented");
    }
}
```

## CarForm.java
This creates the window where users interact with the car rental system.

```java
// Import necessary libraries
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarForm extends JFrame {
    // These are all the input fields in the form
    private JTextField regField, brandField, modelField, yearField, priceField, searchField, colorField;
    private JTextArea outputArea;  // Shows messages to the user
    private JTable carTable;       // Shows the list of cars in a table
    private DefaultTableModel tableModel;  // Manages the data in the table

    // This is the constructor that runs when we create a new CarForm
    public CarForm() {
        // Set up the window
        setTitle("Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);  // Make window bigger
        setLocationRelativeTo(null);  // Center the window

        // Create the main panel that holds everything
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Create the form for adding new cars
        createInputForm();
        
        // Create the search panel
        createSearchPanel();
        
        // Create the table to show cars
        createCarTable();
        
        // Add everything to the window
        add(mainPanel);
        
        // Load all cars when starting
        viewAllCars();
    }
    
    // Other methods for handling user interactions...
    
    public static void main(String[] args) {
        // This makes the GUI look better on different systems
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the window
        SwingUtilities.invokeLater(() -> {
            new CarForm().setVisible(true);
        });
    }
}
```

## DatabaseManager.java
This class handles all the database operations, like saving and loading cars.

```java
// Import necessary libraries
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class DatabaseManager {
    // Database connection details
    private static final String DB_URL = "jdbc:sqlite:car_rental.db";
    private static Connection conn = null;

    // This runs when the class is first loaded
    static {
        try {
            // Try to connect to the database
            conn = DriverManager.getConnection(DB_URL);
            createTableIfNotExists();  // Make sure the table exists
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Create the cars table if it doesn't exist
    private static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS cars (" +
                    "registration TEXT PRIMARY KEY, " +
                    "brand TEXT NOT NULL, " +
                    "model TEXT NOT NULL, " +
                    "year INTEGER NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "color TEXT, " +
                    "available BOOLEAN DEFAULT 1)";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            showError("Error creating table: " + e.getMessage());
        }
    }
    
    // Other database methods (addCar, getAllCars, etc.)...
}
```

## How to Run

1. **Prerequisites**:
   - Install Java JDK 8 or later
   - Download the SQLite JDBC driver (included in the `lib` folder)

2. **Compile the code**:
   ```bash
   javac -cp ".;lib/sqlite-jdbc.jar" *.java
   ```

3. **Run the application**:
   ```bash
   java -cp ".;lib/sqlite-jdbc.jar" Main
   ```

   Or use the batch files:
   - `build.bat` - Compiles the code
   - `run.bat` - Runs the application
   - `rebuild.bat` - Deletes old compiled files and recompiles

4. **Using the application**:
   - Fill in the form to add a new car
   - Click "View All Cars" to see all cars
   - Use the search box to find specific cars
   - Double-click a car in the table to see its details

## Common Issues

1. **Database Connection Error**:
   - Make sure the `lib` folder contains `sqlite-jdbc.jar`
   - Check that no other program is using the database file

2. **Class Not Found**:
   - Make sure to include the SQLite JDBC driver in the classpath
   - Check that all Java files are in the same directory

3. **Window Doesn't Appear**:
   - Check for error messages in the console
   - Make sure no other instance of the program is running - Car Rental System

## Car.java

```java
public class Car {
    // Private fields to store car information
    private String registration;  // Car registration number
    private String brand;         // Car manufacturer (Toyota, Honda, etc.)
    private String model;         // Car model (Camry, Civic, etc.)
    private int year;             // Manufacturing year
    private double price;         // Rental price per day

    // Constructor to create a new Car object
    public Car(String registration, String brand, String model, int year, double price) {
        this.registration = registration;  // Set registration number
        this.brand = brand;                // Set brand
        this.model = model;                // Set model
        this.year = year;                  // Set year
        this.price = price;                // Set price
    }

    // Getter methods to access private fields
    public String getRegistration() { return registration; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }

    // Override toString() to display car information nicely
    @Override
    public String toString() {
        return registration + " - " + brand + " " + model + " (" + year + ") - $" + price;
    }
}
```

## DatabaseManager.java

```java
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Static list to store all cars in memory
    private static List<Car> cars = new ArrayList<>();
    
    // Method to add a car to the list
    public static boolean addCar(Car car) {
        try {
            cars.add(car);  // Add car to the list
            return true;    // Return true if successful
        } catch (Exception e) {
            System.err.println("Add car error: " + e.getMessage());
            return false;   // Return false if error occurs
        }
    }

    // Method to get all cars from the list
    public static List<Car> getAllCars() {
        return new ArrayList<>(cars);  // Return a copy of the cars list
    }
}
```

## CarForm.java

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarForm extends JFrame {
    // GUI components
    private JTextField regField, brandField, modelField, yearField, priceField, searchField;
    private JTextArea outputArea;
    private JButton addButton, searchButton, clearButton;

    // Constructor to set up the GUI
    public CarForm() {
        setTitle("Car Rental System");                    // Window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on X
        setSize(500, 400);                              // Window size
        setLocationRelativeTo(null);                    // Center on screen

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        
        // Input panel for adding cars
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Car"));
        
        // Create labels and text fields for car details
        inputPanel.add(new JLabel("Registration:"));
        regField = new JTextField();                    // Registration input
        inputPanel.add(regField);
        
        inputPanel.add(new JLabel("Brand:"));
        brandField = new JTextField();                  // Brand input
        inputPanel.add(brandField);
        
        inputPanel.add(new JLabel("Model:"));
        modelField = new JTextField();                   // Model input
        inputPanel.add(modelField);
        
        inputPanel.add(new JLabel("Year:"));
        yearField = new JTextField();                    // Year input
        inputPanel.add(yearField);
        
        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();                   // Price input
        inputPanel.add(priceField);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);                // Search input
        searchPanel.add(searchField);
        searchButton = new JButton("Search");            // Search button
        searchButton.addActionListener(e -> searchCars()); // Action when clicked
        searchPanel.add(searchButton);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Car");              // Add button
        addButton.addActionListener(e -> addCar());       // Action when clicked
        clearButton = new JButton("Clear");              // Clear button
        clearButton.addActionListener(e -> clearFields()); // Action when clicked
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        // Output area to display results
        outputArea = new JTextArea(8, 40);               // Text area for output
        outputArea.setEditable(false);                   // Make it read-only
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));

        // Add all panels to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.EAST);

        add(mainPanel);                                  // Add main panel to frame
        outputArea.setText("Welcome to Car Rental System!\nAdd cars or search for existing ones.");
    }

    // Method to add a car
    private void addCar() {
        try {
            // Get input from text fields
            String reg = regField.getText().trim();
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());  // Convert to int
            double price = Double.parseDouble(priceField.getText().trim()); // Convert to double

            // Validate inputs
            if (reg.isEmpty() || brand.isEmpty() || model.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            // Create car object and add to database
            Car car = new Car(reg, brand, model, year, price);
            if (DatabaseManager.addCar(car)) {
                outputArea.setText("Car added:\n" + car);
                clearFields();  // Clear input fields
            } else {
                outputArea.setText("Failed to add car!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year or price!");
        }
    }

    // Method to search for cars
    private void searchCars() {
        String search = searchField.getText().trim();    // Get search term
        List<Car> cars = DatabaseManager.getAllCars();    // Get all cars
        
        StringBuilder result = new StringBuilder("Search Results:\n");
        boolean found = false;
        
        // Search through all cars
        for (Car car : cars) {
            if (car.getRegistration().toLowerCase().contains(search.toLowerCase()) ||
                car.getBrand().toLowerCase().contains(search.toLowerCase()) ||
                car.getModel().toLowerCase().contains(search.toLowerCase())) {
                result.append(car.toString()).append("\n");
                found = true;
            }
        }
        
        if (!found) {
            result.append("No cars found!");
        }
        
        outputArea.setText(result.toString());           // Display results
    }

    // Method to clear all input fields
    private void clearFields() {
        regField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        searchField.setText("");
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarForm().setVisible(true));
    }
}
```

## Main.java

```java
public class Main {
    public static void main(String[] args) {
        CarForm.main(args);  // Simply call CarForm's main method
    }
}
```


1. **Car.java**: Defines what a car is (registration, brand, model, year, price)
2. **DatabaseManager.java**: Stores cars in memory using an ArrayList
3. **CarForm.java**: Creates the GUI window with input fields, buttons, and display area
4. **Main.java**: Entry point that starts the application

