import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:derby:cardb;create=true";
    
    static {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            try {
                stmt.executeQuery("SELECT * FROM cars LIMIT 1");
            } catch (SQLException e) {
                stmt.execute("CREATE TABLE cars (" +
                           "registration VARCHAR(20) PRIMARY KEY, " +
                           "brand VARCHAR(50), " +
                           "model VARCHAR(50), " +
                           "year INT, " +
                           "price DOUBLE)");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static boolean addCar(Car car) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO cars VALUES (?, ?, ?, ?, ?)")) {
            
            pstmt.setString(1, car.getRegistration());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setDouble(5, car.getPrice());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add car error: " + e.getMessage());
            return false;
        }
    }

    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars")) {
            
            while (rs.next()) {
                cars.add(new Car(
                    rs.getString("registration"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get cars error: " + e.getMessage());
        }
        return cars;
    }
}
