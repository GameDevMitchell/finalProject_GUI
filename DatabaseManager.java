import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static List<Car> cars = new ArrayList<>();
    
    public static boolean addCar(Car car) {
        try {
            cars.add(car);
            return true;
        } catch (Exception e) {
            System.err.println("Add car error: " + e.getMessage());
            return false;
        }
    }

    public static List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }
}
