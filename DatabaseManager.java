import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/smartdrive_rentals";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Set your database password here

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

    static {
        initializeDatabase();
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
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

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
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
            e.printStackTrace();
        }
        
        return cars;
    }
}
