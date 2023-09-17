package com.mycompany.tnlotr1;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;



public class IntroPage extends JFrame{
    private Image backgroundImage;
    JFrame frame;
    JButton buttonLogin = new JButton("Login");
    JButton buttonRegister = new JButton("Register");
    public final int width = 1920;
    public final int height = 1080;
    
    JPanel contentPane = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    };
    
    JPanel loginPanel = new JPanel();
    JPanel registerPanel = new JPanel();
    JPanel selectLanguage = new JPanel();

    public IntroPage() throws LineUnavailableException, UnsupportedAudioFileException, IOException, SQLException {
        File soundFile = new File("C:\\Users\\Rares\\Documents\\NetBeansProjects\\TNLOTR1\\IntroSong.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
        
        frame = new JFrame("TNLOTR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField usernameLogin = createPlaceholderTextField("Username");
        usernameLogin.setPreferredSize(new Dimension(160, 30));
        JPasswordField passwordLogin = createPlaceholderPasswordField("Password");
        passwordLogin.setPreferredSize(new Dimension(160, 30));
        JTextField usernameRegister = createPlaceholderTextField("Username");
        usernameRegister.setPreferredSize(new Dimension(160, 30));
        JPasswordField registerPassword = createPlaceholderPasswordField("123456789");
        registerPassword.setPreferredSize(new Dimension(160, 30));
        JPasswordField registerPasswordConf = createPlaceholderPasswordField("123456789");
        registerPasswordConf.setPreferredSize(new Dimension(160, 30));
        JTextField emailRegister = createPlaceholderTextField("example@address.com");
        emailRegister.setPreferredSize(new Dimension(160, 30));
        JTextField refferalID = createPlaceholderTextField("Referral ID");
        refferalID.setPreferredSize(new Dimension(160, 30));
        JCheckBox checkBox = new JCheckBox("I agree to the terms and conditions");
        JCheckBox checkBox2 = new JCheckBox("I want to be remembered");
        backgroundImage = new ImageIcon("C:\\Users\\Rares\\Documents\\NetBeansProjects\\TNLOTR1\\background.jpg").getImage();

        usernameLogin.setHorizontalAlignment(JTextField.CENTER);
        passwordLogin.setHorizontalAlignment(JTextField.CENTER);
        usernameRegister.setHorizontalAlignment(JTextField.CENTER);
        registerPassword.setHorizontalAlignment(JTextField.CENTER);
        registerPasswordConf.setHorizontalAlignment(JTextField.CENTER);
        emailRegister.setHorizontalAlignment(JTextField.CENTER);
        refferalID.setHorizontalAlignment(JTextField.CENTER);
        loginPanel.setLayout(new GridBagLayout());

                buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameRegister.getText();
                String password = String.valueOf(registerPassword.getPassword());
                String email = emailRegister.getText();
                String referralID = refferalID.getText();

                try {
                    sendDataToServer(username, password, email, referralID);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 5, 5);

        gbc.gridy = 0;
        loginPanel.add(usernameLogin, gbc);

        gbc.gridy = 1;
        loginPanel.add(passwordLogin, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        
        loginPanel.add(checkBox2, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(buttonLogin, gbc);

        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setPreferredSize(new Dimension(200, 100));
        gbc2.gridy = 0;
        registerPanel.add(usernameRegister, gbc2);
        gbc2.gridy = 1;
        registerPanel.add(registerPassword, gbc2);
        gbc2.gridy = 2;
        registerPanel.add(registerPasswordConf, gbc2);
        gbc2.gridy = 3;
        registerPanel.add(emailRegister, gbc2);
        gbc2.gridy = 4;
        registerPanel.add(refferalID, gbc2);
        gbc2.gridy = 5;
        registerPanel.add(checkBox, gbc2);
        gbc2.gridy = 6;
        registerPanel.add(buttonRegister, gbc2);
        
        contentPane.setLayout(null);

        loginPanel.setBounds(1350, 800, 300, 150);
        registerPanel.setBounds(400, 500, 270, 300);
   
        contentPane.add(loginPanel);
        contentPane.add(registerPanel);

        frame.setContentPane(contentPane);
        frame.setSize(width, height);
        buttonLogin.setVisible(true);
        frame.setResizable(false);
        frame.setVisible(true);

    }

    public static JTextField createPlaceholderTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText(""); 
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder); 
                }
            }
        });

        return textField;
    }
    
    public static JPasswordField createPlaceholderPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(placeholder);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText(""); 
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder); 
                }
            }
        });

        return passwordField;
    }
    
    private void sendDataToServer(String username, String password, String email, String refferalID) throws SQLException {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/alpha", "root", "");

            String insertQuery = "INSERT INTO accounts (account_name, password, mail, reffered_by) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, refferalID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Error during registration. Please try again later.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
    
    
    
}
    

