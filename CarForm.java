// CarForm.java
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarForm extends JFrame {
    private JTextField registrationField, brandField, modelField, 
                      yearField, colorField, rateField, searchField;
    private JTextArea outputArea;
    private JButton addButton, viewAllButton, clearButton, searchButton;

    public CarForm() {
        // Set up the main window
        setTitle("Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Create output area
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel and output area to frame
        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize database
        DatabaseManager.getAllCars(); // This will create the table if it doesn't exist
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Add New Car"));
        
        // Create form fields
        registrationField = createFormField("Registration Number:", panel);
        brandField = createFormField("Brand:", panel);
        modelField = createFormField("Model:", panel);
        yearField = createFormField("Year:", panel);
        colorField = createFormField("Color:", panel);
        rateField = createFormField("Daily Rate ($):", panel);
        
        return panel;
    }

    private JTextField createFormField(String label, JPanel panel) {
        JLabel jLabel = new JLabel(label);
        panel.add(jLabel);
        
        JTextField textField = new JTextField(20);
        panel.add(textField);
        
        return textField;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Search Cars"));
        
        JLabel searchLabel = new JLabel("Search by Registration or Name:");
        searchField = new JTextField(25);
        searchButton = new JButton("Search");
        
        // Style the search button
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(new SearchCarListener());
        
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Create buttons with icons
        addButton = createStyledButton("Add Car", UIManager.getIcon("OptionPane.informationIcon"));
        viewAllButton = createStyledButton("View All Cars", UIManager.getIcon("FileChooser.listViewIcon"));
        clearButton = createStyledButton("Clear Form", UIManager.getIcon("OptionPane.errorIcon"));

        // Add action listeners
        addButton.addActionListener(new AddCarListener());
        viewAllButton.addActionListener(e -> viewAllCars());
        clearButton.addActionListener(e -> clearForm());

        // Add buttons to panel
        panel.add(addButton);
        panel.add(viewAllButton);
        panel.add(clearButton);
        
        return panel;
    }

    private JButton createStyledButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
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
        List<Car> cars = DatabaseManager.getAllCars();
        if (cars.isEmpty()) {
            outputArea.setText("No cars found in the database.");
            return;
        }

        StringBuilder sb = new StringBuilder("=== All Registered Cars ===\n\n");
        for (Car car : cars) {
            sb.append(car).append("--------------------------\n");
        }
        outputArea.setText(sb.toString());
    }

    private class AddCarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get and validate input
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

                // Create and add car
                Car car = new Car(registration, brand, model, year, color, dailyRate);
                if (DatabaseManager.addCar(car)) {
                    outputArea.setText("Car added successfully:\n" + car);
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

    private class SearchCarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(CarForm.this,
                        "Please enter a search term (registration number or car name).",
                        "Search Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Car> allCars = DatabaseManager.getAllCars();
            if (allCars.isEmpty()) {
                outputArea.setText("No cars found in the database.");
                return;
            }

            StringBuilder result = new StringBuilder("=== Search Results ===\n\n");
            boolean found = false;

            for (Car car : allCars) {
                if (car.getRegistrationNumber().equalsIgnoreCase(searchTerm) ||
                    car.getBrand().equalsIgnoreCase(searchTerm) ||
                    car.getModel().equalsIgnoreCase(searchTerm)) {
                    
                    result.append(car).append("--------------------------\n");
                    found = true;
                }
            }

            if (!found) {
                outputArea.setText("No cars found matching: " + searchTerm);
            } else {
                outputArea.setText(result.toString());
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set default font
            Font font = new Font("Arial", Font.PLAIN, 14);
            UIManager.put("Button.font", font);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("TextArea.font", font);
            
            // Run the application
            SwingUtilities.invokeLater(() -> {
                CarForm form = new CarForm();
                form.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}