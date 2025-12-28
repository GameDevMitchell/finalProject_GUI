import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Start the application
            CarForm.main(args);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver. Make sure the JAR is in the classpath.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to load database driver. Please make sure the SQLite JDBC driver is in the lib folder.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
