package com.perisic.tomato.peripherals;



import java.awt.event.ActionEvent;
import java.awt.Color;

import java.awt.event.ActionListener;

import java.util.Timer;

// import java.util.TimerTask;



import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JPasswordField;

import javax.swing.JTextField;



public class LoginSignupGUI extends JFrame {

    /**

     * 

     */

    private static final long serialVersionUID = 1L;

    private JTextField txuser = new JTextField(15);

    private JPasswordField pass = new JPasswordField(15);

    private JButton blogin = new JButton("Login (User ID)");

    private JButton bsignup = new JButton("Signup (Password)");

    private JLabel timerLabel = new JLabel("Time Remaining: 2:00"); // Initializing value, adjusting as needed



    private LoginData loginData = new LoginData(); // Creating an instance of LoginData

    private Timer sessionTimer = new Timer(true);



    public Timer getSessionTimer() {

        return sessionTimer;

    }



    public void setSessionTimer(Timer sessionTimer) {

        this.sessionTimer = sessionTimer;

    }



    public LoginSignupGUI() {

        setTitle("Login/Signup");

        setSize(300, 200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        JPanel panel = new JPanel();
        panel.setBackground(new Color(100, 149, 237));
        panel.add(new JLabel("Username:"));

        panel.add(txuser);

        panel.add(new JLabel("Password:"));

        panel.add(pass);

        panel.add(bsignup);

        panel.add(blogin);

        panel.add(timerLabel);



        add(panel);

        setLocationRelativeTo(null);

        setVisible(true);



        blogin.addActionListener(new LoginActionListener());

        bsignup.addActionListener(new SignupActionListener());

    }



    private class LoginActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            String username = txuser.getText();

            String password = String.valueOf(pass.getPassword());



            if (loginData.checkPassword(username, password)) {

                String sessionToken = loginData.startSession(username);

                updateTimerLabel();

                openGameGUI(sessionToken);

            } else {

                JOptionPane.showMessageDialog(null, "Wrong Password / Username");

            }

        }

    }



    private class SignupActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            String newUsername = txuser.getText();

            String newPassword = String.valueOf(pass.getPassword());



            // Checking if the username already exists

            if (loginData.userExists(newUsername)) {

                JOptionPane.showMessageDialog(null, "Username already exists. Choose a different one.");

            } else {

                // Register the user (add to userDatabase)

                loginData.registerUser(newUsername, newPassword);



                JOptionPane.showMessageDialog(null, "User registered successfully.");

                openGameGUI(newUsername);

            }

        }

    }



    private void openGameGUI(String username) {

        dispose(); // To close the login/signup page



        String welcomeMessage;



        // Checking if the user is an existing user

        if (loginData.userExists(username)) {

            // Fetching user data from the database

            UserData userData = loginData.getUserData(username);



            welcomeMessage = "Welcome back, " + userData.getFullName() + "!";

        } else {

            welcomeMessage = "Welcome, " + username + "!";

        }



        // Creating a new GameGUI instance

        GameGUI gameGUI = new GameGUI(username);



        // To display the welcome prompt

        JOptionPane.showMessageDialog(gameGUI, welcomeMessage);



        // Making the GameGUI frame visible

        gameGUI.setVisible(true);

    }





    public void updateTimerLabel() {



    }



    public static void main(String[] args) {

        new LoginSignupGUI();

    }

}

