import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class HomeScreen extends BaseScreen {
    private JPanel routinesPanel;
    private JPanel historyPanel;
    private JButton addRoutineButton;
    private ArrayList<Routine> routines;
    private JLabel textUserGreeting;

    public HomeScreen() {
        super("Workout Tracker - Home");
        routines = Routine.loadRoutines();
        initializeComponents();
        setupLayout();
        updateRoutinesPanel();
        updateHistoryPanel();
    }

    @Override
    void initializeComponents() {
        // Routines Panel
        routinesPanel = new JPanel();
        routinesPanel.setLayout(new BoxLayout(routinesPanel, BoxLayout.Y_AXIS));
        JScrollPane routinesScroll = new JScrollPane(routinesPanel);

        // History Panel
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        JScrollPane historyScroll = new JScrollPane(historyPanel);

        // Add Routine Button
        addRoutineButton = new JButton("Add Routine");
        addRoutineButton.setMargin(new Insets(10, 20, 10, 20));
        addRoutineButton.setFont(new Font("Arial", Font.BOLD, 14));
        addRoutineButton.addActionListener(e -> {
            RoutineScreen routineScreen = new RoutineScreen(routines);
            routineScreen.setVisible(true);
            routineScreen.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    updateRoutinesPanel();
                }
            });
        });

        // User Greeting
        textUserGreeting = new JLabel("<html><font size='+2'>Hi, <font color='red'>user</font>!</font></html>");
        textUserGreeting.setBorder(null);
        textUserGreeting.setOpaque(false);
    }

    @Override
    void setupLayout() {
        mainPanel.setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(addRoutineButton, BorderLayout.EAST);
        topPanel.add(textUserGreeting, BorderLayout.CENTER);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Split Pane
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(routinesPanel),
                new JScrollPane(historyPanel));
        splitPane.setResizeWeight(0.5);
        splitPane.setEnabled(false); // non resizable

        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void updateRoutinesPanel() {
        routinesPanel.removeAll();
        routinesPanel.add(Box.createVerticalStrut(10));

        for (Routine routine : routines) {
            JPanel routineCard = createRoutineCard(routine);
            routinesPanel.add(routineCard);
            routinesPanel.add(Box.createVerticalStrut(10));
        }

        routinesPanel.revalidate();
        routinesPanel.repaint();
    }

    private JPanel createRoutineCard(Routine routine) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(250, 250, 250));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Routine name with ellipsis if too long
        String name = routine.getName();
        if (name.length() > 20) {
            name = name.substring(0, 17) + "...";
        }
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Exercise count
        JLabel countLabel = new JLabel(routine.getExercises().size() + " exercises");
        countLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        countLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(countLabel, BorderLayout.SOUTH);

        card.add(textPanel, BorderLayout.CENTER);

        // Make the entire card clickable
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    WorkoutScreen workoutScreen = new WorkoutScreen(routine);
                    workoutScreen.setVisible(true);
                }
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 240, 240));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(250, 250, 250));
            }
        });

        return card;
    }

    private void updateHistoryPanel() {
        historyPanel.removeAll();
        historyPanel.add(Box.createVerticalStrut(10));

        try (BufferedReader reader = new BufferedReader(new FileReader("workout_history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JPanel historyEntry = createHistoryEntry(line);
                historyPanel.add(historyEntry);
                historyPanel.add(Box.createVerticalStrut(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        historyPanel.revalidate();
        historyPanel.repaint();
    }

    private JPanel createHistoryEntry(String line) {
        JPanel entry = new JPanel();
        entry.setLayout(new BoxLayout(entry, BoxLayout.Y_AXIS));
        entry.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        entry.setBackground(new Color(252, 252, 252));
        entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Parse and format the history line
        String[] parts = line.split("\\|");
        if (parts.length >= 4) {
            // Date with special styling
            JPanel datePanel = new JPanel();
            datePanel.setBackground(new Color(70, 130, 180));
            datePanel.setBorder(new RoundedBorder(4, new Color(70, 130, 180)));
            JLabel dateLabel = new JLabel(parts[0].trim());
            dateLabel.setForeground(Color.WHITE);
            dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
            datePanel.add(dateLabel);

            // Workout name
            JLabel workoutLabel = new JLabel(parts[1].trim());
            workoutLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Duration
            JLabel durationLabel = new JLabel(parts[2].trim());
            durationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            durationLabel.setForeground(new Color(100, 100, 100));

            // Exercise count
            JLabel exerciseLabel = new JLabel(parts[3].trim());
            exerciseLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            exerciseLabel.setForeground(new Color(100, 100, 100));

            // Add components
            entry.add(datePanel);
            entry.add(Box.createVerticalStrut(5));
            entry.add(workoutLabel);
            entry.add(Box.createVerticalStrut(3));
            entry.add(durationLabel);
            entry.add(exerciseLabel);
        }

        return entry;
    }

    // Custom rounded border class
    public static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }
    }
}
