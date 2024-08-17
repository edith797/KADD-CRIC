import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OverOptionSwing extends JFrame {

    private static JLabel selectLabel; // Change selectLabel to static
    public static int overLimit = 0;

    public OverOptionSwing() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); // Remove the title bar
        setLayout(new BorderLayout());

        // Create background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load background image
                ImageIcon backgroundImage = new ImageIcon("overs.jpg"); // Adjust the file name as per your image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // Create left panel for the static text
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(400, getHeight()));
        backgroundPanel.add(leftPanel, BorderLayout.WEST);

        // Add static text to left panel
        selectLabel = new JLabel("SELECT OVERS");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 40));
        selectLabel.setForeground(Color.WHITE); // Change text color to white
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(selectLabel, gbc);

        // Create right panel for the buttons
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = GridBagConstraints.RELATIVE;
        gbcRight.insets = new Insets(10, 0, 10, 0); // Add some padding between buttons
        backgroundPanel.add(rightPanel, BorderLayout.EAST);

        // Create and add buttons
        JButton btFiveOver = createButton("5-Over", Color.GREEN);
        rightPanel.add(btFiveOver, gbcRight);

        JButton btTenOver = createButton("10-Over", Color.BLUE);
        rightPanel.add(btTenOver, gbcRight);

        JButton btTwenty = createButton("T-20", Color.ORANGE);
        rightPanel.add(btTwenty, gbcRight);

        JButton btODI = createButton("ODI", Color.RED);
        rightPanel.add(btODI, gbcRight);

        // Create home button
        JButton btHomePage = new JButton("HomePage");
        btHomePage.setFont(new Font("Arial", Font.BOLD, 20));
        btHomePage.setForeground(Color.WHITE);
        btHomePage.setBackground(new Color(0, 0, 0)); // Green color
        btHomePage.setFocusPainted(false);
        btHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window
                dispose();

                // Open the HomePageSwing window again
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new HomePageSwing();
                    }
                });
            }
        });
        add(btHomePage, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(150, 50));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setForeground(Color.WHITE);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.PINK); // Change text color to yellow on hover
                button.setBackground(color.brighter()); // Lighten background color on hover
                button.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE); // Reset text color on exit
                button.setBackground(null); // Reset background color on exit
                button.setFont(new Font("Arial", Font.BOLD, 20)); // Reset font size on exit
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open PlayerListTeam1Control window based on button clicked
                switch (text) {
                    case "5-Over":
                        overLimit = 5;
                        break;
                    case "10-Over":
                        overLimit = 10;
                        break;
                    case "T-20":
                        overLimit = 20;
                        dispose();
                        new PlayerListTeam1Control(20).setVisible(true);
                        return;
                    case "ODI":
                        dispose();
                        new PlayerListTeam1Control(50).setVisible(true);
                        return;
                }
                dispose();
                new PlayerListTeam1Control(overLimit).setVisible(true);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OverOptionSwing::new);
    }
}
