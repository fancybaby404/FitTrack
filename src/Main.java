import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // Create and display the home screen on the event dispatch thread.
        SwingUtilities.invokeLater(() -> {
            new HomeScreen().setVisible(true);
        });
    }

}
