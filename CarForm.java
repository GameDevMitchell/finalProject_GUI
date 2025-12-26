import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CarForm extends JFrame {
    private JTextField registrationField, brandField, modelField, yearField, colorField, rateField;
    private JTextArea outputArea;
    private JButton addButton, viewAllButton, clearButton;

    public CarForm() {
        setTitle("SmartDrive Rentals - Car Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Car"));

        formPanel.add(new JLabel("Registration Number:"));
        registrationField = new JTextField();
        formPanel.add(registrationField);

        formPanel.add(new JLabel("Brand:"));
        brandField = new JTextField();
        formPanel.add(brandField);

        formPanel.add(new JLabel("Model:"));
        modelField = new JTextField();
        formPanel.add(modelField);

        formPanel.add(new JLabel("Year:"));
        yearField = new JTextField();
        formPanel.add(yearField);

        formPanel.add(new JLabel("Color:"));
        colorField = new JTextField();
        formPanel.add(colorField);

        formPanel.add(new JLabel("Daily Rate ($):"));
        rateField = new JTextField();
        formPanel.add(rateField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Car");
        viewAllButton = new JButton("View All Cars");
        clearButton = new JButton("Clear Form");

        addButton.addActionListener(new AddCarListener());
        viewAllButton.addActionListener(e -> viewAllCars());
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(clearButton);

        // Output area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        outputArea.setText("Welcome to SmartDrive Rentals!\n\n" +
                         "1. Fill in car details and click 'Add Car'\n" +
                         "2. Click 'View All Cars' to see all registered cars\n" +
                         "3. Use 'Clear Form' to reset fields");
    }

    private void clearForm() {
        registrationField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        colorField.setText("");
        rateField.setText("");
        registrationField.requestFocus();
    }

    private void viewAllCars() {
        try {
            java.util.List<Car> cars = DatabaseManager.getAllCars();
            if (cars.isEmpty()) {
                outputArea.setText("No cars found in the database.");
            } else {
                StringBuilder sb = new StringBuilder("=== All Registered Cars ===\n\n");
                for (int i = 0; i < cars.size(); i++) {
                    sb.append("Car ").append(i + 1).append(":\n");
                    sb.append(cars.get(i).toString()).append("\n");
                    sb.append("--------------------------\n");
                }
                outputArea.setText(sb.toString());
            }
        } catch (Exception e) {
            outputArea.setText("Error retrieving cars: " + e.getMessage());
        }
    }

    private class AddCarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String registration = registrationField.getText().trim();
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String color = colorField.getText().trim();
                double dailyRate = Double.parseDouble(rateField.getText().trim());

                if (registration.isEmpty() || brand.isEmpty() || model.isEmpty() || color.isEmpty()) {
                    JOptionPane.showMessageDialog(CarForm.this,
                            "Please fill in all fields.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Car car = new Car(registration, brand, model, year, color, dailyRate);
                if (DatabaseManager.addCar(car)) {
                    outputArea.setText("Car added successfully!\n\n" + car.toString());
                    clearForm();
                } else {
                    outputArea.setText("Failed to add car. Please try again.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CarForm.this,
                        "Please enter valid numbers for year and daily rate.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CarForm.this,
                        "An error occurred: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CarForm().setVisible(true);
        });
    }
}
