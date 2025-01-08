import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.util.ArrayList;

public class RoutineScreen extends JFrame {
    private Routine routine;
    private ArrayList<Routine> allRoutines;
    private JPanel mainPanel;
    private JPanel exercisesPanel;
    private JTextField routineNameField;
    private JPanel exerciseListPanel;

    public RoutineScreen(ArrayList<Routine> allRoutines) {
        this.allRoutines = allRoutines;
        this.routine = new Routine("New Routine");
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Create New Routine");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Routine name panel
        JPanel namePanel = new JPanel(new BorderLayout());
        routineNameField = new JTextField(routine.getName());
        routineNameField.setFont(new Font("Arial", Font.BOLD, 16));
        routineNameField.addActionListener(e -> {
            routine.setName(routineNameField.getText());
            setTitle("Routine: " + routine.getName());
        });
        namePanel.add(new JLabel("Routine Name: "), BorderLayout.WEST);
        namePanel.add(routineNameField, BorderLayout.CENTER);

        // Exercises panel
        exercisesPanel = new JPanel(new BorderLayout());
        exerciseListPanel = new JPanel();
        exerciseListPanel.setLayout(new BoxLayout(exerciseListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(exerciseListPanel);

        // Add Exercise button
        JButton addExerciseButton = new JButton("Add Exercise");
        addExerciseButton.addActionListener(e -> addNewExercisePanel());

        // Save button panel
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save Routine");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> saveRoutine());
        savePanel.add(saveButton);

        // Arrange panels
        exercisesPanel.add(scrollPane, BorderLayout.CENTER);
        exercisesPanel.add(addExerciseButton, BorderLayout.NORTH);

        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(exercisesPanel, BorderLayout.CENTER);
        mainPanel.add(savePanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addNewExercisePanel() {
        JPanel exercisePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exercisePanel.setBorder(BorderFactory.createEtchedBorder());

        JTextField nameField = new JTextField(15);
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
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.addActionListener(e -> {
            exerciseListPanel.remove(exercisePanel);
            exerciseListPanel.revalidate();
            exerciseListPanel.repaint();
        });
        exercisePanel.add(removeButton);

        ActionListener addExerciseAction = e -> {
            try {
                Exercise exercise = new Exercise(
                    nameField.getText(),
                    Double.parseDouble(weightField.getText()),
                    Integer.parseInt(repsField.getText()),
                    Integer.parseInt(setsField.getText())
                );
                routine.addExercise(exercise);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for weight, reps, and sets.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        };

        nameField.addActionListener(addExerciseAction);
        weightField.addActionListener(addExerciseAction);
        repsField.addActionListener(addExerciseAction);
        setsField.addActionListener(addExerciseAction);

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
        allRoutines.add(routine);
        Routine.saveRoutines(allRoutines);
        dispose();
    }
}
