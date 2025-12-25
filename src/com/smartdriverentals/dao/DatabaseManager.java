package com.smartdriverentals.dao;

import com.smartdriverentals.model.Car;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:derby:data/smartdrive_rentals;create=true";
    private static final String CREATE_TABLE = """
            CREATE TABLE cars (
                registration_number VARCHAR(20) PRIMARY KEY,
                brand VARCHAR(50) NOT NULL,
                model VARCHAR(50) NOT NULL,
                year INT NOT NULL,
                color VARCHAR(30) NOT NULL,
                daily_rate DOUBLE NOT NULL,
                available BOOLEAN DEFAULT true
            )""";

    private static final String INSERT_CAR = """
            INSERT INTO cars (registration_number, brand, model, year, color, daily_rate)
            VALUES (?, ?, ?, ?, ?, ?)""";

    private static final String GET_ALL_CARS = "SELECT * FROM cars";

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Check if table exists
            try {
                stmt.execute("SELECT 1 FROM cars");
            } catch (SQLException e) {
                // Table doesn't exist, create it
                stmt.execute(CREATE_TABLE);
            }
        } catch (SQLException e) {
            showError("Error initializing database: " + e.getMessage());
            e.printStackTrace();
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

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            showError("Error adding car: " + e.getMessage());
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
                        rs.getDouble("daily_rate"));
                car.setAvailable(rs.getBoolean("available"));
                cars.add(car);
            }
        } catch (SQLException e) {
            showError("Error retrieving cars: " + e.getMessage());
            e.printStackTrace();
        }

        return cars;
    }

    private static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            showError("Could not connect to the database. Error: " + e.getMessage());
            throw e;
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
