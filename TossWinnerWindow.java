import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TossWinnerWindow extends JFrame {
    private TransparentPanel transparentPanel;
    private JLabel lblTeamA;
    private JLabel lblTeamB;
    private JLabel lblTossResult;
    private JLabel lblTossChoice;
    private JLabel lblGif;

    private JButton btnTeamA;
    private JButton btnTeamB;
    private JButton submitButton;

    public static String teamAName;
    public static String teamBName;
    public static String tossWinner;
    public static String notATossWinner;
    public static String chosenOption;

    public TossWinnerWindow(String teamAName, String teamBName, int overLimit) {
        this(teamAName, teamBName, overLimit, "", "");
    }

    public TossWinnerWindow(String teamAName, String teamBName, int overLimit, String venue, String matchName) {
        this.teamAName = teamAName;
        this.teamBName = teamBName;

        setTitle("Toss Winner - " + teamAName + " vs " + teamBName);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set the content pane to a custom JPanel with a gradient background
        setContentPane(new GradientPanel());

        setLayout(new GridBagLayout());

        initComponents(overLimit, venue, matchName);
        addComponents();
    }

    private void initComponents(int overLimit, String venue, String matchName) {
        transparentPanel = new TransparentPanel();
        lblTeamA = new JLabel("Team A: " + teamAName);
        lblTeamB = new JLabel("Team B: " + teamBName);
        lblTossResult = new JLabel("Toss Winner: ");
        lblTossChoice = new JLabel("Choose to Bat or Bowl");
        lblGif = new JLabel();

        lblGif.setVisible(false);

        btnTeamA = new JButton(teamAName);
        btnTeamB = new JButton(teamBName);
        submitButton = new JButton("Submit");
        submitButton.setVisible(false);

        // Styling the buttons
        styleButton(btnTeamA);
        styleButton(btnTeamB);
        styleButton(submitButton);

        // Styling the labels
        styleLabel(lblTeamA);
        styleLabel(lblTeamB);
        styleLabel(lblTossResult);
        styleLabel(lblTossChoice);

        ActionListener teamButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnTeamA) {
                    lblTeamA.setForeground(Color.PINK);
                    lblTeamB.setForeground(Color.WHITE);
                    tossWinner = teamAName;
                    notATossWinner = teamBName;
                } else {
                    lblTeamB.setForeground(Color.PINK);
                    lblTeamA.setForeground(Color.WHITE);
                    tossWinner = teamBName;
                    notATossWinner = teamAName;
                }
                chosenOption = showChoiceDialog();
                lblTossResult.setText("Toss Winner: " + tossWinner);
                lblTossChoice.setText("Choice: " + chosenOption);
                lblGif.setVisible(true);
                btnTeamA.setVisible(false);
                btnTeamB.setVisible(false);
                submitButton.setVisible(true);
                Timer timer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        lblGif.setVisible(false);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        };

        btnTeamA.addActionListener(teamButtonListener);
        btnTeamB.addActionListener(teamButtonListener);

        submitButton.addActionListener(e -> {
            // Update toss winner in the database
            //updateTossWinnerInDatabase(tossWinner, chosenOption, overLimit, PlayerListTeam1Control.venue, PlayerListTeam1Control.matchName);
            // Open new screen or perform other actions
            // Close current window
            dispose();

            // Open the SetBatsmanControl window
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SetBatsmanControl setBatsmanControl = new SetBatsmanControl(chosenOption, teamAName, teamBName, tossWinner, notATossWinner, overLimit, venue, matchName);
                    // Set any necessary data in the SetBatsmanControl instance
                    setBatsmanControl.setVisible(true);
                }
            });
        });
    }

    private void addComponents() {
        transparentPanel.setPreferredSize(new Dimension(600, 400));
        transparentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        transparentPanel.add(lblTeamA, gbc);

        gbc.gridy = 1;
        transparentPanel.add(lblTeamB, gbc);

        gbc.gridy = 2;
        transparentPanel.add(lblTossResult, gbc);

        gbc.gridy = 3;
        transparentPanel.add(lblTossChoice, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        transparentPanel.add(lblGif, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        transparentPanel.add(btnTeamA, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        transparentPanel.add(btnTeamB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        transparentPanel.add(submitButton, gbc);

        add(transparentPanel);
    }

    private String showChoiceDialog() {
        // Custom buttons
        JButton batButton = new JButton("Bat");
        JButton bowlButton = new JButton("Bowl");

        // Apply the same style to custom buttons
        styleButton(batButton);
        styleButton(bowlButton);

        // Specific colors for "Bat" and "Bowl" buttons
        batButton.setBackground(new Color(0, 128, 0)); // Green
        bowlButton.setBackground(new Color(220, 20, 60)); // Crimson

        // Custom message label
        JLabel messageLabel = new JLabel("Choose to Bat or Bowl");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment for the message

        // Panel to hold message and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(74, 11, 69)); // Match background color
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(batButton);
        buttonPanel.add(bowlButton);
        buttonPanel.setBackground(new Color(74, 11, 69)); // Match background color

        // Add message and buttons to main panel
        panel.add(messageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        panel.add(buttonPanel);

        // Dialog to display the panel
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setSize(300, 95);
        dialog.setLocationRelativeTo(this);

        // Action listeners for buttons
        final String[] choice = new String[1];
        batButton.addActionListener(e -> {
            choice[0] = "Bat";
            dialog.dispose();
        });
        bowlButton.addActionListener(e -> {
            choice[0] = "Bowl";
            dialog.dispose();
        });

        dialog.setVisible(true);

        return choice[0];
    }



    private void updateTossWinnerInDatabase(String tossWinner, String chosenOption, int overLimit, String venue, String matchName) {
        String url = "jdbc:postgresql://localhost:5432/matches";
        String user = "postgres";
        String password = "dharsh";
        String query = "INSERT INTO iplmatch (toss_win, toss_choice, date_, time_, overlimit, match_name, venue) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set toss winner and chosen option
            statement.setString(1, tossWinner);
            statement.setString(2, chosenOption);

            // Get current date without time
            LocalDate currentDate = LocalDate.now();
            // Set current date
            statement.setDate(3, java.sql.Date.valueOf(currentDate));

            // Get current time with AM/PM format
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedTime = currentTime.format(formatter);

            // Set current time
            statement.setString(4, formattedTime);

            statement.setInt(5, overLimit);
            statement.setString(6, matchName);
            statement.setString(7, venue);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Toss winner updated successfully.");
            } else {
                System.out.println("Failed to update toss winner.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(74, 11, 69)); // Matching the deep pink color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE); // Setting label text color to white
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Setting a bold font for better readability
    }

    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(5, 1, 58); // Violet
            Color color2 = new Color(74, 11, 69); // Deep Pink
            GradientPaint gp = new GradientPaint(0, 0, color1, width, 0, color2); // Left to right
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    private class TransparentPanel extends JPanel {
        public TransparentPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 150)); // Black with some transparency
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Rounded corners
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TossWinnerWindow("Team A", "Team B", 20).setVisible(true));
    }
}