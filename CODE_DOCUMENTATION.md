# Code Documentation - Car Rental System

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

