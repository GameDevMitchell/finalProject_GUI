// Car.java
public class Car {
    private String registrationNumber;
    private String brand;
    private String model;
    private int year;
    private String color;
    private double dailyRate;
    private boolean available;

    public Car(String registrationNumber, String brand, String model, 
              int year, String color, double dailyRate) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.dailyRate = dailyRate;
        this.available = true;
    }

    // Getters and setters
    public String getRegistrationNumber() { return registrationNumber; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getColor() { return color; }
    public double getDailyRate() { return dailyRate; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("""
            Registration: %s
            Brand: %s
            Model: %s
            Year: %d
            Color: %s
            Daily Rate: $%.2f
            Available: %s
            """, 
            registrationNumber, brand, model, year, color, 
            dailyRate, available ? "Yes" : "No");
    }
}