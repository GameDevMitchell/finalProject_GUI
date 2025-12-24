import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DatabaseManager {
    // Database connection details - update these with your MySQL credentials
    private static final String URL = "jdbc:mysql://localhost:3306/smartdrive_rentals?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Leave empty if no password

    // SQL queries
    private static final String CREATE_TABLE = """n            CREATE TABLE IF NOT EXISTS cars (
                registration_number VARCHAR(20) PRIMARY KEY,
                brand VARCHAR(50) NOT NULL,
                model VARCHAR(50) NOT NULL,
                year INT NOT NULL,
                color VARCHAR(30) NOT NULL,
                daily_rate DOUBLE NOT NULL,
                is_available BOOLEAN DEFAULT true
            )""";

    private static final String INSERT_CAR = """n            INSERT INTO cars (registration_number, brand, model, year, color, daily_rate, is_available)
            VALUES (?, ?, ?, ?, ?, ?, ?)""";

    private static final String GET_ALL_CARS = "SELECT * FROM cars";

    // Load the MySQL JDBC driver when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            showError("MySQL JDBC Driver not found. Please add it to your project's classpath.");
        } catch (Exception e) {
            showError("Failed to initialize database: " + e.getMessage());
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create the database if it doesn't exist
            stmt.execute("CREATE DATABASE IF NOT EXISTS smartdrive_rentals");
            stmt.execute("USE smartdrive_rentals");
            
            // Create the cars table if it doesn't exist
            stmt.executeUpdate(CREATE_TABLE);
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        try {
            // Try to establish a connection with a 5-second timeout
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            showError("Could not connect to the database. Please check if MySQL is running and the credentials are correct.\n\nError: " + e.getMessage());
            throw e;
        }
    }

    // Add a new car to the database
    public static boolean addCar(Car car) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_CAR)) {
            
            pstmt.setString(1, car.getRegistrationNumber());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setString(5, car.getColor());
            pstmt.setDouble(6, car.getDailyRate());
            pstmt.setBoolean(7, car.isAvailable());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Car added successfully: " + car.getRegistrationNumber());
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
            showError("Database error: " + e.getMessage());
            return false;
        }
    }

    // Get all cars from the database
    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_CARS)) {
            
            while (rs.next()) {
                Car car = new Car(
                    rs.getString("registration_number"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getString("color"),
                    rs.getDouble("daily_rate")
                );
                car.setAvailable(rs.getBoolean("is_available"));
                cars.add(car);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving cars: " + e.getMessage());
            showError("Error retrieving cars: " + e.getMessage());
        }
        
        return cars;
    }
}
