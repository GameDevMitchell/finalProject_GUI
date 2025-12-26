import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:derby:smartdrive_db;create=true";
    private static Connection connection;

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            
            // Try to select from cars table, if it doesn't exist create it
            try {
                stmt.execute("SELECT 1 FROM cars LIMIT 1");
            } catch (SQLException e) {
                // Table doesn't exist, create it
                stmt.execute("CREATE TABLE cars (" +
                           "registration_number VARCHAR(20) PRIMARY KEY, " +
                           "brand VARCHAR(50) NOT NULL, " +
                           "model VARCHAR(50) NOT NULL, " +
                           "year INT NOT NULL, " +
                           "color VARCHAR(30) NOT NULL, " +
                           "daily_rate DOUBLE NOT NULL)");
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean addCar(Car car) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
            
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO cars (registration_number, brand, model, year, color, daily_rate) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
            
            pstmt.setString(1, car.getRegistrationNumber());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setString(5, car.getColor());
            pstmt.setDouble(6, car.getDailyRate());
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cars");
            
            while (rs.next()) {
                Car car = new Car(
                    rs.getString("registration_number"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getString("color"),
                    rs.getDouble("daily_rate")
                );
                cars.add(car);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cars;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
