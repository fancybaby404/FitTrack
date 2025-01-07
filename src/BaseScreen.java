import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

abstract class BaseScreen extends JFrame {
    protected JPanel mainPanel;
    private Border padding;

    public BaseScreen(String title) {
        super(title);

        // border
        padding = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        // window properties
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600, 600);

        // main panel
        this.mainPanel = new JPanel();
        this.mainPanel.setBorder(padding);
        this.add(mainPanel);
    }

    // (MUST BE IMPLEMENTED) abstract methods
    abstract void initializeComponents();

    abstract void setupLayout();
}
