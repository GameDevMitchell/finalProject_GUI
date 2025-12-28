public class Car {
    private String registration;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String color;
    private boolean available;

    public Car(String registration, String brand, String model, int year, double price) {
        this(registration, brand, model, year, price, "", true);
    }

    public Car(String registration, String brand, String model, int year, double price, String color,
            boolean available) {
        this.registration = registration;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color != null ? color : "";
        this.available = available;
    }

    // Getters
    public String getRegistration() {
        return registration;
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

    public double getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setColor(String color) {
        this.color = color;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("""
                Registration: %s
                Brand: %s
                Model: %s
                Year: %d
                Price: $%.2f
                Color: %s
                Available: %s
                """,
                registration, brand, model, year, price,
                color.isEmpty() ? "Not specified" : color,
                available ? "Yes" : "No");
    }

    public String toTableRow() {
        return String.format("%-15s %-15s %-15s %-6d $%-10.2f %-10s %s",
                registration, brand, model, year, price,
                color.isEmpty() ? "-" : color,
                available ? "Yes" : "No");
    }

    public static String getTableHeader() {
        return String.format("%-15s %-15s %-15s %-6s %-11s %-10s %s",
                "REGISTRATION", "BRAND", "MODEL", "YEAR", "PRICE", "COLOR", "AVAILABLE") +
                "\n" + "-".repeat(90);
    }
}
