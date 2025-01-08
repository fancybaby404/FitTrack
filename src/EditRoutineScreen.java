import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

public class EditRoutineScreen extends JFrame {
    private Routine routine;
    private ArrayList<Routine> allRoutines;
    private JPanel mainPanel;
    private JPanel exerciseListPanel;
    private JTextField routineNameField;

    public EditRoutineScreen(Routine routine, ArrayList<Routine> allRoutines) {
        this.allRoutines = allRoutines;
        this.routine = routine;
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Edit routine: " + routine.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with padding
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Routine name panel
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        namePanel.setBackground(Color.WHITE);
        namePanel.setBorder(new RoundedBorder(10, new Color(200, 200, 200)));

        JLabel nameLabel = new JLabel("Routine Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        routineNameField = new JTextField(routine.getName());
        routineNameField.setFont(new Font("Arial", Font.PLAIN, 14));

        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(routineNameField, BorderLayout.CENTER);

        // Exercise list panel
        exerciseListPanel = new JPanel();
        exerciseListPanel.setLayout(new BoxLayout(exerciseListPanel, BoxLayout.Y_AXIS));
        exerciseListPanel.setBackground(new Color(245, 245, 245));

        // Load existing exercises
        for (Exercise exercise : routine.getExercises()) {
            System.out.println(exercise.getName());
            addExercisePanel(exercise);
        }

        JScrollPane scrollPane = new JScrollPane(exerciseListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));

        // Add exercise button
        JButton addExerciseButton = new JButton("Add Exercise");
        styleButton(addExerciseButton);
        addExerciseButton.addActionListener(e -> addNewExercisePanel());

        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButtonPanel.setBackground(new Color(245, 245, 245));
        addButtonPanel.add(addExerciseButton);

        // Save button
        JButton saveButton = new JButton("Save Routine");
        styleButton(saveButton);
        saveButton.addActionListener(e -> saveRoutine());

        JPanel saveButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButtonPanel.setBackground(new Color(245, 245, 245));
        saveButtonPanel.add(saveButton);

        // Add components to main panel
        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(addButtonPanel, BorderLayout.WEST);
        mainPanel.add(saveButtonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addNewExercisePanel() {
        JPanel exercisePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exercisePanel.setBackground(Color.WHITE);
        exercisePanel.setBorder(new RoundedBorder(10, new Color(200, 200, 200)));

        JTextField nameField = new JTextField(12);
        JTextField weightField = new JTextField(5);
        JTextField repsField = new JTextField(5);
        JTextField setsField = new JTextField(5);

        exercisePanel.add(new JLabel("Name:"));
        exercisePanel.add(nameField);
        exercisePanel.add(new JLabel("Weight (kg):"));
        exercisePanel.add(weightField);
        exercisePanel.add(new JLabel("Reps:"));
        exercisePanel.add(repsField);
        exercisePanel.add(new JLabel("Sets:"));
        exercisePanel.add(setsField);

        JButton removeButton = new JButton("×");
        styleButton(removeButton);
        removeButton.addActionListener(e -> {
            exerciseListPanel.remove(exercisePanel);
            exerciseListPanel.revalidate();
            exerciseListPanel.repaint();
        });
        exercisePanel.add(removeButton);

        exerciseListPanel.add(Box.createVerticalStrut(10));
        exerciseListPanel.add(exercisePanel);
        exerciseListPanel.revalidate();
        exerciseListPanel.repaint();
    }

    private void saveRoutine() {
        if (routineNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a routine name.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        routine.setName(routineNameField.getText().trim());
        routine.getExercises().clear();

        for (Component component : exerciseListPanel.getComponents()) {
            if (component instanceof JPanel exercisePanel) {
                JTextField nameField = (JTextField) exercisePanel.getComponent(1);
                JTextField weightField = (JTextField) exercisePanel.getComponent(3);
                JTextField repsField = (JTextField) exercisePanel.getComponent(5);
                JTextField setsField = (JTextField) exercisePanel.getComponent(7);

                try {
                    Exercise exercise = new Exercise(
                            nameField.getText(),
                            Double.parseDouble(weightField.getText()),
                            Integer.parseInt(repsField.getText()),
                            Integer.parseInt(setsField.getText()));
                    routine.addExercise(exercise);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter valid numbers for weight, reps, and sets.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (!allRoutines.contains(routine)) {
            allRoutines.add(routine);
        }
        Routine.saveRoutines(allRoutines);
        JOptionPane.showMessageDialog(this, "Routine saved successfully!", "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void addExercisePanel(Exercise exercise) {
        JPanel exercisePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exercisePanel.setBackground(Color.WHITE);
        exercisePanel.setBorder(new RoundedBorder(10, new Color(200, 200, 200)));

        JTextField nameField = new JTextField(12);
        JTextField weightField = new JTextField(5);
        JTextField repsField = new JTextField(5);
        JTextField setsField = new JTextField(5);

        if (exercise != null) {
            nameField.setText(exercise.getName());
            weightField.setText(String.valueOf(exercise.getWeight()));
            repsField.setText(String.valueOf(exercise.getReps()));
            setsField.setText(String.valueOf(exercise.getSets()));
        }

        exercisePanel.add(new JLabel("Name:"));
        exercisePanel.add(nameField);
        exercisePanel.add(new JLabel("Weight (kg):"));
        exercisePanel.add(weightField);
        exercisePanel.add(new JLabel("Reps:"));
        exercisePanel.add(repsField);
        exercisePanel.add(new JLabel("Sets:"));
        exercisePanel.add(setsField);

        JButton removeButton = new JButton("×");
        styleButton(removeButton);
        removeButton.addActionListener(e -> {
            exerciseListPanel.remove(exercisePanel);
            exerciseListPanel.revalidate();
            exerciseListPanel.repaint();
        });
        exercisePanel.add(removeButton);

        exerciseListPanel.add(Box.createVerticalStrut(10));
        exerciseListPanel.add(exercisePanel);
        exerciseListPanel.revalidate();
        exerciseListPanel.repaint();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    }

    // Custom RoundedBorder class for reusable borders
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }
    }
}
