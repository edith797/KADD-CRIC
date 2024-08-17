import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoardSplitPaneControl {
    public static String battingTeam;
    public static String notAtossWinner;
    public static String bowlingTeam;
    private int runsOffThisOver = 0;
    private JTable table1;
    private JTable table2;
    private String selectedStriker;
    private String selectedNonStriker;
    private int[] playerRuns = {0, 0};
    private int[] playerBalls = {0, 0};
    private Map<String, BowlerStats> bowlerStatsMap = new HashMap<>();
    private int wickets = 0;
    public int totalScore = 0;
    public int extras = 0;
    private JTextField totalScoreTextField;
    private JTextField wicketTextField;
    private JTextField extrasTextField;
    private JTextField oversTextField;
    public int balls = 0;
    public int overs = 0;
    private JComboBox<String> setBowlerComboBox;
    JFrame frame = new JFrame("Runs Updater");

    public ScoreBoardSplitPaneControl(String selectedStriker, String selectedNonStriker, String bowlingTeam, String battingTeam, String tossWinner, String chosenOption, int overLimit, String venue, String matchName) {
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.selectedStriker = selectedStriker;
        this.selectedNonStriker = selectedNonStriker;
        initializeUI(bowlingTeam, battingTeam);
    }

    public List<String> initializeUI(String notAtossWinner, String battingTeam) {

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel inningsLabel = new JLabel("1st INNINGS", SwingConstants.LEFT);
        inningsLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel teamsLabel = new JLabel(PlayerListTeam1Control.matchName, SwingConstants.CENTER);
        teamsLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String[] columnNamesTable1 = {"BATTING", "OUT/NOT OUT", "R", "B", "4s", "6s", "SR"};
        String[] columnNamesTable2 = {"BOWLING", "OVER", "M", "R", "W", "WB", "ECON"};

        table1 = createTable(columnNamesTable1, 40, 15);
        table2 = createTable(columnNamesTable2, 40, 15);

        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel labelsPanel = new JPanel(new BorderLayout());
        labelsPanel.add(inningsLabel, BorderLayout.WEST);
        labelsPanel.add(teamsLabel, BorderLayout.CENTER);
        upperPanel.add(labelsPanel, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablesPanel.add(new JScrollPane(table1));
        tablesPanel.add(new JScrollPane(table2));
        upperPanel.add(tablesPanel, BorderLayout.CENTER);

        JPanel totalScorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel totalScoreLabel = new JLabel("Total Score:");
        totalScoreTextField = new JTextField(10);
        totalScoreTextField.setEditable(false);
        totalScorePanel.add(totalScoreLabel);
        totalScorePanel.add(totalScoreTextField);

        JLabel wicketLabel = new JLabel("Wickets:");
        wicketTextField = new JTextField(10);
        wicketTextField.setEditable(false);
        JLabel extrasLabel = new JLabel("Extras:");
        extrasTextField = new JTextField(10);
        extrasTextField.setEditable(false);
        JLabel oversLabel = new JLabel("Overs:");
        oversTextField = new JTextField(10);
        oversTextField.setEditable(false);

        totalScorePanel.add(wicketLabel);
        totalScorePanel.add(wicketTextField);
        totalScorePanel.add(extrasLabel);
        totalScorePanel.add(extrasTextField);
        totalScorePanel.add(oversLabel);
        totalScorePanel.add(oversTextField);

        upperPanel.add(totalScorePanel, BorderLayout.SOUTH);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        JPanel runsByBatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel runsByBatLabel = new JLabel("Runs by Bat:");
        JComboBox<String> runsByBatComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "6", "0"});
        JButton submitRunsByBatButton = new JButton("Submit Runs by Bat");
        runsByBatPanel.add(runsByBatLabel);
        runsByBatPanel.add(runsByBatComboBox);
        runsByBatPanel.add(submitRunsByBatButton);

        JPanel switchBatsmanPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        JLabel switchBatsmanLabel = new JLabel("Switch Batsman:");
        JComboBox<String> switchBatsmanComboBox = new JComboBox<>();
        switchBatsmanPanel.add(switchBatsmanLabel);
        switchBatsmanPanel.add(switchBatsmanComboBox);

        DefaultComboBoxModel<String> switchBatsmanModel = new DefaultComboBoxModel<>();
        switchBatsmanModel.addElement(selectedStriker);
        switchBatsmanModel.addElement(selectedNonStriker);
        switchBatsmanComboBox.setModel(switchBatsmanModel);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        JButton outButton = new JButton("OUT");
        JButton wideBallButton = new JButton("Wide Ball");
        JButton noBallButton = new JButton("No Ball");
        buttonsPanel.add(outButton);
        buttonsPanel.add(noBallButton);
        buttonsPanel.add(wideBallButton);

        JPanel extraRunPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel extraRunLabel = new JLabel("Extra Run:");
        JComboBox<String> extraRunComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        JButton submitExtraRunButton = new JButton("Submit Extra Run");
        extraRunPanel.add(extraRunLabel);
        extraRunPanel.add(extraRunComboBox);
        extraRunPanel.add(submitExtraRunButton);

        JPanel setBowlerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        JLabel setBowlerLabel = new JLabel("Set Bowler:");
        setBowlerComboBox = new JComboBox<>();
        populateBowlerComboBox(setBowlerComboBox, notAtossWinner);
        JButton submitBowlerButton = new JButton("Submit Bowler");
        setBowlerPanel.add(setBowlerLabel);
        setBowlerPanel.add(setBowlerComboBox);
        setBowlerPanel.add(submitBowlerButton);

        lowerPanel.add(runsByBatPanel);
        lowerPanel.add(switchBatsmanPanel);
        lowerPanel.add(extraRunPanel);
        lowerPanel.add(setBowlerPanel);
        lowerPanel.add(buttonsPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, lowerPanel);
        splitPane.setResizeWeight(0.89);

        frame.add(splitPane);
        frame.setSize(600, 400);
        frame.setVisible(true);

        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        DefaultTableModel model2 = (DefaultTableModel) table2.getModel();

        model1.insertRow(0, new Object[]{selectedStriker, "Not Out", 0, 0, 0, 0, 0});
        model1.insertRow(1, new Object[]{selectedNonStriker, "Not Out", 0, 0, 0, 0, 0});


        submitRunsByBatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (balls >= 120) {
                    showMaxOversReachedDialog(submitRunsByBatButton, outButton, wideBallButton, noBallButton, submitExtraRunButton);
                    return;
                }

                String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);


                if (currentBowlerStats.balls >= 24) {
                    JOptionPane.showMessageDialog(null, "This bowler has reached the limit of 4 overs.");
                    return;
                }

                String selectedRunsByBat = (String) runsByBatComboBox.getSelectedItem();
                assert selectedRunsByBat != null;
                int runsToAdd = Integer.parseInt(selectedRunsByBat);
                totalScore += runsToAdd;
                totalScoreTextField.setText(String.valueOf(totalScore));

                currentBowlerStats.runs += runsToAdd;

                updateBowlerStats(selectedBowler, currentBowlerStats);
                incrementBallCount();

                int selectedBatsmanRow = getBatsmanRow(model1, selectedStriker);

                playerRuns[selectedBatsmanRow] += runsToAdd;
                playerBalls[selectedBatsmanRow] += 1;
                model1.setValueAt(playerRuns[selectedBatsmanRow], selectedBatsmanRow, 2);
                model1.setValueAt(playerBalls[selectedBatsmanRow], selectedBatsmanRow, 3);

                if (runsToAdd == 4) {
                    int fours = (int) model1.getValueAt(selectedBatsmanRow, 4);
                    model1.setValueAt(fours + 1, selectedBatsmanRow, 4);
                } else if (runsToAdd == 6) {
                    int sixes = (int) model1.getValueAt(selectedBatsmanRow, 5);
                    model1.setValueAt(sixes + 1, selectedBatsmanRow, 5);
                }

                double strikeRate = calculateStrikeRate(playerRuns[selectedBatsmanRow], playerBalls[selectedBatsmanRow]);
                model1.setValueAt(String.format("%.2f", strikeRate), selectedBatsmanRow, 6);

                if (runsToAdd % 2 == 1) {
                    switchStrikers();
                }
                //checkAndUpdateMaidenOver();
            }

            private void checkAndUpdateMaidenOver() {
                // Check if the current over is a maiden over (no runs scored)
                if (balls % 6 == 0 && runsOffThisOver == 0) {
                    String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                    BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
                    currentBowlerStats.maidens++;
                    updateBowlerStats(selectedBowler, currentBowlerStats); // Update the bowler's stats to reflect maiden over
                }
            }
        });


        submitExtraRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (balls >= 120) {
                    showMaxOversReachedDialog(submitRunsByBatButton, outButton, wideBallButton, noBallButton, submitExtraRunButton);
                    return;
                }

                String selectedExtraRun = (String) extraRunComboBox.getSelectedItem();
                assert selectedExtraRun != null;
                int extraRunToAdd = Integer.parseInt(selectedExtraRun);
                totalScore += extraRunToAdd;
                extras += extraRunToAdd;
                totalScoreTextField.setText(String.valueOf(totalScore));
                extrasTextField.setText(String.valueOf(extras));

                String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
                currentBowlerStats.runs += extraRunToAdd;
                updateBowlerStats(selectedBowler, currentBowlerStats);
                updateOver();
            }
        });



        outButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String outType = "Out";
                if (balls >= 120) {
                    showMaxOversReachedDialog(submitRunsByBatButton, outButton, wideBallButton, noBallButton, submitExtraRunButton);
                    return;
                }


                // Update the bowler's stats
                String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
                currentBowlerStats.wickets += 1;
                if (currentBowlerStats.balls >= 24) {
                    JOptionPane.showMessageDialog(null, "This bowler has reached the limit of 4 overs.");
                    return;
                }

                // Update wickets count
                wickets += 1;
                wicketTextField.setText(String.valueOf(wickets));

                updateBowlerStats(selectedBowler, currentBowlerStats);
                incrementBallCount();

                String[] options = {"Bowled", "Caught", "Runout"};
                int optionChosen = JOptionPane.showOptionDialog(null, "Select out type:",
                        "Out", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

                if (optionChosen == 0) { // Bowled selected
                    outType = "Bowled";
                } else if (optionChosen == 1) { // Caught selected
                    outType = "Caught";
                } else if (optionChosen == 2) { // Runout selected
                    outType = "Runout";
                }

                int selectedBatsmanRow = getBatsmanRow(model1, selectedStriker);

                playerBalls[selectedBatsmanRow] += 1;
                model1.setValueAt(playerBalls[selectedBatsmanRow], selectedBatsmanRow, 3);

                // Store the player who got out in a new row
                Object[] outPlayerData = new Object[model1.getColumnCount()];
                for (int i = 0; i < model1.getColumnCount(); i++) {
                    outPlayerData[i] = model1.getValueAt(selectedBatsmanRow, i);
                }
                model1.addRow(outPlayerData);

                // Mark the current batsman as out

                model1.setValueAt(outType, model1.getRowCount() - 1, 1);

                //updateBowlerStats(selectedBowler, currentBowlerStats);

                if (wickets >= 10) {
                    model1.removeRow(selectedBatsmanRow);
                    submitRunsByBatButton.setEnabled(false);
                    outButton.setEnabled(false);
                    wideBallButton.setEnabled(false);
                    noBallButton.setEnabled(false);
                    submitExtraRunButton.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "You have reached the maximum limit of 10 Wickets.");
                    int target = totalScore+1;
                    JOptionPane.showMessageDialog(null, "First innings Completed! \n Target Score = "+target);

                    SetBatsmanControl2 setBatsmanControl2 = new SetBatsmanControl2(bowlingTeam, battingTeam, target, TossWinnerWindow.tossWinner, TossWinnerWindow.chosenOption, OverOptionSwing.overLimit, PlayerListTeam1Control.venue, PlayerListTeam1Control.matchName); // Assuming SetBatsmanControl2 is the name of your class for the second screen
                    setBatsmanControl2.setVisible(true);
                    frame.dispose();

                    return;

                }

                // Fetch player names from the database
                List<String> playerNames = fetchPlayerNamesFromDatabase(battingTeam);

                playerNames.removeIf(player -> player.equals(selectedStriker) || player.equals(selectedNonStriker) || isPlayerOut(player, model1));

                // Create a combo box for selecting the next batsman
                JComboBox<String> nextBatsmanComboBox = new JComboBox<>(playerNames.toArray(new String[0]));
                int result = JOptionPane.showConfirmDialog(null, nextBatsmanComboBox, "Select Next Batsman", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "Invalid Batsman Name. Please try again.");
                    return;
                }

                String newBatsmanName = (String) nextBatsmanComboBox.getSelectedItem();
                if (newBatsmanName == null || newBatsmanName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid Batsman Name. Please try again.");
                    return;
                }

                switchBatsmanComboBox.removeItem(newBatsmanName);
                switchBatsmanComboBox.addItem(newBatsmanName);

                // Update the original row to show the new batsman
                //String newBatsman = JOptionPane.showInputDialog("Enter new batsman name:");
                model1.setValueAt(newBatsmanName, selectedBatsmanRow, 0); // Update name
                model1.setValueAt("Not Out", selectedBatsmanRow, 1); // Update status
                model1.setValueAt(0, selectedBatsmanRow, 2); // Reset runs
                model1.setValueAt(0, selectedBatsmanRow, 3); // Reset balls
                model1.setValueAt(0, selectedBatsmanRow, 4); // Reset 4s
                model1.setValueAt(0, selectedBatsmanRow, 5); // Reset 6s
                model1.setValueAt(0.0, selectedBatsmanRow, 6); // Reset strike rate

                // Add new batsman to switchBatsmanComboBox model
                DefaultComboBoxModel<String> switchBatsmanModel = (DefaultComboBoxModel<String>) switchBatsmanComboBox.getModel();
                switchBatsmanModel.addElement(newBatsmanName);

                // Reset the runs and balls for the new batsman
                playerRuns[selectedBatsmanRow] = 0;
                playerBalls[selectedBatsmanRow] = 0;

                // Prompt to select the new striker if required
                if (selectedStriker.equals(outPlayerData[0])) {
                    selectedStriker = newBatsmanName;
                } else {
                    selectedNonStriker = newBatsmanName;
                }

                incrementBallCount();
            }

            private boolean isPlayerOut(String playerName, DefaultTableModel model) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(playerName) && !"Not Out".equals(model.getValueAt(i, 1))) {
                        return true;
                    }
                }
                return false;
            }

        });


        noBallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extras += 1;
                totalScore += 1;
                totalScoreTextField.setText(String.valueOf(totalScore));
                extrasTextField.setText(String.valueOf(extras));

                String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
                currentBowlerStats.runs += 1;
                updateBowlerStats(selectedBowler, currentBowlerStats);
                updateOver();
            }
        });

        wideBallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extras += 1;
                totalScore += 1;
                totalScoreTextField.setText(String.valueOf(totalScore));
                extrasTextField.setText(String.valueOf(extras));

                String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
                currentBowlerStats.runs += 1;
                currentBowlerStats.wideBalls += 1;
                updateBowlerStats(selectedBowler, currentBowlerStats);
                updateOver();
            }
        });

        switchBatsmanComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedStriker = (String) switchBatsmanComboBox.getSelectedItem();
            }
        });

        submitBowlerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
                if (!bowlerStatsMap.containsKey(selectedBowler)) {
                    bowlerStatsMap.put(selectedBowler, new BowlerStats());
                    model2.addRow(new Object[]{selectedBowler, 0.0, 0, 0, 0, 0, 0.0});
                }
            }
        });
        return null;
    }


    private List<String> fetchPlayerNamesFromDatabase(String battingTeam) {
        List<String> playerNames = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/Ipl";
        String user = "postgres";
        String password = "dharsh";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT player_name FROM " + battingTeam + " WHERE player_name != '" + selectedStriker + "' AND player_name != '" + selectedNonStriker + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                playerNames.add(resultSet.getString("player_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerNames;
    }

    private void showMaxOversReachedDialog(JButton... buttons) {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
        JOptionPane.showMessageDialog(null, "You have reached the maximum limit of 20 overs.");
        int target = totalScore+1;
        JOptionPane.showMessageDialog(null, "First innings Completed! \n Target Score = "+target);
        SetBatsmanControl2 setBatsmanControl2 = new SetBatsmanControl2(bowlingTeam, battingTeam, target, TossWinnerWindow.tossWinner, TossWinnerWindow.chosenOption, OverOptionSwing.overLimit, PlayerListTeam1Control.venue, PlayerListTeam1Control.matchName ); // Assuming SetBatsmanControl2 is the name of your class for the second screen
        setBatsmanControl2.setVisible(true);
        frame.dispose();

    }


    private void updateOver() {
        // Calculate the total overs and remaining balls
        int oversInt = balls / 6;
        int remainingBalls = balls % 6;

        // Update the overs text field with the current overs
        oversTextField.setText(oversInt + "." + remainingBalls);

        // Print the remaining balls for debugging purposes
        System.out.println(remainingBalls);

        // Update the "OVER" column in table2 for the selected bowler
        String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
        String lastBowler = null;
        if (selectedBowler != null && !selectedBowler.isEmpty()) {
            // Check if the over is complete and ensure the bowler is not the same as the last bowler
            if (remainingBalls == 0 && selectedBowler.equals(lastBowler)) {
                JOptionPane.showMessageDialog(frame, "Bowler can't bowl consecutive overs!");
                return;
            }

            DefaultTableModel model2 = (DefaultTableModel) table2.getModel();
            for (int i = 0; i < model2.getRowCount(); i++) {
                if (model2.getValueAt(i, 0).equals(selectedBowler)) {
                    // Calculate the overs for the selected bowler
                    BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
                    int bowlerBalls = currentBowlerStats.balls;
                    double currentOvers;
                    if (bowlerBalls == 0) {
                        currentOvers = 0.0;
                    } else {
                        int bowlerOversInt = bowlerBalls / 6;
                        int bowlerRemainingBalls = bowlerBalls % 6;
                        currentOvers = bowlerOversInt + (bowlerRemainingBalls / 10.0); // Corrected to consider tenths of an over
                    }

                    // Update the table model with the current overs
                    model2.setValueAt(String.format("%.1f", currentOvers), i, 1); // Format to one decimal place
                    break;
                }
            }

            // Update the last bowler only if the over is complete
            if (remainingBalls == 0) {
                lastBowler = selectedBowler;
            }
        }
    }


    private JTable createTable(String[] columnNames, int rowHeight, int fontSize) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setRowHeight(rowHeight);
        table.setFont(new Font("Arial", Font.PLAIN, fontSize));
        return table;
    }

    // Method to populate the "Set Bowler" combo box with player names from the specified team and specialties "Spin bowler" or "Fast bowler"
    private void populateBowlerComboBox(JComboBox<String> comboBox, String teamName) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Ipl", "postgres", "dharsh");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT player_name, speciality FROM " + teamName);

            ArrayList<String> bowlers = new ArrayList<>();
            while (rs.next()) {
                String player = rs.getString("player_name");
                String speciality = rs.getString("speciality");
                if (speciality.contains("Spin bowler") || speciality.contains("Fast bowler")) {
                    // Extract the first specialty before ','
                    String[] specialties = speciality.split(",");
                    bowlers.add(player + " - " + specialties[0]);
                }
            }

            comboBox.setModel(new DefaultComboBoxModel<>(bowlers.toArray(new String[0])));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double calculateStrikeRate(int runs, int balls) {
        return (balls == 0) ? 0.0 : (double) runs * 100 / balls;
    }

    private int getBatsmanRow(DefaultTableModel model, String batsman) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(batsman)) {
                return row;
            }
        }
        return -1;
    }

    private void updateBowlerStats(String bowler, BowlerStats stats) {
        int bowlerRow = getBowlerRow((DefaultTableModel) table2.getModel(), bowler);
        DefaultTableModel model2 = (DefaultTableModel) table2.getModel();
        if (bowlerRow == -1) {
            model2.addRow(new Object[]{bowler, 0, 0, 0, 0, 0, 0.0});
            bowlerRow = model2.getRowCount() - 1;
        }
        model2.setValueAt(stats.overs, bowlerRow, 1);
        model2.setValueAt(stats.maidens, bowlerRow, 2);
        model2.setValueAt(stats.runs, bowlerRow, 3);
        model2.setValueAt(stats.wickets, bowlerRow, 4);
        model2.setValueAt(stats.wideBalls, bowlerRow, 5);
        //model2.setValueAt(stats.economyRate(), bowlerRow, 6);
        double economyRate = calculateEconomyRate(stats.balls, stats.runs);
        model2.setValueAt(String.format("%.2f", economyRate), bowlerRow, 6);
    }

    private int getBowlerRow(DefaultTableModel model, String bowler) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(bowler)) {
                return row;
            }
        }
        return -1;
    }

    private void incrementBallCount() {
        balls++;

        String selectedBowler = (String) setBowlerComboBox.getSelectedItem();
        if (selectedBowler != null && !selectedBowler.isEmpty()) {
            BowlerStats currentBowlerStats = bowlerStatsMap.get(selectedBowler);
            if (currentBowlerStats != null) {
                currentBowlerStats.balls++;
                updateBowlerStats(selectedBowler, currentBowlerStats); // Update the bowler's stats including overs
            }
        }
        updateOver();
    }

    private void switchStrikers() {
        String temp = selectedStriker;
        selectedStriker = selectedNonStriker;
        selectedNonStriker = temp;
    }

    public void setVisible(boolean b) {
    }

    class BowlerStats {
        public int balls;
        int overs;
        int maidens;
        int runs;
        int wickets;
        int wideBalls;

        /*double economyRate() {
            return (overs == 0) ? 0.0 : (double) runs / (balls/6.0);
        }*/
    }
    private double calculateEconomyRate(int balls, int runs) {
        return (balls == 0) ? 0.0 : (double) runs * 6 / balls;
    }


    public static void main(String[] args) {
        new ScoreBoardSplitPaneControl("Striker", "Non-Striker", "Bowling Team", "Batting Team", TossWinnerWindow.tossWinner, TossWinnerWindow.chosenOption, OverOptionSwing.overLimit, PlayerListTeam1Control.venue, PlayerListTeam1Control.matchName);
    }
}