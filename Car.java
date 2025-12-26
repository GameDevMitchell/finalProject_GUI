public class Car {
    private String registrationNumber;
    private String brand;
    private String model;
    private int year;
    private String color;
    private double dailyRate;

    public Car(String registrationNumber, String brand, String model, int year, String color, double dailyRate) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.dailyRate = dailyRate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    @Override
    public String toString() {
        return "Registration: " + registrationNumber + 
               "\nBrand: " + brand + 
               "\nModel: " + model + 
               "\nYear: " + year + 
               "\nColor: " + color + 
               "\nDaily Rate: $" + dailyRate;
    }
}
