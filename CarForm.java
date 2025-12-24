
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
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 243, 248)); // Light blue-gray background

        // Form panel where we'll put all the input fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                "  CAR DETAILS  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(50, 100, 150)));
        formPanel.setBackground(new Color(255, 255, 255, 240)); // Slightly transparent white
        formPanel.setOpaque(true);
        formPanel.setPreferredSize(new Dimension(750, 300));

        // This helps us position everything neatly in the form
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 10); // More padding around elements

        // Add all the form fields with proper spacing and styling
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Registration Number
        addFormField(formPanel, gbc, "Registration Number:", 0);
        registrationField = createStyledTextField(20, fieldFont);
        addComponent(formPanel, registrationField, gbc, 1, 0);

        // Brand
        addFormField(formPanel, gbc, "Brand:", 1);
        brandField = createStyledTextField(20, fieldFont);
        addComponent(formPanel, brandField, gbc, 1, 1);

        // Model
        addFormField(formPanel, gbc, "Model:", 2);
        modelField = createStyledTextField(20, fieldFont);
        addComponent(formPanel, modelField, gbc, 1, 2);

        // Year
        addFormField(formPanel, gbc, "Year:", 3);
        yearField = createStyledTextField(20, fieldFont);
        addComponent(formPanel, yearField, gbc, 1, 3);

        // Color
        addFormField(formPanel, gbc, "Color:", 4);
        colorField = createStyledTextField(20, fieldFont);
        addComponent(formPanel, colorField, gbc, 1, 4);

        // Daily Rate
        addFormField(formPanel, gbc, "Daily Rate ($):", 5);
        rateField = createStyledTextField(20, fieldFont);
        addComponent(formPanel, rateField, gbc, 1, 5);

        // Make the labels look consistent
        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setFont(labelFont);
                label.setForeground(new Color(60, 60, 60));
            }
        }

        // Button panel with GridLayout for consistent button sizes
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(240, 243, 248));

        // Add some padding around the button panel
        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setBackground(new Color(240, 243, 248));
        buttonContainer.add(buttonPanel, BorderLayout.CENTER);
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50)); // Left and right margins

        // Create buttons with consistent styling and icons
        addButton = createStyledButton("  Add Car  ", new Color(46, 125, 50), Color.WHITE,
                UIManager.getIcon("OptionPane.informationIcon"));
        viewAllButton = createStyledButton("  View All Cars  ", new Color(26, 115, 232), Color.WHITE,
                UIManager.getIcon("FileChooser.listViewIcon"));
        clearButton = createStyledButton("  Clear Form  ", new Color(183, 28, 28), Color.WHITE,
                UIManager.getIcon("OptionPane.errorIcon"));

        // Make buttons respond to mouse hover
        setupButtonHoverEffect(addButton, new Color(56, 142, 60));
        setupButtonHoverEffect(viewAllButton, new Color(41, 121, 255));
        setupButtonHoverEffect(clearButton, new Color(198, 40, 40));

        // What happens when you click the buttons
        addButton.addActionListener(new AddCarListener());
        viewAllButton.addActionListener(new ViewAllCarsListener());
        clearButton.addActionListener(e -> clearForm());

        // Add some keyboard shortcuts for power users
        setupKeyboardShortcuts();

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(viewAllButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(clearButton);

        // Make sure the buttons are always visible and properly sized
        Dimension buttonSize = new Dimension(180, 45);
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setPreferredSize(buttonSize);
                comp.setMinimumSize(buttonSize);
                comp.setMaximumSize(buttonSize);
                ((JButton) comp).setFocusPainted(false);
            }
        }

        // Output area where we'll show results and messages
        outputArea = new JTextArea(12, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(new Color(253, 253, 253));
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        // Make the output area scrollable
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "  System Output  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70, 130, 180)));
        scrollPane.setBackground(Color.WHITE);

        // Add components to main panel with proper spacing
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 243, 248));
        topPanel.add(formPanel, BorderLayout.NORTH);

        // Add some vertical space between form and buttons
        topPanel.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonContainer, BorderLayout.CENTER);

        // Add the output area in a scroll pane at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    // Helper method to add a label to the form
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(labelComponent, gbc);
    }

    // Position a component in the grid layout
    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Makes components expand horizontally
        panel.add(comp, gbc);
    }

    // Create a nicely styled button with hover effects and icon
    private JButton createStyledButton(String text, Color bgColor, Color fgColor, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add some space between icon and text
        button.setIconTextGap(8);

        // Set a fixed size for all buttons
        Dimension buttonSize = new Dimension(180, 45);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // Make buttons change color when hovered over
    private void setupButtonHoverEffect(JButton button, Color hoverColor) {
        Color normalColor = button.getBackground();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createLineBorder(hoverColor.darker(), 1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setBorder(BorderFactory.createLineBorder(normalColor.darker(), 1));
            }
        });
    }

    // Create a styled text field with consistent look and feel
    private JTextField createStyledTextField(int columns, Font font) {
        JTextField field = new JTextField(columns);
        field.setFont(font);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return field;
    }

    // Add keyboard shortcuts for better usability
    private void setupKeyboardShortcuts() {
        // Pressing Enter in any field will click the Add button
        ActionListener enterAction = e -> addButton.doClick();
        registrationField.addActionListener(enterAction);
        brandField.addActionListener(enterAction);
        modelField.addActionListener(enterAction);
        yearField.addActionListener(enterAction);
        colorField.addActionListener(enterAction);
        rateField.addActionListener(enterAction);

        // Escape key clears the form
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clearForm");
        getRootPane().getActionMap().put("clearForm", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // F5 refreshes the car list
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refreshList");
        getRootPane().getActionMap().put("refreshList", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllButton.doClick();
            }
        });
    }

    // Clear all input fields and focus on the first field
    private void clearForm() {
        // Clear text fields
        registrationField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        colorField.setText("");
        rateField.setText("");

        // Clear output but keep any important messages
        if (!outputArea.getText().contains("successfully") &&
                !outputArea.getText().startsWith("Error")) {
            outputArea.setText("");
        }

        // Put focus back on the first field
        registrationField.requestFocus();
    }

    // Handles the Add Car button click
    private class AddCarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // First, check if all required fields are filled
                if (isAnyFieldEmpty()) {
                    showError("Please fill in all fields.", "Missing Information");
                    return;
                }

                // Try to parse the numbers, will throw exception if invalid
                int year = Integer.parseInt(yearField.getText().trim());
                double dailyRate = Double.parseDouble(rateField.getText().trim());

                // Basic validation for year (assuming cars from 1900 to next year)
                int currentYear = java.time.Year.now().getValue();
                if (year < 1900 || year > currentYear + 1) {
                    showError("Please enter a valid year between 1900 and " + (currentYear + 1) + ".",
                            "Invalid Year");
                    return;
                }

                // Create the car object with the entered data
                Car car = new Car(
                        registrationField.getText().trim().toUpperCase(),
                        capitalizeFirstLetter(brandField.getText().trim()),
                        capitalizeFirstLetter(modelField.getText().trim()),
                        year,
                        capitalizeFirstLetter(colorField.getText().trim()),
                        dailyRate);

                // Try to add the car to the database
                boolean success = DatabaseManager.addCar(car);

                if (success) {
                    // Show success message with the car details
                    outputArea.setText("✅ Car added successfully!\n\n" + car);
                    // Clear the form but keep the success message visible
                    clearForm();
                } else {
                    // Show error if something went wrong (like duplicate registration)
                    outputArea.setText("❌ Error: Could not add car to database.\n" +
                            "The registration number might already exist.");
                }

            } catch (NumberFormatException ex) {
                // Show error if year or daily rate is not a valid number
                showError("Please check your input:\n" +
                        "• Year must be a number (e.g., 2023)\n" +
                        "• Daily rate must be a number (e.g., 49.99)",
                        "Invalid Input");
            } catch (Exception ex) {
                // Catch any other unexpected errors
                showError("An unexpected error occurred: " + ex.getMessage(),
                        "Error");
                ex.printStackTrace();
            }
        }

        // Check if any required field is empty
        private boolean isAnyFieldEmpty() {
            return registrationField.getText().trim().isEmpty() ||
                    brandField.getText().trim().isEmpty() ||
                    modelField.getText().trim().isEmpty() ||
                    yearField.getText().trim().isEmpty() ||
                    colorField.getText().trim().isEmpty() ||
                    rateField.getText().trim().isEmpty();
        }

        // Helper method to show error messages in a consistent way
        private void showError(String message, String title) {
            JOptionPane.showMessageDialog(
                    CarForm.this,
                    message,
                    title,
                    JOptionPane.ERROR_MESSAGE);
        }

        // Make sure names start with capital letters
        private String capitalizeFirstLetter(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

    // Handles the View All Cars button click
    private class ViewAllCarsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Show a loading message while we fetch the data
                outputArea.setText("Loading car inventory...");

                // This runs the database query in a background thread to keep the UI responsive
                SwingWorker<List<Car>, Void> worker = new SwingWorker<>() {
                    @Override
                    protected List<Car> doInBackground() throws Exception {
                        return DatabaseManager.getAllCars();
                    }

                    @Override
                    protected void done() {
                        try {
                            List<Car> cars = get();
                            displayCars(cars);
                        } catch (Exception ex) {
                            outputArea.setText("❌ Error retrieving cars: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                };

                worker.execute();

            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // Display the list of cars in a nicely formatted way
        private void displayCars(List<Car> cars) {
            if (cars.isEmpty()) {
                outputArea.setText("No cars found in the database.\n" +
                        "Use the form above to add a new car.");
                return;
            }

            // Create a fancy header
            StringBuilder sb = new StringBuilder();
            sb.append("🚗 ").append("CAR INVENTORY").append(" 🚗\n");
            sb.append("-".repeat(60)).append("\n\n");

            // Add each car's details
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                sb.append(String.format("#%d %s %s (%d)\n",
                        i + 1, car.getBrand(), car.getModel(), car.getYear()));
                sb.append(String.format("   Registration: %s\n", car.getRegistrationNumber()));
                sb.append(String.format("   Color: %s\n", car.getColor()));
                sb.append(String.format("   Daily Rate: $%.2f\n", car.getDailyRate()));
                sb.append(String.format("   Status: %s\n",
                        car.isAvailable() ? "✅ Available" : "❌ Rented"));

                // Add a separator between cars, but not after the last one
                if (i < cars.size() - 1) {
                    sb.append("-".repeat(40)).append("\n\n");
                }
            }

            // Add a nice footer with the total count
            sb.append("\n").append("-".repeat(30)).append("\n");
            sb.append(String.format("Total cars: %d\n", cars.size()));

            outputArea.setText(sb.toString());
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
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 13);
            UIManager.put("Button.font", defaultFont);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 13));

        } catch (Exception e) {
            // If we can't set the look and feel, the default will be used
            System.err.println("Warning: Couldn't set system look and feel");
        }

        // This ensures the UI is created on the Event Dispatch Thread (required for
        // Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and show the main window
                CarForm form = new CarForm();
                form.setVisible(true);

                // Show a welcome message with better formatting
                String welcomeMessage = " 🚗 WELCOME TO SMARTDRIVE RENTALS 🚗\n" +
                        "===================================\n\n" +
                        "To get started:\n" +
                        "1. Fill in the car details below\n" +
                        "2. Click 'Add Car' to save to the database\n" +
                        "3. Click 'View All Cars' to see your inventory\n\n" +
                        "💡 TIPS:\n" +
                        "• Press Enter to submit the form\n" +
                        "• Press Esc to clear the form\n" +
                        "• Press F5 to refresh the car list\n\n" +
                        "🔍 Current Status: Ready";

                form.outputArea.setText(welcomeMessage);
                form.outputArea.setCaretPosition(0); // Scroll to top

            } catch (Exception e) {
                // If something goes wrong, show an error dialog
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to start the application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
