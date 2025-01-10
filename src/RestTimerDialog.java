import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.concurrent.*;

class RestTimerDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240);
    private int remainingSeconds;
    private Timer timer;
    private JPanel circularProgress;
    private JLabel timeLabel;
    private JButton playPauseButton;
    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    private boolean isPaused = false;
    private boolean isTimerRunning = false;
    private final int DEFAULT_REST_TIME = 60; // 1 minute default

    public RestTimerDialog(JFrame parent) {
        super(parent, "Rest Timer", true);
        loadIcons();
        this.remainingSeconds = DEFAULT_REST_TIME;
        initializeComponents();
        startTimer();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        setSize(300, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Main panel with white background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create circular progress panel
        circularProgress = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(getWidth(), getHeight()) - 40;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                // Draw background circle
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(10));
                g2d.drawOval(x, y, size, size);

                // Draw progress arc
                g2d.setColor(PRIMARY_COLOR);
                double percentage = (double) remainingSeconds / DEFAULT_REST_TIME;
                int startAngle = 90;
                int arcAngle = (int) (percentage * 360);
                g2d.drawArc(x, y, size, size, startAngle, arcAngle);
            }
        };
        circularProgress.setPreferredSize(new Dimension(200, 200));

        // Time label
        timeLabel = new JLabel(formatTime(remainingSeconds));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Control buttons panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        controlPanel.setBackground(Color.WHITE);

        playPauseButton = createControlButton("⏯"); // Use the existing playPauseButton
        playPauseButton.setIcon(playIcon); // Set the icon here
        playPauseButton.setBorderPainted(false);
        playPauseButton.setContentAreaFilled(false);
        playPauseButton.setFocusPainted(false);
        playPauseButton.addActionListener(e -> toggleTimer());

        controlPanel.add(playPauseButton);

        // Time adjustment buttons panel
        JPanel adjustmentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        adjustmentPanel.setBackground(Color.WHITE);

        JButton decreaseButton = createControlButton("-30s");
        decreaseButton.addActionListener(e -> adjustTime(-30));

        JButton increaseButton = createControlButton("+30s");
        increaseButton.addActionListener(e -> adjustTime(30));

        adjustmentPanel.add(decreaseButton);
        adjustmentPanel.add(increaseButton);

        // Layout assembly
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(circularProgress, BorderLayout.CENTER);
        centerPanel.add(timeLabel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(adjustmentPanel, BorderLayout.NORTH);

        add(mainPanel);
    }

    private void toggleTimer() {
        isTimerRunning = !isTimerRunning;
        if (isTimerRunning) {
            if (pauseIcon != null) {
                playPauseButton.setIcon(pauseIcon);
            } else {
                playPauseButton.setText("Pause");
            }
            timer.start();
        } else {
            if (playIcon != null) {
                playPauseButton.setIcon(playIcon);
            } else {
                playPauseButton.setText("Play");
            }
            timer.stop();
        }
    }

    private JButton createControlButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setPreferredSize(new Dimension(80, 35));
        button.setBorder(new WorkoutScreen.RoundedBorder(17, PRIMARY_COLOR));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void loadIcons() {
        try {
            // Using absolute file paths
            File playFile = new File(AppPaths.getInstance().getPlayImage());
            File pauseFile = new File(AppPaths.getInstance().getPauseImage());

            if (!playFile.exists() || !pauseFile.exists()) {
                System.err.println("Could not find icon files. Using text fallback.");
                return;
            }

            playIcon = new ImageIcon(playFile.getAbsolutePath());
            pauseIcon = new ImageIcon(pauseFile.getAbsolutePath());

            // Scale icons to a reasonable size
            Image playImg = playIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            Image pauseImg = pauseIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);

            playIcon = new ImageIcon(playImg);
            pauseIcon = new ImageIcon(pauseImg);
        } catch (Exception e) {
            System.err.println("Error loading icons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (!isPaused) {
                remainingSeconds--;
                timeLabel.setText(formatTime(remainingSeconds));
                circularProgress.repaint();

                if (remainingSeconds <= 0) {
                    timer.stop();
                    playCompletionSound();
                    dispose();
                }
            }
        });
        timer.start();
    }

    private void togglePausePlay() {
        isPaused = !isPaused;
    }

    private void adjustTime(int seconds) {
        remainingSeconds = Math.max(0, remainingSeconds + seconds);
        timeLabel.setText(formatTime(remainingSeconds));
        circularProgress.repaint();
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    private void playCompletionSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    getClass().getResource(AppPaths.getInstance().getBellSound()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
