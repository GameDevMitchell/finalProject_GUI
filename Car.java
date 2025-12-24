public class Car {
    private String registrationNumber;
    private String brand;
    private String model;
    private int year;
    private String color;
    private double dailyRate;
    private boolean isAvailable;

    // Constructor
    public Car(String registrationNumber, String brand, String model, int year, 
              String color, double dailyRate) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.dailyRate = dailyRate;
        this.isAvailable = true; // By default, a new car is available
    }

    // Getters and Setters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return String.format("Car Details:%n" +
                           "Registration: %s%n" +
                           "Brand: %s%n" +
                           "Model: %s%n" +
                           "Year: %d%n" +
                           "Color: %s%n" +
                           "Daily Rate: $%.2f%n" +
                           "Available: %s",
                registrationNumber, brand, model, year, color, dailyRate, 
                isAvailable ? "Yes" : "No");
    }
}
