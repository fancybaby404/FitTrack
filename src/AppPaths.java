import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
         // Find the src directory regardless of working directory
         baseDir = findSrcDirectory();
        
         // Set up paths relative to the src directory
         configDir = Paths.get(baseDir, "config").toString();
         soundsDir = Paths.get(baseDir, "sounds").toString();
         imagesDir = Paths.get(baseDir, "images").toString();
 
         workoutHistoryFile = Paths.get(configDir, "workout_history.txt").toString();
         routinesFile = Paths.get(configDir, "routines.txt").toString();
         pingSound = Paths.get(soundsDir, "ping.wav").toString();
         bellSound = Paths.get(soundsDir, "bell.wav").toString();
         playImage = Paths.get(imagesDir, "play.png").toString();
         pauseImage = Paths.get(imagesDir, "pause.png").toString();
         trashImage = Paths.get(imagesDir, "trash.png").toString();
         
         // Ensure directories exist
         createRequiredPaths();
    }
    
    public static AppPaths getInstance() {
        return instance;
    }
    
    private void createRequiredPaths() {
        try {
            // Create directories if they don't exist
            createDirectoryIfNotExists(configDir);
            createDirectoryIfNotExists(soundsDir);
            createDirectoryIfNotExists(imagesDir);
            
            // Create config files if they don't exist
            createFileIfNotExists(workoutHistoryFile);
            createFileIfNotExists(routinesFile);
            
            // Check if resource files exist
            checkFile(pingSound, "ping.wav");
            checkFile(bellSound, "bell.wav");
            checkFile(playImage, "play.png");
            checkFile(pauseImage, "pause.png");
            checkFile(trashImage, "trash.png");
            
        } catch (IOException e) {
            System.err.println("Error creating required paths: " + e.getMessage());
        }
    }
    
    private void createDirectoryIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private void createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    private String findSrcDirectory() {
        // Start with the current working directory
        Path currentPath = Paths.get("").toAbsolutePath();
        
        // First check if we're already in the src directory
        if (isSrcDirectory(currentPath)) {
            return currentPath.toString();
        }
        
        // Check if src is a direct child of current directory
        Path srcPath = currentPath.resolve("src");
        if (isSrcDirectory(srcPath)) {
            return srcPath.toString();
        }
        
        // If we're in a subdirectory of src, traverse up until we find it
        Path parent = currentPath;
        while (parent != null) {
            if (isSrcDirectory(parent)) {
                return parent.toString();
            }
            parent = parent.getParent();
            if (parent != null) {
                srcPath = parent.resolve("src");
                if (isSrcDirectory(srcPath)) {
                    return srcPath.toString();
                }
            }
        }
        
        // If we couldn't find the src directory, throw an exception
        throw new RuntimeException("Could not locate src directory. Current working directory: " + currentPath);
    }
    private void checkFile(String path, String filename) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Warning: File " + filename + " not found at: " + path);
        }
    }
     private boolean isSrcDirectory(Path path) {
        if (!path.toFile().exists()) {
            return false;
        }
        
        // Verify this is the correct src directory by checking for some known files/directories
        return Files.exists(path.resolve("config")) &&
               Files.exists(path.resolve("config/routines.txt")) &&
               Files.exists(path.resolve("Main.java")) &&
               Files.exists(path.resolve("AppPaths.java"));
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
    
    public boolean soundExists(String soundPath) {
        return new File(soundPath).exists();
    }
    
    public boolean imageExists(String imagePath) {
        return new File(imagePath).exists();
    }
}
// private AppPaths() {
// // Initialize base paths
// baseDir = System.getProperty("user.home" + File.separator + ".yourappname");
// configDir = baseDir + "/config";
// soundsDir = baseDir + "/sounds";
// imagesDir = baseDir + "/images";

// workoutHistoryFile = configDir + "/workout_history.txt";
// routinesFile = configDir + "/routines.txt";
// pingSound = soundsDir + "/ping.wav";
// bellSound = soundsDir + "/bell.wav";
// playImage = imagesDir + "/play.png";
// pauseImage = imagesDir + "/pause.png";
// trashImage = imagesDir + "/trash.png";

// // Ensure config directory and files exist
// createRequiredPaths();
// }

// public static AppPaths getInstance() {
// return instance;
// }

// private void createRequiredPaths() {
// try {
// // Create base directory if it doesn't exist
// File base = new File(baseDir);
// if (!base.exists()) {
// base.mkdirs();
// }

// // Create config directory if it doesn't exist
// File config = new File(configDir);
// if (!config.exists()) {
// config.mkdirs();
// }

// // Create workout history file if it doesn't exist
// File history = new File(workoutHistoryFile);
// if (!history.exists()) {
// history.createNewFile();
// }

// // Create routines file if it doesn't exist
// File routine = new File(routinesFile);
// if (!routine.exists()) {
// routine.createNewFile();
// }

// // Create sounds directory if it doesn't exist
// File sounds = new File(soundsDir);
// if (!sounds.exists()) {
// sounds.mkdirs();
// // Copy default sound files from resources
// copyResourceToFile("/sounds/ping.wav", pingSound);
// copyResourceToFile("/sounds/bell.wav", bellSound);
// }

// // Create images directory if it doesn't exist
// File images = new File(imagesDir);
// if (!images.exists()) {
// images.mkdirs();
// // Copy default image files from resources
// copyResourceToFile("/images/play.png", playImage);
// copyResourceToFile("/images/pause.png", pauseImage);
// copyResourceToFile("/images/trash.png", trashImage);
// }

// } catch (IOException e) {
// System.err.println("Error creating required paths: " + e.getMessage());
// }
// }

// private void copyResourceToFile(String resourcePath, String destPath) {
// try {
// URL resource = getClass().getResource(resourcePath);
// if (resource != null) {
// java.nio.file.Files.copy(
// resource.openStream(),
// new File(destPath).toPath(),
// java.nio.file.StandardCopyOption.REPLACE_EXISTING);
// } else {
// System.err.println("Warning: Resource not found: " + resourcePath);
// }
// } catch (IOException e) {
// System.err.println("Error copying resource " + resourcePath + ": " +
// e.getMessage());
// }
// }

// private void checkSoundFile(String path, String filename) {
// File sound = new File(path);
// if (!sound.exists()) {
// System.err.println("Warning: Sound file " + filename + " not found at: " +
// path);
// }
// }

// private void checkImageFile(String path, String filename) {
// File image = new File(path);
// if (!image.exists()) {
// System.err.println("Warning: Image file " + filename + " not found at: " +
// path);
// }
// }

// public String getWorkoutHistoryPath() {
// return workoutHistoryFile;
// }

// public String getRoutinesPath() {
// return routinesFile;
// }

// public String getConfigDir() {
// return configDir;
// }

// public String getBaseDir() {
// return baseDir;
// }

// public String getPingSound() {
// return pingSound;
// }

// public String getBellSound() {
// return bellSound;
// }

// public String getPlayImage() {
// return playImage;
// }

// public String getPauseImage() {
// return pauseImage;
// }

// public String getTrashImage() {
// return trashImage;
// }

// // Helper method to check if a sound file exists
// public boolean soundExists(String soundPath) {
// return new File(soundPath).exists();
// }

// // Helper method to check if an image file exists
// public boolean imageExists(String imagePath) {
// return new File(imagePath).exists();
// }
// }