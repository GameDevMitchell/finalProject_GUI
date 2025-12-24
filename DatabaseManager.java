import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DatabaseManager {
    // Database connection details for JavaDB (Derby)
    private static final String DB_NAME = "smartdrive_rentals";
    private static final String URL = "jdbc:derby:" + DB_NAME + ";create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    // SQL queries
    private static final String CREATE_TABLE = """
            CREATE TABLE cars (
                registration_number VARCHAR(20) PRIMARY KEY,
                brand VARCHAR(50) NOT NULL,
                model VARCHAR(50) NOT NULL,
                year INT NOT NULL,
                color VARCHAR(30) NOT NULL,
                daily_rate DOUBLE NOT NULL,
                is_available BOOLEAN DEFAULT true
            )""";

    // Load the Derby JDBC driver when the class is loaded
    static {
        try {
            Class.forName(DRIVER);
            initializeDatabase();
        } catch (Exception e) {
            showError("Failed to initialize database: " + e.getMessage());
        }
    }

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
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check if table exists
            try {
                stmt.execute("SELECT 1 FROM cars");
                // Table exists, no need to create
            } catch (SQLException e) {
                // Table doesn't exist, create it
                stmt.execute(CREATE_TABLE);
            }
            
        } catch (SQLException e) {
            showError("Error initializing database: " + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            showError("Could not connect to the database. Error: " + e.getMessage());
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
