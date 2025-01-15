import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // Create and display the home screen.
        SwingUtilities.invokeLater(() -> {
            new HomeScreen().setVisible(true);
        });
    }

}
