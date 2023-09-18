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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;



public class IntroPage extends JFrame{
    private Image backgroundImage;
    public JFrame frame;
    public JButton buttonLogin = new JButton("Login");
    public JButton buttonRegister = new JButton("Register");
    public final int width = 1920;
    public final int height = 1080;
    JCheckBox checkBox = new JCheckBox("I agree to the terms and conditions");
    JCheckBox checkBox2 = new JCheckBox("I want to be remembered", true);
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
    JTextField usernameLogin = createPlaceholderTextField("Username");
    JPasswordField passwordLogin = createPlaceholderPasswordField("Password");
    JTextField usernameRegister = createPlaceholderTextField("Username");
    JPasswordField registerPassword = createPlaceholderPasswordField("123456789");
    JPasswordField registerPasswordConf = createPlaceholderPasswordField("123456789");
    JTextField emailRegister = createPlaceholderTextField("example@address.com");
    JTextField refferalID = createPlaceholderTextField("Referral ID");
        
    public IntroPage() throws LineUnavailableException, UnsupportedAudioFileException, IOException, SQLException {
        loadCredentialsFromFile();
        File soundFile = new File("C:\\Users\\Rares\\Documents\\NetBeansProjects\\TNLOTR1\\IntroSong.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
        
        frame = new JFrame("TNLOTR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passwordLogin.setPreferredSize(new Dimension(160, 30));
        usernameLogin.setPreferredSize(new Dimension(160, 30));
        usernameRegister.setPreferredSize(new Dimension(160, 30));
        registerPassword.setPreferredSize(new Dimension(160, 30));
        registerPasswordConf.setPreferredSize(new Dimension(160, 30));
        emailRegister.setPreferredSize(new Dimension(160, 30));
        refferalID.setPreferredSize(new Dimension(160, 30));

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
                String confirmPassword = String.valueOf(registerPasswordConf.getPassword());
                String email = emailRegister.getText();
                String referralID = refferalID.getText();

                try {
                    sendDataToServer(username, password, confirmPassword, email, referralID);
                } catch (SQLException ex) {
                } catch (UnknownHostException ex) {
                    Logger.getLogger(IntroPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
);
        
buttonLogin.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameLogin.getText();
        String password = String.valueOf(passwordLogin.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter both username and password.");
            return;
        }

        try {
            if (verifyLogin(username, password)) {
                if (checkBox2.isSelected()) {
                    saveCredentialsToFile(username, password);
                }
                
                JOptionPane.showMessageDialog(null, "Login performed succesfully. Entering the game in a second.");
                contentPane.remove(loginPanel);
                contentPane.remove(registerPanel);
                
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(IntroPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IntroPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
});
checkBox2.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkBox2.isSelected()) {
            try (PrintWriter writer = new PrintWriter("saved_account")) {
                writer.println(usernameLogin.getText());
                writer.println(passwordLogin.getText());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            File savedAccountFile = new File("saved_account");
            if (savedAccountFile.exists()) {
                savedAccountFile.delete();
                usernameLogin.setText("");
                passwordLogin.setText("");
            }
        }
    }
});

File savedAccountFile = new File("saved_account");
if (savedAccountFile.exists() && checkBox2.isSelected()) {
    try (Scanner scanner = new Scanner(savedAccountFile)) {
        String savedUsername = scanner.nextLine();
        String savedPassword = scanner.nextLine();
        usernameLogin.setText(savedUsername);
        passwordLogin.setText(savedPassword);
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    }
}



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
    
    private void sendDataToServer(String username, String password, String confirmPassword, String email, String referralID) throws SQLException, UnknownHostException {
        Connection con = null;
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            String hostAddress = ipAddress.getHostAddress();
            con = DriverManager.getConnection("jdbc:mysql://localhost/alpha", "root", "");
            if (username.length() < 6 || username.length() > 20) 
            {
                JOptionPane.showMessageDialog(null, "Username must be between 6 and 20 characters.");
                return;
            }

            String checkQuery = "SELECT COUNT(*) FROM accounts WHERE account_name = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) 
            {
                JOptionPane.showMessageDialog(null, "Username is already taken. Please choose a different one.");
                return;
            }

        
            String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
            if (!password.matches(passwordRegex)) 
            {
                JOptionPane.showMessageDialog(null, "Password must have at least:\n - one uppercase letter\n - one number\n - one special character\n - be at least 8 characters long");
                return;
            }
        
            if (!password.equals(confirmPassword)) 
            {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return;
            }
        
            String emailRegex = "^(.+)@(yahoo\\.com|gmail\\.com)$";
            if (!email.matches(emailRegex)) 
            {
                JOptionPane.showMessageDialog(null, "Please enter a valid Yahoo or Gmail address.\n Any other type of address is not accepted.");
                return;
            }

            String checkQuery2 = "SELECT COUNT(*) FROM accounts WHERE mail = ?";
            PreparedStatement checkStatement2 = con.prepareStatement(checkQuery2);
            checkStatement2.setString(1, email);
            ResultSet resultSet2 = checkStatement2.executeQuery();

            if (resultSet2.next() && resultSet2.getInt(1) > 0) 
            {
                JOptionPane.showMessageDialog(null, "Email address is already in use. Please use a different one.");
                return;
            }

            if (!referralID.isEmpty()) {
                String checkReferralQuery = "SELECT COUNT(*) FROM accounts WHERE account_name = ?";
                PreparedStatement checkReferralStatement = con.prepareStatement(checkReferralQuery);
                checkReferralStatement.setString(1, referralID);
                ResultSet referralResultSet = checkReferralStatement.executeQuery();

                if (!referralResultSet.next() || referralResultSet.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(null, "Referral ID does not exist. Please enter a valid one.");
                    return;
                }
            } else {
                referralID = "";
            }

             if (!checkBox.isSelected()) {
                JOptionPane.showMessageDialog(null, "You must agree to the terms and conditions.");
                return;
            }
        
            String vcode = generateRandomVerificationCode();
            String insertReferralQuery = "INSERT INTO refferal_manager (refferal_id, reffered_player) VALUES (?, ?)";
            PreparedStatement referralPreparedStatement = con.prepareStatement(insertReferralQuery);
            referralPreparedStatement.setString(1, referralID);
            referralPreparedStatement.setString(2, username);
            int rowsAffected1 = referralPreparedStatement.executeUpdate();
            String insertQuery = "INSERT INTO accounts (account_name, password, mail, ip_address, vcode) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, hostAddress);
            preparedStatement.setString(5, vcode);
            
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                sendEmail(email, vcode);
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
    
    private String generateRandomVerificationCode() 
    {
    return String.format("%06d", new Random().nextInt(1000000));
    }
    
private void sendEmail(String recipientEmail, String verificationCode) {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");  
    props.put("mail.smtp.port", "587");   
    props.put("mail.smtp.auth", "true");    
    props.put("mail.smtp.starttls.enable", "true"); 

    javax.mail.Session mailSession = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
        @Override
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication("zzzzzzzzzzzm", "zzzzzzzzzzw");
        }
    });

    try {
        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("zzzzzzzzl.com")); 
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Account Verification");
        message.setText("""
                        Hello :),
                        
                        Your account on Totally Not Lord of The Rings has been registered successfully.
                        Do not delete this e-mail, you will be prompted to verify your account first time when you will log in.
                        
                        Your verification code is: """ + verificationCode);

        Transport.send(message);

        System.out.println("Email sent successfully");

    } catch (MessagingException e) {
    }
}

private boolean verifyLogin(String username, String password) throws SQLException {
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost/alpha", "root", "");

        String query = "SELECT COUNT(*) FROM accounts WHERE account_name = ? AND password = ?";
        preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next() && resultSet.getInt(1) > 0) {
            return true; 
        } else {
            return false; 
        }
    } finally {
        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (con != null) con.close();
    }
}

private void saveCredentialsToFile(String username, String password) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("saved_account.txt"));
    writer.write(username + "," + password);
    writer.close();
}

private void loadCredentialsFromFile() {
    try {
        File file = new File("saved_account.txt");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] credentials = reader.readLine().split(",");
            usernameLogin.setText(credentials[0]);
            passwordLogin.setText(credentials[1]);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
    

