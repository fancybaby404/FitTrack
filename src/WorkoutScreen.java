import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;

public class WorkoutScreen extends JFrame {
    private Routine routine;
    private JPanel mainPanel;
    private JPanel exercisesPanel;
    private Timer stopwatch;
    private int seconds = 0;
    private JButton startFinishButton;
    private JLabel timerLabel;
    private static final String HISTORY_FILE = "workout_history.txt";
    private Color primaryColor = new Color(70, 130, 180);
    private Color accentColor = new Color(240, 240, 240);

    public WorkoutScreen(Routine routine) {
        this.routine = routine;
        initializeComponents();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        setTitle("Workout: " + routine.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with routine name
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel routineNameLabel = new JLabel(routine.getName());
        routineNameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        routineNameLabel.setForeground(primaryColor);
        headerPanel.add(routineNameLabel, BorderLayout.WEST);

        // Timer display
        timerLabel = new JLabel("00:00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(timerLabel, BorderLayout.EAST);

        // Exercises panel
        exercisesPanel = new JPanel();
        exercisesPanel.setBackground(Color.WHITE);
        exercisesPanel.setLayout(new BoxLayout(exercisesPanel, BoxLayout.Y_AXIS));
        exercisesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Display exercises
        for (Exercise exercise : routine.getExercises()) {
            JPanel exercisePanel = createExercisePanel(exercise);
            exercisesPanel.add(exercisePanel);
            exercisesPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(exercisesPanel);
        scrollPane.setBorder(new RoundedBorder(10, new Color(220, 220, 220)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        startFinishButton = new JButton("Start Workout");
        startFinishButton.setFont(new Font("Arial", Font.BOLD, 16));
        startFinishButton.setForeground(Color.WHITE);
        startFinishButton.setBackground(primaryColor);
        startFinishButton.setPreferredSize(new Dimension(150, 40));
        startFinishButton.setBorder(new RoundedBorder(20, primaryColor));
        startFinishButton.setFocusPainted(false);
        startFinishButton.addActionListener(e -> handleStartFinish());
        startFinishButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startFinishButton.setBackground(primaryColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startFinishButton.setBackground(primaryColor);
            }
        });

        controlPanel.add(startFinishButton);

        // Arrange panels
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Initialize stopwatch
        stopwatch = new Timer(1000, e -> {
            seconds++;
            updateTimerLabel();
        });
    }

    private JPanel createExercisePanel(Exercise exercise) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(accentColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Left side - Exercise name and details
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(exercise.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel detailsLabel = new JLabel(String.format(
                "%.1f kg × %d reps × %d sets",
                exercise.getWeight(),
                exercise.getReps(),
                exercise.getSets()));
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);

        // Right side - Sets counter and + button
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setOpaque(false);

        // Counter variable to track completed sets
        final int[] completedSets = { 0 };

        // Sets counter
        JLabel setsLabel = new JLabel("0 / " + exercise.getSets());
        setsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // + button for set completion
        JButton addSetButton = new JButton("+");
        addSetButton.setFont(new Font("Arial", Font.BOLD, 20));
        addSetButton.setForeground(Color.WHITE);
        addSetButton.setBackground(primaryColor);
        addSetButton.setPreferredSize(new Dimension(40, 40));
        addSetButton.setFocusPainted(false);
        addSetButton.setBorder(new RoundedBorder(20, primaryColor));

        addSetButton.addActionListener(e -> {
            if (completedSets[0] < exercise.getSets()) {
                completedSets[0]++;
                setsLabel.setText(completedSets[0] + " / " + exercise.getSets());

                if (completedSets[0] == exercise.getSets()) {
                    addSetButton.setEnabled(false);
                    addSetButton.setBackground(Color.GRAY);
                }

            }
        });

        // Add hover effect
        addSetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (addSetButton.isEnabled()) {
                    addSetButton.setBackground(primaryColor.darker());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (addSetButton.isEnabled()) {
                    addSetButton.setBackground(primaryColor);
                }
            }
        });

        // - button
        JButton minusSetButton = new JButton("-");
        minusSetButton.setFont(new Font("Arial", Font.BOLD, 20));
        minusSetButton.setForeground(Color.WHITE);
        minusSetButton.setBackground(primaryColor);
        minusSetButton.setPreferredSize(new Dimension(40, 40));
        minusSetButton.setFocusPainted(false);
        minusSetButton.setBorder(new RoundedBorder(20, primaryColor));

        minusSetButton.addActionListener(e -> {
            if (completedSets[0] > 0) {
                completedSets[0]--;
                setsLabel.setText(completedSets[0] + " / " + exercise.getSets());

                addSetButton.setEnabled(true);
                addSetButton.setBackground(primaryColor);

            }
        });

        // Add hover effect
        minusSetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (minusSetButton.isEnabled()) {
                    minusSetButton.setBackground(primaryColor.darker());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (minusSetButton.isEnabled()) {
                    minusSetButton.setBackground(primaryColor);
                }
            }
        });

        // Add components to controls panel
        controlsPanel.add(setsLabel);
        controlsPanel.add(addSetButton);
        controlsPanel.add(minusSetButton);

        // Add panels to main panel
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(controlsPanel, BorderLayout.EAST);

        return panel;
    }

    private void handleStartFinish() {
        if (startFinishButton.getText().equals("Start Workout")) {
            stopwatch.start();
            startFinishButton.setText("Finish Workout");
        } else {
            stopwatch.stop();
            logWorkout();
            dispose();
        }
    }

    private void updateTimerLabel() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
    }

    private void logWorkout() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            writer.println(String.format(
                    "Date: %s | Workout: %s | Duration: %s | Exercises: %d",
                    now.format(formatter),
                    routine.getName(),
                    timerLabel.getText(),
                    routine.getExercises().size()));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error logging workout: " + e.getMessage(),
                    "Log Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

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
