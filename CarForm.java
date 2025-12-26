import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarForm extends JFrame {
    private JTextField regField, brandField, modelField, yearField, priceField, searchField;
    private JTextArea outputArea;
    private JButton addButton, searchButton, clearButton;

    public CarForm() {
        setTitle("Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Car"));
        
        inputPanel.add(new JLabel("Registration:"));
        regField = new JTextField();
        inputPanel.add(regField);
        
        inputPanel.add(new JLabel("Brand:"));
        brandField = new JTextField();
        inputPanel.add(brandField);
        
        inputPanel.add(new JLabel("Model:"));
        modelField = new JTextField();
        inputPanel.add(modelField);
        
        inputPanel.add(new JLabel("Year:"));
        yearField = new JTextField();
        inputPanel.add(yearField);
        
        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchCars());
        searchPanel.add(searchButton);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Car");
        addButton.addActionListener(e -> addCar());
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        // Output area
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.EAST);

        add(mainPanel);
        outputArea.setText("Welcome to Car Rental System!\nAdd cars or search for existing ones.");
    }

    private void addCar() {
        try {
            String reg = regField.getText().trim();
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (reg.isEmpty() || brand.isEmpty() || model.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            Car car = new Car(reg, brand, model, year, price);
            if (DatabaseManager.addCar(car)) {
                outputArea.setText("Car added:\n" + car);
                clearFields();
            } else {
                outputArea.setText("Failed to add car!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year or price!");
        }
    }

    private void searchCars() {
        String search = searchField.getText().trim();
        List<Car> cars = DatabaseManager.getAllCars();
        
        StringBuilder result = new StringBuilder("Search Results:\n");
        boolean found = false;
        
        for (Car car : cars) {
            if (car.getRegistration().toLowerCase().contains(search.toLowerCase()) ||
                car.getBrand().toLowerCase().contains(search.toLowerCase()) ||
                car.getModel().toLowerCase().contains(search.toLowerCase())) {
                result.append(car.toString()).append("\n");
                found = true;
            }
        }
        
        if (!found) {
            result.append("No cars found!");
        }
        
        outputArea.setText(result.toString());
    }

    private void clearFields() {
        regField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarForm().setVisible(true));
    }
}
