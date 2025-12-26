public class Car {
    private String registration;
    private String brand;
    private String model;
    private int year;
    private double price;

    public Car(String registration, String brand, String model, int year, double price) {
        this.registration = registration;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getRegistration() { return registration; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return registration + " - " + brand + " " + model + " (" + year + ") - $" + price;
    }
}
