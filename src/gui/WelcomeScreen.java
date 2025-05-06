package gui;

import javax.swing.*;
import java.awt.*;

/**
 * WelcomeScreen is the initial JFrame of the Green-Er consumption application.
 * it provides the main interface for the user to navigate to other screens of the application
 */
public class WelcomeScreen extends JFrame {

    /**
     * Constructor for the WelcomeScreen JFrame.
     * Initializes the main screen with welcome message, buttons, and images.
     */
    public WelcomeScreen() {
        setTitle("Green-Er Consumption Application");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Green-Er consumption application", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton creditsButton = new JButton("Credits");
        creditsButton.setPreferredSize(new Dimension(150, 40));
        creditsButton.addActionListener(e -> {
            new Credits(this);
            setVisible(false);
        });

        JButton howToUseButton = new JButton("How to Use");
        howToUseButton.setPreferredSize(new Dimension(150, 40));
        howToUseButton.addActionListener(e -> {
            new HowToUse(this);
            setVisible(false);
        });

        buttonPanel.add(creditsButton);
        buttonPanel.add(howToUseButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel imageButtonPanel = new JPanel();
        imageButtonPanel.setLayout(new GridLayout(2, 2, 20, 20));

        JLabel greenerImageLabel = new JLabel(resizeImage("Green-Er.jpg", 300, 200));
        greenerImageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageButtonPanel.add(greenerImageLabel);

        JButton greenerDataButton = new JButton("GreenEr_Data");
        greenerDataButton.setPreferredSize(new Dimension(200, 30));
        greenerDataButton.addActionListener(e -> {
            new GreenErDataScreen(this);
            setVisible(false);
        });
        JPanel greenerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        greenerButtonPanel.add(greenerDataButton);
        imageButtonPanel.add(greenerButtonPanel);

        JLabel classroomImageLabel = new JLabel(resizeImage("Classroom_4A020.png", 300, 200));
        classroomImageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageButtonPanel.add(classroomImageLabel);

        JButton classroomButton = new JButton("classRoom_4A020");
        classroomButton.setPreferredSize(new Dimension(200, 30));
        classroomButton.addActionListener(e -> {
            new ClassRoomWindow(this);
            setVisible(false);
        });
        JPanel classroomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        classroomButtonPanel.add(classroomButton);
        imageButtonPanel.add(classroomButtonPanel);

        mainPanel.add(imageButtonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Resizes an image to the given width and height.
     *
     * @param filePath the path to the image file
     * @param width    the desired width
     * @param height   the desired height
     * @return an ImageIcon with the resized image
     */
    private ImageIcon resizeImage(String filePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(filePath);
        Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * Main method to launch the WelcomeScreen application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }
}
