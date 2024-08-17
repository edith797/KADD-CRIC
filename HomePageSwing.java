import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class HomePageSwing extends JFrame {

    public HomePageSwing() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); // Remove the title bar

        setSize(900, 770);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load background image
                URL imageUrl = getClass().getResource("home1.jpg");
                if (imageUrl != null) {
                    ImageIcon backgroundImage = new ImageIcon(imageUrl);
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Handle error when background image cannot be loaded
                    System.err.println("Error loading background image.");
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout()); // Use BorderLayout for precise positioning
        add(backgroundPanel);

        // Load app logo GIF
        URL logoUrl = getClass().getResource("text.gif");
        if (logoUrl != null) {
            ImageIcon logoIcon = new ImageIcon(logoUrl);
            JLabel logoLabel = new JLabel(logoIcon);
            backgroundPanel.add(logoLabel, BorderLayout.NORTH); // Add the logo to the top of the backgroundPanel
        } else {
            // Handle error when logo GIF cannot be loaded
            System.err.println("Error loading app logo GIF.");
        }

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 20)); // 4 rows, 1 column, vertical gap of 20 pixels
        buttonPanel.setOpaque(false); // Make the panel transparent

        // Create buttons
        JButton btNewMatch = createStyledButton("New Match");
        JButton btOptions = createStyledButton("Options");
        JButton btExit = createStyledButton("EXIT");

        // Add buttons to the buttonPanel
        buttonPanel.add(btNewMatch);
        buttonPanel.add(btOptions);
        buttonPanel.add(btExit);

        // Add buttonPanel to the right of backgroundPanel
        backgroundPanel.add(buttonPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Candara Bold Italic", Font.BOLD | Font.ITALIC, 35));
        button.setContentAreaFilled(false); // Set content area filled to false
        button.setBorderPainted(false); // Remove border
        button.setForeground(Color.PINK); // Set text color
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Align buttons to the center

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.RED); // Change text color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.PINK); // Reset text color on exit
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Options")) {
                    // Close the current window
                    dispose();

                    // Open the OptionsWindow
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new OptionsWindow();
                        }
                    });
                } else if (text.equals("New Match")) {
                    // Close the current window
                    dispose();

                    // Open the OverOptionSwing window
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new OverOptionSwing();
                        }
                    });
                } else if (text.equals("EXIT")) {
                    // Action for Exit button
                    System.exit(0);
                }
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePageSwing::new);
    }
}
