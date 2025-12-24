import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CarForm extends JFrame {
    private JTextField registrationField, brandField, modelField, yearField, colorField, rateField;
    private JTextArea outputArea;
    private JButton addButton, viewAllButton, clearButton;
    
    public CarForm() {
        // Set up the main frame
        setTitle("SmartDrive Rentals - Car Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null); // Center the window
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Car Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        addFormField(formPanel, gbc, "Registration Number:", 0);
        registrationField = new JTextField(20);
        addComponent(formPanel, registrationField, gbc, 1, 0);
        
        addFormField(formPanel, gbc, "Brand:", 1);
        brandField = new JTextField(20);
        addComponent(formPanel, brandField, gbc, 1, 1);
        
        addFormField(formPanel, gbc, "Model:", 2);
        modelField = new JTextField(20);
        addComponent(formPanel, modelField, gbc, 1, 2);
        
        addFormField(formPanel, gbc, "Year:", 3);
        yearField = new JTextField(20);
        addComponent(formPanel, yearField, gbc, 1, 3);
        
        addFormField(formPanel, gbc, "Color:", 4);
        colorField = new JTextField(20);
        addComponent(formPanel, colorField, gbc, 1, 4);
        
        addFormField(formPanel, gbc, "Daily Rate ($):", 5);
        rateField = new JTextField(20);
        addComponent(formPanel, rateField, gbc, 1, 5);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add Car");
        viewAllButton = new JButton("View All Cars");
        clearButton = new JButton("Clear Form");
        
        // Style buttons
        styleButton(addButton, new Color(50, 150, 50), Color.WHITE);
        styleButton(viewAllButton, new Color(70, 130, 180), Color.WHITE);
        styleButton(clearButton, new Color(220, 80, 60), Color.WHITE);
        
        // Add action listeners
        addButton.addActionListener(new AddCarListener());
        viewAllButton.addActionListener(new ViewAllCarsListener());
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(clearButton);
        
        // Create output area
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        
        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
    }
    
    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comp, gbc);
    }
    
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorderPainted(true);
        button.setOpaque(true);
    }
    
    private void clearForm() {
        registrationField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        colorField.setText("");
        rateField.setText("");
        outputArea.setText("");
        registrationField.requestFocus();
    }
    
    private class AddCarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Validate inputs
                if (registrationField.getText().trim().isEmpty() ||
                    brandField.getText().trim().isEmpty() ||
                    modelField.getText().trim().isEmpty() ||
                    yearField.getText().trim().isEmpty() ||
                    colorField.getText().trim().isEmpty() ||
                    rateField.getText().trim().isEmpty()) {
                    
                    JOptionPane.showMessageDialog(CarForm.this,
                        "Please fill in all fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Parse numeric fields
                int year = Integer.parseInt(yearField.getText().trim());
                double dailyRate = Double.parseDouble(rateField.getText().trim());
                
                // Create new Car object
                Car car = new Car(
                    registrationField.getText().trim().toUpperCase(),
                    brandField.getText().trim(),
                    modelField.getText().trim(),
                    year,
                    colorField.getText().trim(),
                    dailyRate
                );
                
                // Add to database
                boolean success = DatabaseManager.addCar(car);
                
                if (success) {
                    outputArea.setText("Car added successfully!\n\n" + car);
                    clearForm();
                } else {
                    outputArea.setText("Error: Could not add car to database.\n" +
                                     "Please check if the registration number is unique.");
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CarForm.this,
                    "Please enter valid numbers for Year and Daily Rate.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CarForm.this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private class ViewAllCarsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<Car> cars = DatabaseManager.getAllCars();
                
                if (cars.isEmpty()) {
                    outputArea.setText("No cars found in the database.");
                    return;
                }
                
                StringBuilder sb = new StringBuilder("=== ALL CARS IN INVENTORY ===\n\n");
                for (Car car : cars) {
                    sb.append(car).append("\n-----------------------------\n");
                }
                
                outputArea.setText(sb.toString());
                
            } catch (Exception ex) {
                outputArea.setText("Error retrieving cars: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            CarForm form = new CarForm();
            form.setVisible(true);
        });
    }
}
