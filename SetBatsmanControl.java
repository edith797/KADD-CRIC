import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SetBatsmanControl extends JFrame {

    private JComboBox<String> cbStriker;
    private JComboBox<String> cbNonStriker;
    private JButton btStart;
    private JButton btHomePage;
    private JLabel lblsetbatsman;

    // New instance variables to store striker and non-striker names
    private static String selectedStriker;
    private static String selectedNonStriker;

    public SetBatsmanControl(String chosenOption, String teamAName, String teamBName, String tossWinner, String NotatossWinner , int overLimit, String venue, String matchName) {
        initializeUI(chosenOption, teamAName, teamBName, tossWinner, NotatossWinner);
        fetchPlayerNames(); // Fetch and populate player names when the UI is initialized
    }

    public static String getNotatossWinner() {
        return TossWinnerWindow.notATossWinner;
    }

    private void initializeUI(String chosenOption, String teamAName, String teamBName, String tossWinner, String NotatossWinner) {
        setTitle("Set Batsman");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove the title bar
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(null);
        getContentPane().setBackground(new Color(0, 0, 0)); // Set background color

        String battingTeam;
        String bowlingTeam;
        if (chosenOption.equals("Bat")) {
            battingTeam = tossWinner;
            bowlingTeam = NotatossWinner;
        } else if (chosenOption.equals("Bowl") && tossWinner.equals(teamAName)) {
            battingTeam = teamBName;
            bowlingTeam = teamAName;
        } else {
            battingTeam = teamAName;
            bowlingTeam = teamBName;
        }

        lblsetbatsman = new JLabel("Batting Team: " + battingTeam);
        lblsetbatsman.setBounds(50, 50, 800, 30); // Adjusted width to match the entire panel
        lblsetbatsman.setForeground(Color.WHITE);
        lblsetbatsman.setFont(new Font("Candara Bold Italic", Font.PLAIN, 24)); // Adjusted font size
        lblsetbatsman.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        add(lblsetbatsman);

        JLabel strikerLabel = new JLabel("Striker:");
        strikerLabel.setBounds(158, 225, 171, 60);
        strikerLabel.setForeground(Color.WHITE); // Set font color
        strikerLabel.setFont(new Font("Candara Bold Italic", Font.PLAIN, 28));
        add(strikerLabel);

        cbStriker = new JComboBox<>();
        cbStriker.setBounds(461, 225, 300, 60);
        cbStriker.setFont(new Font("Candara,Calibri,Segoe,Segoe UI,Optima,Arial,sans-serif", Font.BOLD | Font.ITALIC, 16));
        cbStriker.setBackground(Color.WHITE);
        add(cbStriker);

        JLabel nonStrikerLabel = new JLabel("Non-Striker:");
        nonStrikerLabel.setBounds(158, 314, 171, 60);
        nonStrikerLabel.setForeground(Color.WHITE); // Set font color
        nonStrikerLabel.setFont(new Font("Candara Bold Italic", Font.PLAIN, 28));
        add(nonStrikerLabel);

        cbNonStriker = new JComboBox<>();
        cbNonStriker.setBounds(461, 314, 300, 60);
        cbNonStriker.setFont(new Font("Candara,Calibri,Segoe,Segoe UI,Optima,Arial,sans-serif", Font.BOLD | Font.ITALIC, 16));
        cbNonStriker.setBackground(Color.WHITE);
        add(cbNonStriker);

        btStart = new JButton("Next");
        btStart.setBounds(682, 422, 136, 52);
        btStart.setBackground(Color.WHITE);
        btStart.setFont(new Font("Candara Bold Italic", Font.PLAIN, 25));
        btStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Store the selected striker and non-striker names
                selectedStriker = (String) cbStriker.getSelectedItem();
                selectedNonStriker = (String) cbNonStriker.getSelectedItem();

                // Check if the striker and non-striker are the same
                if (selectedStriker.equals(selectedNonStriker)) {
                    JOptionPane.showMessageDialog(null, "Striker and non-striker cannot be the same.");
                    return; // Exit the method if striker and non-striker are the same
                }

                dispose();
                SwingUtilities.invokeLater(() -> {
                    new ScoreBoardSplitPaneControl(selectedStriker, selectedNonStriker, bowlingTeam, battingTeam , tossWinner, chosenOption, OverOptionSwing.overLimit, PlayerListTeam1Control.venue, PlayerListTeam1Control.matchName).setVisible(true);
                });
            }
        });
        add(btStart);

        btHomePage = new JButton("HomePage");
        btHomePage.setBounds(386, 423, 150, 50);
        btHomePage.setBackground(Color.WHITE);
        btHomePage.setFont(new Font("Candara Bold Italic", Font.PLAIN, 25));
        btHomePage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ex) {
                dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new HomePageSwing();
                    }
                });
            }
        });
        add(btHomePage);

        setVisible(true);
    }

    private void fetchPlayerNames() {
        String selectedTeam = lblsetbatsman.getText().replace("Batting Team: ", "").trim(); // Get the selected batting team

        // Retrieve player names for the selected team from the database
        String url = "jdbc:postgresql://localhost:5432/Ipl";
        String user = "postgres";
        String password = "dharsh";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT player_name FROM " + selectedTeam;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String playerName = resultSet.getString("player_name");
                cbStriker.addItem(playerName);
                cbNonStriker.addItem(playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void start(ActionEvent event) {
        // Implement the start logic here
    }

    private void homepage(ActionEvent event) {
        // Implement the homepage logic here
    }

    // Getters for the selected striker and non-striker names
    public static String getSelectedStriker() {
        return selectedStriker;
    }

    public static String getSelectedNonStriker() {
        return selectedNonStriker;
    }

    public static void main(String[] args) {
        String chosenOption = TossWinnerWindow.chosenOption;
        String teamAName = TossWinnerWindow.teamAName;
        String teamBName = TossWinnerWindow.teamBName;
        String tossWinner = TossWinnerWindow.tossWinner;
        String NotatossWinner = TossWinnerWindow.notATossWinner;

        SwingUtilities.invokeLater(() -> {
            SetBatsmanControl frame = new SetBatsmanControl(chosenOption, teamAName, teamBName, tossWinner, NotatossWinner, OverOptionSwing.overLimit, PlayerListTeam1Control.venue, PlayerListTeam1Control.matchName);
        });
    }
}