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
        configDir = baseDir + "/config";
        soundsDir = baseDir + "/sounds";
        imagesDir = baseDir + "/images";

        workoutHistoryFile = configDir + "/workout_history.txt";
        routinesFile = configDir + "/routines.txt";
        pingSound = soundsDir + "/ping.wav";
        bellSound = soundsDir + "/bell.wav";
        playImage = imagesDir + "/play.png";
        pauseImage = imagesDir + "/pause.png";
        trashImage = imagesDir + "/trash.png";
        
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
    
    public String getWorkoutHistoryPath() {
        return workoutHistoryFile;
    }

    public String getRoutinesPath() {
        return routinesFile;
    }
    
    public String getConfigDir() {
        return configDir;
    }
    
    public String getBaseDir() {
        return baseDir;
    }
    
    public String getPingSound() {
        return pingSound;
    }
    
    public String getBellSound() {
        return bellSound;
    }
    
    public String getPlayImage() {
        return playImage;
    }
    
    public String getPauseImage() {
        return pauseImage;
    }
    
    public String getTrashImage() {
        return trashImage;
    }
    
    // Helper method to check if a sound file exists
    public boolean soundExists(String soundPath) {
        return new File(soundPath).exists();
    }
    
    // Helper method to check if an image file exists
    public boolean imageExists(String imagePath) {
        return new File(imagePath).exists();
    }
}