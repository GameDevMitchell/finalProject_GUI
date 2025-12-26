public class Main {
    public static void main(String[] args) {
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.closeConnection();
        }));
        
        // Start the GUI application
        CarForm.main(args);
    }
}
