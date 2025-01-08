import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Routine {
    private String name;
    private ArrayList<Exercise> exercises;
    private LocalDateTime date;
    private boolean isCompleted;
    private static final String ROUTINES_FILE = "C:\\Users\\Aaron\\Documents\\GitHub\\FitTrack\\src\\config\\routines.txt";

    public Routine(String name) {
        this.name = name;
        this.exercises = new ArrayList();
        this.isCompleted = false;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    // getters
    public String getName() {
        return name;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // exercise management
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(int index) {
        if (index > 0 && index < exercises.size()) {
            exercises.remove(index);
        }
    }

    // Convert routine to string for saving
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROUTINE_START||").append(name).append("\n");

        for (Exercise exercise : exercises) {
            sb.append("EXERCISE||").append(exercise.toString()).append("\n");
        }

        sb.append("ROUTINE_END\n");
        return sb.toString();
    }

    // Static methods for saving and loading routines
    public static void saveRoutines(ArrayList<Routine> routines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ROUTINES_FILE))) {
            for (Routine routine : routines) {
                writer.print(routine.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error saving routines: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ArrayList<Routine> loadRoutines() {
        ArrayList<Routine> routines = new ArrayList<>();

        // Create file if it doesn't exist
        try {
            Files.createFile(Paths.get(ROUTINES_FILE));
        } catch (FileAlreadyExistsException e) {
            // File exists, continue
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ROUTINES_FILE))) {
            String line;
            Routine currentRoutine = null;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|\\|");

                switch (parts[0]) {
                    case "ROUTINE_START":
                        currentRoutine = new Routine(parts[1]);
                        break;

                    case "EXERCISE":
                        if (currentRoutine != null) {
                            // Remove "EXERCISE||" from the string
                            String exerciseData = line.substring("EXERCISE||".length());
                            Exercise exercise = Exercise.fromString(exerciseData);
                            currentRoutine.addExercise(exercise);
                        }
                        break;

                    case "ROUTINE_END":
                        if (currentRoutine != null) {
                            routines.add(currentRoutine);
                            currentRoutine = null;
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error loading routines: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return routines;
    }

    public static void deleteRoutine(String routineName, ArrayList<Routine> routines) {
        // Remove the routine from the ArrayList
        routines.removeIf(routine -> routine.getName().equals(routineName));
        
        // Save the updated list to file
        saveRoutines(routines);
        
    }

    
}
