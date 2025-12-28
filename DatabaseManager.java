import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:car_rental.db";
    private static Connection conn = null;

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create database connection
            conn = DriverManager.getConnection(DB_URL);

            // Create cars table if it doesn't exist
            String sql = """
                        CREATE TABLE IF NOT EXISTS cars (
                            registration TEXT PRIMARY KEY,
                            brand TEXT NOT NULL,
                            model TEXT NOT NULL,
                            year INTEGER NOT NULL,
                            price REAL NOT NULL,
                            color TEXT,
                            available BOOLEAN DEFAULT 1
                        )
                    """;

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean addCar(Car car) {
        String sql = """
                    INSERT INTO cars(registration, brand, model, year, price, color, available)
                    VALUES(?,?,?,?,?,?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, car.getRegistration());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setDouble(5, car.getPrice());
            pstmt.setString(6, car.getColor());
            pstmt.setBoolean(7, car.isAvailable());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
            return false;
        }
    }

    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Car car = new Car(
                        rs.getString("registration"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price"));
                car.setColor(rs.getString("color"));
                car.setAvailable(rs.getBoolean("available"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cars: " + e.getMessage());
        }

        return cars;
    }

    public static List<Car> searchCars(String query) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE registration LIKE ? OR brand LIKE ? OR model LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Car car = new Car(
                        rs.getString("registration"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price"));
                car.setColor(rs.getString("color"));
                car.setAvailable(rs.getBoolean("available"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }

        return cars;
    }

    public static Car getCar(String registration) {
        String sql = "SELECT * FROM cars WHERE registration = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registration);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Car car = new Car(
                        rs.getString("registration"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price"));
                car.setColor(rs.getString("color"));
                car.setAvailable(rs.getBoolean("available"));
                return car;
            }
        } catch (SQLException e) {
            System.err.println("Error getting car: " + e.getMessage());
        }

        return null;
    }
}
