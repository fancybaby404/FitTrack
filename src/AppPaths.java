import java.io.File;
import java.io.IOException;

public class AppPaths {
    private static final AppPaths instance = new AppPaths();
    private final String baseDir;
    private final String configDir;
    private final String workoutHistoryFile;
    private final String routinesFile;
    private final String soundsDir;
    private final String pingSound;
    private final String bellSound;
    private final String imagesDir;
    private final String playImage;
    private final String pauseImage;
    private final String trashImage;
    
    private AppPaths() {
        // Initialize base paths
        baseDir = System.getProperty("user.dir");
        // Remove 'src' from baseDir if we're running from src directory
        String actualBaseDir = baseDir.endsWith("src") ? 
            baseDir.substring(0, baseDir.length() - 4) : baseDir;
            
        configDir = actualBaseDir + File.separator + "config";
        workoutHistoryFile = configDir + File.separator + "workout_history.txt";
        routinesFile = configDir + File.separator + "routines.txt";
        
        // Set correct paths for resources
        soundsDir = actualBaseDir + File.separator + "src" + File.separator + "sounds";
        pingSound = soundsDir + File.separator + "ping.wav";
        bellSound = soundsDir + File.separator + "bell.wav";
        
        imagesDir = actualBaseDir + File.separator + "src" + File.separator + "images";
        playImage = imagesDir + File.separator + "play.png";
        pauseImage = imagesDir + File.separator + "pause.png";
        trashImage = imagesDir + File.separator + "trash.png";
        
        // Ensure config directory and files exist
        createRequiredPaths();
    }
    
    public static AppPaths getInstance() {
        return instance;
    }
    
    private void createRequiredPaths() {
        try {
            // Create config directory if it doesn't exist
            File config = new File(configDir);
            if (!config.exists()) {
                config.mkdirs();
            }
            
            // Create workout history file if it doesn't exist
            File history = new File(workoutHistoryFile);
            if (!history.exists()) {
                history.createNewFile();
            }

            // Create routines file if it doesn't exist
            File routine = new File(routinesFile);
            if (!routine.exists()) {
                routine.createNewFile();
            }
            
            // Create sounds directory if it doesn't exist
            File sounds = new File(soundsDir);
            if (!sounds.exists()) {
                sounds.mkdirs();
            }
            
            // Create images directory if it doesn't exist
            File images = new File(imagesDir);
            if (!images.exists()) {
                images.mkdirs();
            }
            
            // Check if sound files exist and print warning if they don't
            checkSoundFile(pingSound, "ping.wav");
            checkSoundFile(bellSound, "bell.wav");
            
            // Check if image files exist and print warning if they don't
            checkImageFile(playImage, "play.png");
            checkImageFile(pauseImage, "pause.png");
            checkImageFile(trashImage, "trash.png");
            
        } catch (IOException e) {
            System.err.println("Error creating required paths: " + e.getMessage());
        }
    }
    
    private void checkSoundFile(String path, String filename) {
        File sound = new File(path);
        if (!sound.exists()) {
            System.err.println("Warning: Sound file " + filename + " not found at: " + path);
        }
    }
    
    private void checkImageFile(String path, String filename) {
        File image = new File(path);
        if (!image.exists()) {
            System.err.println("Warning: Image file " + filename + " not found at: " + path);
        }
    }
    
    // Debug method to print all paths
    public void printAllPaths() {
        System.out.println("Base Directory: " + baseDir);
        System.out.println("Config Directory: " + configDir);
        System.out.println("Workout History File: " + workoutHistoryFile);
        System.out.println("Routines File: " + routinesFile);
        System.out.println("Sounds Directory: " + soundsDir);
        System.out.println("Images Directory: " + imagesDir);
        System.out.println("Play Image: " + playImage);
        System.out.println("Pause Image: " + pauseImage);
        System.out.println("Trash Image: " + trashImage);
    }
    
    // All the getter methods remain the same
    public String getWorkoutHistoryPath() { return workoutHistoryFile; }
    public String getRoutinesPath() { return routinesFile; }
    public String getConfigDir() { return configDir; }
    public String getBaseDir() { return baseDir; }
    public String getPingSound() { return pingSound; }
    public String getBellSound() { return bellSound; }
    public String getPlayImage() { return playImage; }
    public String getPauseImage() { return pauseImage; }
    public String getTrashImage() { return trashImage; }
    
    public boolean soundExists(String soundPath) {
        return new File(soundPath).exists();
    }
    
    public boolean imageExists(String imagePath) {
        return new File(imagePath).exists();
    }
}