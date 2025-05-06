package gui;

import javax.swing.*;
import java.awt.*;

public class Credits extends JFrame {

    /**
     * Constructor for the Credits JFrame.
     * Initializes the credits window with details about the application and contributors.
     *
     * @param parent the parent JFrame, used to bring it back to visibility when the "Back" button is pressed.
     */
    public Credits(JFrame parent) {
        setTitle("Credits");
        setSize(1200, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label1 = new JLabel("This application was created by ASFORA Arthur as project to the subject IT Tools and Optimization from the master ASI of Ense3.", JLabel.CENTER);
        label1.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label1, BorderLayout.NORTH);

        JLabel label2 = new JLabel("I would like to thank professors FREISEM Léonard and RAMDANE Brahim who helped to develop this work.", JLabel.CENTER);
        label2.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label2, BorderLayout.CENTER);

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
