import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PlayerListTeam2Control extends JFrame{
    private JTextField tfTeamName;
    private JTextField tfPlayerName1;
    private JTextField tfPlayerName2;
    private JTextField tfPlayerName3;
    private JTextField tfPlayerName4;
    private JTextField tfPlayerName5;
    private JTextField tfPlayerName6;
    private JTextField tfPlayerName7;
    private JTextField tfPlayerName8;
    private JTextField tfPlayerName9;
    private JTextField tfPlayerName10;
    private JTextField tfPlayerName11;

    private JButton btSubmit;
    private JButton btHomePage;


    public PlayerListTeam2Control() {
        setTitle("Player List - Team 2");
        setSize(750, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Using absolute layout

        initComponents();
        addComponents();
    }


    private void initComponents() {
        JLabel teamLabel = new JLabel("Team-2 Name:");
        teamLabel.setBounds(142, 42, 170, 44);
        teamLabel.setForeground(Color.WHITE);
        teamLabel.setFont(new Font("Candara Italic", Font.BOLD, 28));
        add(teamLabel);

        tfTeamName = new JTextField();
        tfTeamName.setBounds(331, 38, 300, 52);
        tfTeamName.setFont(new Font("Candara Italic", Font.PLAIN, 25));
        add(tfTeamName);

        tfPlayerName1 = createTextField(109);
        tfPlayerName2 = createTextField(169);
        tfPlayerName3 = createTextField(229);
        tfPlayerName4 = createTextField(289);
        tfPlayerName5 = createTextField(349);
        tfPlayerName6 = createTextField(409);
        tfPlayerName7 = createTextField(469);
        tfPlayerName8 = createTextField(529);
        tfPlayerName9 = createTextField(589);
        tfPlayerName10 = createTextField(649);
        tfPlayerName11 = createTextField(709);

        btSubmit = new JButton("Submit");
        btSubmit.setBounds(539, 798, 117, 50);
        btSubmit.setFont(new Font("Candara Bold Italic", Font.BOLD, 25));
        btSubmit.setBackground(Color.WHITE);
        btSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    submit(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        add(btSubmit);

        btHomePage = new JButton("HomePage");
        btHomePage.setBounds(179, 798, 150, 50);
        btHomePage.setFont(new Font("Candara Bold Italic", Font.BOLD, 25));
        btHomePage.setBackground(Color.WHITE);
        btHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    homepage(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        add(btHomePage);
    }

    private JTextField createTextField(int y) {
        JTextField textField = new JTextField();
        textField.setBounds(281, y, 300, 50);
        textField.setFont(new Font("Candara Italic", Font.PLAIN, 24));
        add(textField);
        return textField;
    }

    private void addComponents() {
        add(tfPlayerName1);
        add(tfPlayerName2);
        add(tfPlayerName3);
        add(tfPlayerName4);
        add(tfPlayerName5);
        add(tfPlayerName6);
        add(tfPlayerName7);
        add(tfPlayerName8);
        add(tfPlayerName9);
        add(tfPlayerName10);
        add(tfPlayerName11);
    }

    public void submit(ActionEvent event) throws IOException {
        if (tfTeamName.getText().isBlank() ||
                tfPlayerName1.getText().isBlank() ||
                tfPlayerName2.getText().isBlank() ||
                tfPlayerName3.getText().isBlank() ||
                tfPlayerName4.getText().isBlank() ||
                tfPlayerName5.getText().isBlank() ||
                tfPlayerName6.getText().isBlank() ||
                tfPlayerName7.getText().isBlank() ||
                tfPlayerName8.getText().isBlank() ||
                tfPlayerName9.getText().isBlank() ||
                tfPlayerName10.getText().isBlank()||
                tfPlayerName11.getText().isBlank()
        ) {
            JOptionPane.showMessageDialog(this, "Please input all names", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (isSameName()) {
            JOptionPane.showMessageDialog(this, "Please use Different names", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure?", "Checking", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Your logic for saving data and moving to the next screen goes here
                dispose();
                new PlayerListTeam2Control().setVisible(true);
            }
        }
    }

    public void homepage(ActionEvent event) throws IOException {
        // Your logic for going back to the homepage goes here
    }

    public boolean isSameName() {
        ArrayList<String> playerNameList = new ArrayList<>();
        playerNameList.add(tfPlayerName1.getText());
        playerNameList.add(tfPlayerName2.getText());
        playerNameList.add(tfPlayerName3.getText());
        playerNameList.add(tfPlayerName4.getText());
        playerNameList.add(tfPlayerName5.getText());
        playerNameList.add(tfPlayerName6.getText());
        playerNameList.add(tfPlayerName7.getText());
        playerNameList.add(tfPlayerName8.getText());
        playerNameList.add(tfPlayerName9.getText());
        playerNameList.add(tfPlayerName10.getText());
        playerNameList.add(tfPlayerName11.getText());

        for (String playerName : playerNameList) {
            if (Collections.frequency(playerNameList, playerName) > 1) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PlayerListTeam2Control().setVisible(true);
            }
        });
    }
}
