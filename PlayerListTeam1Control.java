import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

public class PlayerListTeam1Control extends JFrame {
    private JComboBox<String> cbTeamName1;
    private JComboBox<String> cbTeamName2;
    private java.util.List<JCheckBox> cbPlayerNames1;
    private java.util.List<JCheckBox> cbPlayerNames2;
    private JButton btSubmit;
    private BufferedImage backgroundImage;
    private JPanel playerPanel1;
    private JPanel playerPanel2;

    public static int overLimit;
    public static String venue;
    public static String matchName;

    public PlayerListTeam1Control(int overLimit) {
        this.overLimit = overLimit;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove the title bar

        setResizable(true);
        setLayout(null);

        try {
            backgroundImage = ImageIO.read(new File("VS.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);

        try {
            initComponents();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        setCbTeamSelect(); // Populate combo boxes with table names

        // Ensure frame is visible after components are added
        setSize(1000, 1000);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void setOverLimit(int overLimit) {
        this.overLimit = overLimit;
    }

    private void initComponents() throws FontFormatException {
        JLabel teamLabel1 = new JLabel("TEAM 1 NAME:");
        teamLabel1.setBounds(290, 25, 250, 25); // Adjusted width for more spacing
        teamLabel1.setForeground(Color.WHITE);
        try {
            teamLabel1.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 30)); // Changed font style
        } catch (Exception e) {
            teamLabel1.setFont(new Font("SansSerif", Font.BOLD, 30)); // Fallback font
            e.printStackTrace();
        }
        getContentPane().add(teamLabel1);

        cbTeamName1 = createComboBox(290, 80); // Increased spacing

        JLabel teamLabel2 = new JLabel("TEAM 2 NAME:");
        teamLabel2.setBounds(1030, 25, 250, 25); // Adjusted width for more spacing
        teamLabel2.setForeground(Color.WHITE);
        try {
            teamLabel2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 30)); // Changed font style
        } catch (Exception e) {
            teamLabel2.setFont(new Font("SansSerif", Font.BOLD, 30)); // Fallback font
            e.printStackTrace();
        }
        getContentPane().add(teamLabel2);

        cbTeamName2 = createComboBox(1030, 80); // Increased spacing

        cbPlayerNames1 = new ArrayList<>();
        cbPlayerNames2 = new ArrayList<>();

        playerPanel1 = createPlayerPanel(290, 150);
        playerPanel2 = createPlayerPanel(1030, 150);

        getContentPane().add(playerPanel1);
        getContentPane().add(playerPanel2);

        btSubmit = new JButton("SUBMIT");
        btSubmit.setBounds(700, 800, 150, 40); // Adjusted position for more space
        btSubmit.setFont(new Font("SansSerif", Font.BOLD, 20)); // Changed font style
        btSubmit.setBackground(Color.WHITE);
        btSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    submit(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        getContentPane().add(btSubmit);
    }

    private JComboBox<String> createComboBox(int x, int y) {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setBounds(x, y, 250, 40);
        comboBox.setFont(new Font("SansSerif", Font.BOLD, 18)); // Changed font style
        comboBox.setForeground(Color.BLACK);
        getContentPane().add(comboBox);
        return comboBox;
    }

    private JPanel createPlayerPanel(int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, 250, 800); // Adjust height as needed
        panel.setOpaque(false);
        return panel;
    }

    public void submit(ActionEvent event) throws Exception {
        if (isInvalidInput()) {
            JOptionPane.showMessageDialog(this, "Please input all team and player names", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isSameName(cbTeamName1.getSelectedItem().toString(), cbTeamName2.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Please use different team names", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        /*if (isDuplicatePlayerNames(cbPlayerNames1) || isDuplicatePlayerNames(cbPlayerNames2)) {
            JOptionPane.showMessageDialog(this, "Please use different player names within each team", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }*/

        JComboBox<String> venueComboBox = new JComboBox<>();
        venueComboBox.addItem("Chepauk Stadium");
        venueComboBox.addItem("Wankhede Stadium");
        venueComboBox.addItem("Chinnaswamy Stadium");
        venueComboBox.addItem("Eden Gardens Stadium");
        venueComboBox.addItem("Nehru Stadium");

        JTextField matchNameTextField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Venue:"));
        panel.add(venueComboBox);
        panel.add(new JLabel("Match Name:"));
        panel.add(matchNameTextField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Enter Venue and Match Name", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            venue = (String) venueComboBox.getSelectedItem();
            matchName = matchNameTextField.getText();
            if (!matchName.isEmpty()) {
                // Proceed with the next step
                dispose();
                // Navigate to TossWinnerWindow
                try {
                    TossWinnerWindow tossWinnerWindow = new TossWinnerWindow(cbTeamName1.getSelectedItem().toString(), cbTeamName2.getSelectedItem().toString(), overLimit, venue, matchName);
                    tossWinnerWindow.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter match name", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isInvalidInput() {
        //if (cbTeamName1.getSelectedItem() == null || cbTeamName2.getSelectedItem() == null) return true;

        int selectedPlayersCount1 = 0;
        for (JCheckBox cbPlayerName : cbPlayerNames1) {
            if (cbPlayerName.isSelected()) selectedPlayersCount1++;
        }

        int selectedPlayersCount2 = 0;
        for (JCheckBox cbPlayerName : cbPlayerNames2) {
            if (cbPlayerName.isSelected()) selectedPlayersCount2++;
        }

        // Check if both teams have exactly 11 players selected
        return selectedPlayersCount1 != 11 || selectedPlayersCount2 != 11;
    }

    private boolean isSameName(String name1, String name2) {
        return name1.equalsIgnoreCase(name2);
    }

    private boolean isDuplicatePlayerNames(java.util.List<JCheckBox> cbPlayerNames) {
        ArrayList<String> playerNameList = new ArrayList<>();
        for (JCheckBox cbPlayerName : cbPlayerNames) {
            playerNameList.add(cbPlayerName.getText());
        }
        for (String playerName : playerNameList) {
            if (Collections.frequency(playerNameList, playerName) > 1) {
                return true;
            }
        }
        return false;
    }

    private void setCbTeamSelect() {
        String url = "jdbc:postgresql://localhost:5432/Ipl";
        String user = "postgres";
        String password = "dharsh";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                cbTeamName1.addItem(tableName);
                cbTeamName2.addItem(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }

        // Add action listeners to the team combo boxes
        cbTeamName1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTeam = (String) cbTeamName1.getSelectedItem();
                if (selectedTeam != null) {
                    populatePlayerDropdown(selectedTeam, cbPlayerNames1, playerPanel1);
                }
            }
        });

        cbTeamName2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTeam = (String) cbTeamName2.getSelectedItem();
                if (selectedTeam != null) {
                    populatePlayerDropdown(selectedTeam, cbPlayerNames2, playerPanel2);
                }
            }
        });
    }

    private void populatePlayerDropdown(String teamName, java.util.List<JCheckBox> checkBoxes, JPanel panel) {
        String url = "jdbc:postgresql://localhost:5432/Ipl";
        String user = "postgres";
        String password = "dharsh";
        String query = "SELECT player_name FROM " + teamName;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Clear existing checkboxes
            checkBoxes.clear();
            panel.removeAll();

            // Add player names to the checkboxes
            int yPosition = 0;
            while (resultSet.next()) {
                String playerName = resultSet.getString("player_name");
                JCheckBox checkBox = new JCheckBox(playerName);
                checkBox.setFont(new Font("SansSerif", Font.BOLD, 18)); // Set font style
                checkBox.setForeground(Color.WHITE);
                checkBox.setOpaque(false); // Set checkbox background to be transparent
                checkBox.setBounds(0, yPosition, 250, 40); // Adjust checkbox position
                checkBoxes.add(checkBox);
                panel.add(checkBox);
                yPosition += 40; // Increment position for the next checkbox
            }

            panel.setPreferredSize(new Dimension(250, yPosition)); // Adjust panel size
            panel.revalidate();
            panel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PlayerListTeam1Control(0);
            }
        });
    }
}