import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public class HomeScreen extends BaseScreen {
    private static final Path WORKOUT_FILE_PATH = Paths.get(System.getProperty("user.dir"), "config",
            "workout_history.txt");
    private static final String WORKOUT_FILE = WORKOUT_FILE_PATH.toString();

    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color PRIMARY_DARK = new Color(60, 120, 170);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color DANGER_DARK = new Color(200, 35, 51);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(100, 100, 100);

    private JPanel routinesPanel;
    private JPanel historyPanel;
    private JButton addRoutineButton;
    private ArrayList<Routine> allRoutines;
    private JLabel textUserGreeting;
    private JButton clearHistoryButton;

    public HomeScreen() {
        super("Workout Tracker - Home");
        allRoutines = Routine.loadRoutines();
        initializeComponents();
        setupLayout();
        updateRoutinesPanel();
        updateHistoryPanel();
    }

    @Override
    void initializeComponents() {
        // Set main panel background
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Initialize panels with custom backgrounds
        routinesPanel = createScrollablePanel();
        historyPanel = createScrollablePanel();

        clearHistoryButton = createStyledButton("Clear History", DANGER_COLOR); // Use a danger color to indicate
                                                                                // caution
        clearHistoryButton.addActionListener(e -> clearHistory());

        // Create stylish Add Routine button
        addRoutineButton = createStyledButton("Add Routine", PRIMARY_COLOR);

        // Create modern greeting label
        textUserGreeting = new JLabel("<html><div style='font-family: Arial; font-size: 24px; margin: 10px;'>" +
                "Welcome back, <span style='color: " + String.format("#%02x%02x%02x",
                        PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue())
                +
                "'>User</span>!</div></html>");
    }

    private JPanel createScrollablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(10, baseColor));
        button.setPreferredSize(new Dimension(150, 40));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_DARK);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        button.addActionListener(e -> {
            RoutineScreen routineScreen = new RoutineScreen(allRoutines);
            routineScreen.setVisible(true);
            routineScreen.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateRoutinesPanel();
                }
            });
        });

        return button;
    }

    @Override
    void setupLayout() {
        mainPanel.setLayout(new BorderLayout(15, 15));

        // Top panel with gradient background
        JPanel topPanel = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, CARD_COLOR, getWidth(), 0, new Color(240, 240, 245));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.add(textUserGreeting, BorderLayout.WEST);
        topPanel.add(addRoutineButton, BorderLayout.EAST);

        // Create split pane with custom divider
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createScrollPane(routinesPanel, "My Routines"),
                createScrollPane(historyPanel, "Workout History"));
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void clearHistory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(WORKOUT_FILE))) {
            // This will effectively overwrite the file with nothing, clearing the contents
            writer.print(""); // Writing an empty string to clear the content
            JOptionPane.showMessageDialog(this, "Workout history has been cleared.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            updateHistoryPanel(); // Refresh the history panel after clearing the data
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error clearing history: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JScrollPane createScrollPane(JPanel panel, String title) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(BACKGROUND_COLOR);

        // Add title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 0));
        containerPanel.add(titleLabel, BorderLayout.NORTH);

        // Add clearHistoryButton
        if (title.equals("Workout History")) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);
            buttonPanel.add(clearHistoryButton);
            containerPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        containerPanel.add(panel, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        return scrollPane;
    }

    private JPanel createRoutineCard(Routine routine) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, new Color(230, 230, 235)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Create name label with ellipsis
        String name = routine.getName();
        if (name.length() > 20) {
            name = name.substring(0, 17) + "...";
        }
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Load and scale trash icon
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/trash.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Create delete button
        JButton deleteButton = new RoundedIconButton(scaledIcon);
        deleteButton.addActionListener(e -> handleDeleteRoutine(routine));

        // Create exercise count with icon
        JLabel countLabel = new JLabel(routine.getExercises().size() + " exercises");
        countLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        countLabel.setForeground(TEXT_SECONDARY);

        // Layout components
        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.setBackground(CARD_COLOR);
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(countLabel, BorderLayout.SOUTH);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(deleteButton, BorderLayout.EAST);

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    WorkoutScreen workoutScreen = new WorkoutScreen(routine, allRoutines);
                    workoutScreen.setHomeScreen(HomeScreen.this);
                    workoutScreen.setVisible(true);
                }
            }

            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 255));
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
            }
        });

        return card;
    }

    private void handleDeleteRoutine(Routine routine) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + routine.getName() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            Routine.deleteRoutine(routine.getName(), allRoutines);
            updateRoutinesPanel();
            updateHistoryPanel();
        }
    }

    // Custom components
    private static class RoundedIconButton extends JButton {
        public RoundedIconButton(Icon icon) {
            super(icon);
            setContentAreaFilled(false);
            setBackground(DANGER_COLOR);
            setText(null);
            setPreferredSize(new Dimension(50, 100));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(DANGER_DARK);
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(DANGER_COLOR);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2d.setColor(getBackground().darker());
            } else {
                g2d.setColor(getBackground());
            }

            g2d.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            super.paintComponent(g2d);
            g2d.dispose();
        }
    }

    public void updateRoutinesPanel() {
        routinesPanel.removeAll();
        routinesPanel.add(Box.createVerticalStrut(10));

        for (Routine routine : allRoutines) {
            JPanel routineCard = createRoutineCard(routine);
            routinesPanel.add(routineCard);
            routinesPanel.add(Box.createVerticalStrut(10));
        }

        routinesPanel.revalidate();
        routinesPanel.repaint();
    }

    public void updateHistoryPanel() {
        historyPanel.removeAll();
        historyPanel.add(Box.createVerticalStrut(10));

        try (BufferedReader reader = new BufferedReader(new FileReader(WORKOUT_FILE))) {
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
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
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