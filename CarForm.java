import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarForm extends JFrame {
    private JTextField registrationField, brandField, modelField, yearField, colorField, rateField, searchField;
    private JTextArea outputArea;
    private JButton addButton, viewAllButton, clearButton, searchButton;
    private JPanel mainPanel, formPanel, buttonPanel, searchPanel;

    public CarForm() {
        // Set up the main window
        setTitle("SmartDrive Rentals - Car Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 700));
        setLocationRelativeTo(null);

        // Main container with some padding
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Create form panel
        createFormPanel();
        // Create search panel
        createSearchPanel();
        // Create button panel
        createButtonPanel();
        // Create output area
        createOutputArea();

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Display the window
        pack();
    }

    private void createFormPanel() {
        formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Car"));
        formPanel.setBackground(Color.WHITE);

        // Create form fields
        registrationField = createFormField("Registration Number:", formPanel);
        brandField = createFormField("Brand:", formPanel);
        modelField = createFormField("Model:", formPanel);
        yearField = createFormField("Year:", formPanel);
        colorField = createFormField("Color:", formPanel);
        rateField = createFormField("Daily Rate ($):", formPanel);
    }

    private JTextField createFormField(String label, JPanel panel) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(jLabel);
        
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(textField);
        
        return textField;
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Cars"));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search by Registration or Name:");
        searchField = new JTextField(25);
        searchButton = new JButton("Search");
        
        // Style the search button
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(new SearchCarListener());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        // Create buttons with icons
        addButton = createStyledButton("Add Car", UIManager.getIcon("OptionPane.informationIcon"));
        viewAllButton = createStyledButton("View All Cars", UIManager.getIcon("FileChooser.listViewIcon"));
        clearButton = createStyledButton("Clear Form", UIManager.getIcon("OptionPane.errorIcon"));

        // Add action listeners
        addButton.addActionListener(new AddCarListener());
        viewAllButton.addActionListener(e -> viewAllCars());
        clearButton.addActionListener(e -> clearForm());

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(clearButton);
    }

    private JButton createStyledButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        return button;
    }

    private void createOutputArea() {
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputArea.setBackground(new Color(250, 250, 250));
        outputArea.setText("Welcome to SmartDrive Rentals Car Management System!\n\n" +
                "To get started:\n" +
                "1. Fill in the car details and click 'Add Car' to save a new car\n" +
                "2. Use the search box to find specific cars\n" +
                "3. Click 'View All Cars' to see all registered cars\n" +
                "4. Use 'Clear Form' to reset the input fields");
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
            sb.append(String.format("""
                    Registration: %s
                    Brand: %s
                    Model: %s
                    Year: %d
                    Color: %s
                    Daily Rate: $%.2f
                    Available: %s
                    --------------------------
                    """,
                    car.getRegistrationNumber(),
                    car.getBrand(),
                    car.getModel(),
                    car.getYear(),
                    car.getColor(),
                    car.getDailyRate(),
                    car.isAvailable() ? "Yes" : "No"
            ));
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
                Car car = new Car(registration, brand, model, year, color, dailyRate, true);
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
                    
                    result.append(String.format("""
                            Registration: %s
                            Brand: %s
                            Model: %s
                            Year: %d
                            Color: %s
                            Daily Rate: $%.2f
                            Available: %s
                            --------------------------
                            """,
                            car.getRegistrationNumber(),
                            car.getBrand(),
                            car.getModel(),
                            car.getYear(),
                            car.getColor(),
                            car.getDailyRate(),
                            car.isAvailable() ? "Yes" : "No"
                    ));
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the application
        SwingUtilities.invokeLater(() -> {
            CarForm form = new CarForm();
            form.setVisible(true);
        });
    }
}