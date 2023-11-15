/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package TasktifierGUI;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.prefs.Preferences;

/**
 *
 * @author Daniel
 */
public class Login extends javax.swing.JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tasktifier_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static boolean isValidEmail = false;
    private static boolean isValidPassword = false;
    private Preferences preferences;
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
    
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        
        this.setLocationRelativeTo(null);
        
        System.out.println(PREF_EMAIL);
        System.out.println(PREF_PASSWORD);
        
        setDefaultBorderToTextField(EmailTextField);
	setDefaultBorderToPasswordField(PasswordField);

	addFocusListenerToTextField(EmailTextField);
	addFocusListenerToPasswordField(PasswordField);
        
        FrameContainer.requestFocusInWindow();
        
        FrameContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FrameContainer.requestFocusInWindow();
            }
        });
        
        LoginButton.addActionListener((ActionEvent e) -> {
            handleLogin();
        });
        
        preferences = Preferences.userNodeForPackage(Login.class);
        
        if(RememberMeCheckbox.isSelected()) {
            savePreferences(PREF_EMAIL, PREF_PASSWORD);
        }
        
        if(PREF_EMAIL.length() > 0 && PREF_PASSWORD.length() > 0){
            loadPreferences();
        }
        
        if(!RememberMeCheckbox.isSelected()) {
            preferences.put(PREF_EMAIL, "");
            preferences.put(PREF_PASSWORD, "");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FrameContainer = new javax.swing.JPanel();
        Login = new javax.swing.JPanel();
        Logo = new javax.swing.JLabel();
        LogoTitle = new javax.swing.JLabel();
        LogoTextContainer = new javax.swing.JScrollPane();
        LogoText = new javax.swing.JTextPane();
        EmailLabel = new javax.swing.JLabel();
        EmailTextField = new javax.swing.JTextField();
        PasswordLabel = new javax.swing.JLabel();
        PasswordField = new javax.swing.JPasswordField();
        LoginButton = new javax.swing.JButton();
        Separator = new javax.swing.JSeparator();
        ChangePasswordHyperlink = new javax.swing.JLabel();
        SignupLink = new javax.swing.JLabel();
        RememberMeCheckbox = new javax.swing.JCheckBox();
        EmailIconInvalid = new javax.swing.JLabel();
        EmailWarning = new javax.swing.JTextField();
        PasswordIconInvalid = new javax.swing.JLabel();
        PasswordWarning = new javax.swing.JTextField();
        LoginCover = new javax.swing.JLabel();
        LoginMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        FrameContainer.setBackground(new java.awt.Color(39, 43, 48));

        Login.setBackground(new java.awt.Color(39, 43, 48));

        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TasktifierGUI/assets/images/logotest.png"))); // NOI18N
        Logo.setText("jLabel1");

        LogoTitle.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        LogoTitle.setForeground(new java.awt.Color(236, 240, 243));
        LogoTitle.setText("Tasktifier");

        LogoTextContainer.setBorder(null);

        LogoText.setEditable(false);
        LogoText.setBackground(new java.awt.Color(38, 43, 48));
        LogoText.setBorder(null);
        LogoText.setForeground(new java.awt.Color(231, 235, 238));
        LogoText.setText("Automate the busywork, so you can focus on your job, not your tools. We'll show you how.");
        LogoText.setToolTipText("");
        LogoText.setAutoscrolls(false);
        LogoText.setFocusTraversalPolicyProvider(true);
        LogoText.setFocusable(false);
        LogoTextContainer.setViewportView(LogoText);
        StyledDocument doc = LogoText.getStyledDocument();

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        int verticalOffset = 20;
        SimpleAttributeSet verticalAlign = new SimpleAttributeSet();
        doc.setParagraphAttributes(0, doc.getLength(), verticalAlign, false);

        EmailLabel.setBackground(new java.awt.Color(38, 43, 48));
        EmailLabel.setForeground(new java.awt.Color(160, 160, 160));
        EmailLabel.setText("Email");

        EmailTextField.setBackground(new java.awt.Color(39, 43, 48));
        EmailTextField.setFont(new java.awt.Font("MS PGothic", 0, 18)); // NOI18N
        EmailTextField.setForeground(new java.awt.Color(255, 255, 255));
        EmailTextField.setToolTipText("");
        EmailTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 0, true));
        EmailTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        EmailTextField.setMargin(new java.awt.Insets(2, 6, 0, 6));
        EmailTextField.setSelectedTextColor(new java.awt.Color(0, 0, 0));

        PasswordLabel.setBackground(new java.awt.Color(38, 43, 48));
        PasswordLabel.setForeground(new java.awt.Color(160, 160, 160));
        PasswordLabel.setText("Password");

        PasswordField.setBackground(new java.awt.Color(39, 43, 48));
        PasswordField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        PasswordField.setForeground(new java.awt.Color(255, 255, 255));
        PasswordField.setBorder(null);
        PasswordField.setMargin(new java.awt.Insets(2, 6, 2, 0));
        PasswordField.setVerifyInputWhenFocusTarget(false);

        LoginButton.setForeground(new java.awt.Color(255, 255, 255));
        LoginButton.setText("Log in");

        ChangePasswordHyperlink.setBackground(new java.awt.Color(38, 43, 48));
        ChangePasswordHyperlink.setForeground(new java.awt.Color(160, 160, 160));
        ChangePasswordHyperlink.setText("Forgot Password?");

        SignupLink.setBackground(new java.awt.Color(38, 43, 48));
        SignupLink.setForeground(new java.awt.Color(160, 160, 160));
        SignupLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SignupLink.setText("Create an Account!");
        SignupLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        RememberMeCheckbox.setForeground(new java.awt.Color(142, 143, 144));
        RememberMeCheckbox.setText(" Remember Me");

        EmailIconInvalid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TasktifierGUI/assets/images/invalid.png"))); // NOI18N

        EmailWarning.setBackground(new java.awt.Color(39, 43, 48));
        EmailWarning.setForeground(new java.awt.Color(255, 65, 95));
        EmailWarning.setBorder(null);
        EmailWarning.setFocusable(false);

        PasswordIconInvalid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TasktifierGUI/assets/images/invalid.png"))); // NOI18N

        PasswordWarning.setBackground(new java.awt.Color(39, 43, 48));
        PasswordWarning.setForeground(new java.awt.Color(255, 65, 95));
        PasswordWarning.setBorder(null);
        PasswordWarning.setFocusable(false);

        javax.swing.GroupLayout LoginLayout = new javax.swing.GroupLayout(Login);
        Login.setLayout(LoginLayout);
        LoginLayout.setHorizontalGroup(
            LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginLayout.createSequentialGroup()
                        .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Separator)
                            .addComponent(LoginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(54, 54, 54))
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addComponent(PasswordIconInvalid, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PasswordWarning))
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(EmailLabel)
                            .addComponent(PasswordLabel)
                            .addComponent(EmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LoginLayout.createSequentialGroup()
                                .addComponent(EmailIconInvalid, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EmailWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PasswordField))
                        .addContainerGap(54, Short.MAX_VALUE))))
            .addGroup(LoginLayout.createSequentialGroup()
                .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LogoTextContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LoginLayout.createSequentialGroup()
                                .addComponent(Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(LogoTitle))))
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(ChangePasswordHyperlink)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(LoginLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(RememberMeCheckbox)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(SignupLink, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(143, 143, 143))
        );
        LoginLayout.setVerticalGroup(
            LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginLayout.createSequentialGroup()
                .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(LogoTitle)))
                .addGap(18, 18, 18)
                .addComponent(LogoTextContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(EmailLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EmailWarning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EmailIconInvalid, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(PasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PasswordWarning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PasswordIconInvalid, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(RememberMeCheckbox)
                .addGap(31, 31, 31)
                .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChangePasswordHyperlink, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SignupLink)
                .addGap(129, 129, 129))
        );

        SignupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signupLinkMouseClicked();
            }
        });
        EmailIconInvalid.setVisible(false);
        PasswordIconInvalid.setVisible(false);

        LoginCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TasktifierGUI/assets/images/login.png"))); // NOI18N

        LoginMessage.setForeground(new java.awt.Color(255, 65, 95));
        LoginMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TasktifierGUI/assets/images/invalid-25.png"))); // NOI18N
        LoginMessage.setText("User doesn't exist or wrong password.");
        LoginMessage.setMaximumSize(new java.awt.Dimension(38, 25));
        LoginMessage.setMinimumSize(new java.awt.Dimension(38, 25));
        LoginMessage.setPreferredSize(new java.awt.Dimension(38, 25));

        javax.swing.GroupLayout FrameContainerLayout = new javax.swing.GroupLayout(FrameContainer);
        FrameContainer.setLayout(FrameContainerLayout);
        FrameContainerLayout.setHorizontalGroup(
            FrameContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FrameContainerLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(FrameContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FrameContainerLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(LoginCover, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FrameContainerLayout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(LoginMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        FrameContainerLayout.setVerticalGroup(
            FrameContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FrameContainerLayout.createSequentialGroup()
                .addGroup(FrameContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FrameContainerLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FrameContainerLayout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(LoginCover, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LoginMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        LoginMessage.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FrameContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FrameContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void addFocusListenerToPasswordField(JPasswordField passwordField) {
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.YELLOW));
            }

            @Override
            public void focusLost(FocusEvent e) {
                char[] passwordCharacters = passwordField.getPassword();

                if (passwordCharacters.length >= 8) {
                    passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GREEN));
                    PasswordWarning.setText("");
                    PasswordIconInvalid.setVisible(false);
                    isValidPassword = true;
                } else if(passwordCharacters.length == 0){
                    passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
                    PasswordWarning.setText("");
                    PasswordIconInvalid.setVisible(false);
                    isValidPassword = false;
                } else {
                    passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED));
                    PasswordWarning.setText("Password must be at least 8 characters.");
                    PasswordIconInvalid.setVisible(true);
                    isValidPassword = false;
                } 
            }
        });
    }
    
    private void addFocusListenerToTextField(JTextField textField) {
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.YELLOW));
                EmailIconInvalid.setVisible(false);
                EmailWarning.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = EmailTextField.getText();
                if(text.isEmpty()){
                    textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
                    EmailIconInvalid.setVisible(false);
                    EmailWarning.setText("");
                    isValidEmail = false;
                } else {
                    if(text.indexOf('@') < text.indexOf('.') &&
                       text.indexOf('@') + 1 < text.indexOf('.') &&
                       text.matches(".*[a-zA-Z0-9]@.*[a-zA-Z0-9]\\..*[a-zA-Z0-9]")){
                        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GREEN));
                        EmailIconInvalid.setVisible(false);
                        EmailWarning.setText("");
                        isValidEmail = true;
                    } else {
                        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED));
                        EmailIconInvalid.setVisible(true);
                        EmailWarning.setText("Invalid email");
                        isValidEmail = false;
                    }
                }
            }
        });
    }

    private void setDefaultBorderToTextField(JTextField textField) {
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
    }
    
    private void setDefaultBorderToPasswordField(JPasswordField passwordField) {
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
    }
    
    private boolean isValidLogin(String email, String password) {
        try {
            Connection connection = getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?";
        
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
            
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            return false;
        }
    }
    
    private void handleLogin() {
        String email = EmailTextField.getText();
        char[] passwordChars = PasswordField.getPassword();
        String password = new String(passwordChars);

        if(isValidEmail && isValidPassword){
            if (isValidLogin(email, password)) {
                if (RememberMeCheckbox.isSelected()) {
                    savePreferences(email, password);
                }
                setVisible(false);
                dispose();
        
                Welcome welcome = new Welcome();
                SwingUtilities.invokeLater(() -> welcome.setVisible(true));
            } else {
                LoginMessage.setVisible(true);
            }
        }
    }
    
    private void loadPreferences() {
        String savedEmail = preferences.get(PREF_EMAIL, "");
        String savedPassword = preferences.get(PREF_PASSWORD, "");

        EmailTextField.setText(savedEmail);
        PasswordField.setText(savedPassword);
        
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            EmailTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GREEN));
            PasswordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GREEN));
            isValidEmail = true;
            isValidPassword = true;
        }
    }

    private void savePreferences(String email, String password) {
        preferences.put(PREF_EMAIL, email);
        preferences.put(PREF_PASSWORD, password);
    }
    
    private void signupLinkMouseClicked() {
        setVisible(false);
        dispose();

        Signup signup = new Signup();
        SwingUtilities.invokeLater(() -> {
            signup.setVisible(true);
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Not connected to the database! Make sure that MySQL server is open and `tasktifier_db` database exists.");
            System.exit(0);
        }
        
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e){}

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChangePasswordHyperlink;
    private javax.swing.JLabel EmailIconInvalid;
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JTextField EmailTextField;
    private javax.swing.JTextField EmailWarning;
    private javax.swing.JPanel FrameContainer;
    private javax.swing.JPanel Login;
    private javax.swing.JButton LoginButton;
    private javax.swing.JLabel LoginCover;
    private javax.swing.JLabel LoginMessage;
    private javax.swing.JLabel Logo;
    private javax.swing.JTextPane LogoText;
    private javax.swing.JScrollPane LogoTextContainer;
    private javax.swing.JLabel LogoTitle;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JLabel PasswordIconInvalid;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JTextField PasswordWarning;
    private javax.swing.JCheckBox RememberMeCheckbox;
    private javax.swing.JSeparator Separator;
    private javax.swing.JLabel SignupLink;
    // End of variables declaration//GEN-END:variables
}
