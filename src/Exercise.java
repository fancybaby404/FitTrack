
public class Exercise {
    private String name;
    private double weight;
    private int reps;
    private int sets;
    private int completedSets = 0;

    public Exercise(String name, double weight, int reps, int sets) {
        this.name = name;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.completedSets = 0;
    }

    // getters
    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public int getCompletedSets() {
        return completedSets;
    }

    public void setCompletedSets(int completedSets) {
        this.completedSets = completedSets;
    }

    public void incrementCompletedSets() {
        if (completedSets < sets)
            completedSets++;
    }

    @Override
    public String toString() {
        return String.format("%s||%.2f||%d||%d||%d", name, weight, reps, sets, completedSets);
    }

    // Static method to create Exercise from string
    public static Exercise fromString(String str) {
        String[] parts = str.split("\\|\\|");
        return new Exercise(
                parts[0],
                Double.parseDouble(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]));
    }
}
