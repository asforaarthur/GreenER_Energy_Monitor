package gui;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame that displays the instructions on how to use the application.
 * Provides a text area with step-by-step instructions and a "Back" button to return to the parent window.
 */
public class HowToUse extends JFrame {

    /**
     * Constructor for the HowToUse JFrame.
     * Initializes the "How to Use" window with instructions for the user and a back button.
     *
     * @param parent the parent JFrame, used to bring it back to visibility when the "Back" button is pressed.
     */
    public HowToUse(JFrame parent) {
        setTitle("How to Use");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Text area displaying the instructions
        JTextArea textArea = new JTextArea();
        textArea.setText(
            "Welcome to the Green-Er consumption application!\n\n" +
            "Instructions:\n" +
            "1. Click on the 'GreenEr_Data' button to compare and plot the data related to energy consumption, production, radiation, and outdoor temperature of GreenEr building.\n" +
            "2. Click on the 'classRoom_4A020' button to plot the energy consumption of all notebooks in the classroom 4A020 and see if the indoor temperature matches with the reference temperature.\n" +
            "3. You can change the sampling time in both cases to make it more convenient for your analysis. \n"
        );
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}
