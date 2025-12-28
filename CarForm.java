import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarForm extends JFrame {
    private JTextField regField, brandField, modelField, yearField, priceField, searchField, colorField;
    private JTextArea outputArea;
    private JButton addButton, searchButton, clearButton, viewAllButton;
    private JTable carTable;
    private DefaultTableModel tableModel;

    public CarForm() {
        setTitle("Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Car"));

        // Add input fields
        addFormField(inputPanel, "Registration*:", regField = new JTextField());
        addFormField(inputPanel, "Brand*:", brandField = new JTextField());
        addFormField(inputPanel, "Model*:", modelField = new JTextField());
        addFormField(inputPanel, "Year*:", yearField = new JTextField());
        addFormField(inputPanel, "Price*:", priceField = new JTextField());
        addFormField(inputPanel, "Color:", colorField = new JTextField());

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Cars"));

        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchInputPanel.add(new JLabel("Search by registration, brand, or model:"));
        searchField = new JTextField(20);
        searchInputPanel.add(searchField);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = createButton("Add Car", e -> addCar());
        viewAllButton = createButton("View All Cars", e -> viewAllCars());
        searchButton = createButton("Search", e -> searchCars());
        clearButton = createButton("Clear", e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        searchPanel.add(searchInputPanel, BorderLayout.CENTER);

        // Create table for displaying cars
        String[] columns = { "Registration", "Brand", "Model", "Year", "Price", "Color", "Available" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.getTableHeader().setReorderingAllowed(false);

        // Add double-click listener to view car details
        carTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewCarDetails();
                }
            }
        });

        // Set up the main layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(carTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load all cars initially
        viewAllCars();

        add(mainPanel);
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));
        mainPanel.add(scrollPane, BorderLayout.EAST);
        outputArea.setText("Welcome to Car Rental System!\nAdd cars or search for existing ones.");
    }

    // Helper method to create consistent buttons
    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    // Helper method to add form fields with labels
    private void addFormField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        field.setPreferredSize(new Dimension(150, 25));
        panel.add(field);
    }

    private void addCar() {
        try {
            String registration = regField.getText().trim();
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String color = colorField.getText().trim();

            if (registration.isEmpty() || brand.isEmpty() || model.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields (marked with *).");
                return;
            }

            Car car = new Car(registration, brand, model, year, price);
            car.setColor(color);

            if (DatabaseManager.addCar(car)) {
                JOptionPane.showMessageDialog(this, "Car added successfully!");
                clearForm();
                viewAllCars();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add car. The registration number might already exist.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for year and price.");
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

    private void clearForm() {
        regField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        colorField.setText("");
        searchField.setText("");
        regField.requestFocus();
    }

    private void viewAllCars() {
        tableModel.setRowCount(0);
        List<Car> cars = DatabaseManager.getAllCars();
        for (Car car : cars) {
            tableModel.addRow(new Object[] {
                    car.getRegistration(),
                    car.getBrand(),
                    car.getModel(),
                    car.getYear(),
                    String.format("$%.2f", car.getPrice()),
                    car.getColor().isEmpty() ? "-" : car.getColor(),
                    car.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void viewCarDetails() {
        int row = carTable.getSelectedRow();
        if (row != -1) {
            String registration = (String) carTable.getValueAt(row, 0);
            Car car = DatabaseManager.getCar(registration);
            if (car != null) {
                String details = String.format("""
                        <html>
                        <h2>Car Details</h2>
                        <table border='0' cellpadding='5'>
                            <tr><td><b>Registration:</b></td><td>%s</td></tr>
                            <tr><td><b>Brand:</b></td><td>%s</td></tr>
                            <tr><td><b>Model:</b></td><td>%s</td></tr>
                            <tr><td><b>Year:</b></td><td>%d</td></tr>
                            <tr><td><b>Price:</b></td><td>$%.2f</td></tr>
                            <tr><td><b>Color:</b></td><td>%s</td></tr>
                            <tr><td><b>Available:</b></td><td>%s</td></tr>
                        </table>
                        </html>
                        """,
                        car.getRegistration(),
                        car.getBrand(),
                        car.getModel(),
                        car.getYear(),
                        car.getPrice(),
                        car.getColor().isEmpty() ? "Not specified" : car.getColor(),
                        car.isAvailable() ? "Yes" : "No");

                JOptionPane.showMessageDialog(
                        this,
                        details,
                        "Car Details - " + car.getRegistration(),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarForm().setVisible(true));
    }
}
