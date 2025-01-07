import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.ArrayList;

public class HomeScreen extends BaseScreen {
    private JPanel routinesPanel;
    private JPanel historyPanel;
    private JButton addRoutineButton;
    private ArrayList<Routine> routines;
    private ArrayList<Routine> completedRoutines;
    private JLabel textUserGreeting;

    public HomeScreen() {
        super("Workout Tracker - Home");
        routines = new ArrayList<>();
        completedRoutines = new ArrayList<>();
        initializeComponents();
        setupLayout();
    }

    @Override
    void initializeComponents() {
        // UI components

        // Routines Panel (left side)
        routinesPanel = new JPanel();
        routinesPanel.setLayout(new BoxLayout(routinesPanel, BoxLayout.Y_AXIS));
        JScrollPane routinesScroll = new JScrollPane(routinesPanel);

        // History Panel (right side)
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        JScrollPane historyScroll = new JScrollPane(historyPanel);

        addRoutineButton = new JButton("Add Routine");
        addRoutineButton.setMargin(new Insets(5, 5, 5, 5));
        // addRoutineButton.addActionListener(e -> showAddRoutineDialog());

        textUserGreeting = new JLabel("<html><font size='+2'>Hi, <font color='red'>user</font>!</font></html>");
        textUserGreeting.setBorder(null);
        textUserGreeting.setOpaque(false);
    }

    @Override
    void setupLayout() {
        // Layouts
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(addRoutineButton, BorderLayout.EAST);
        topPanel.add(textUserGreeting, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                routinesPanel,
                historyPanel);
        splitPane.setResizeWeight(0.5);

        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }
}
