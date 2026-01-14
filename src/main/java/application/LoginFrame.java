package application;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.example.jobs.maven_exemple.Database;
import security.AdminAuthService;
import application.admin.AdminDashboardFrame;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister, btnForgot;

    public LoginFrame() {
        setTitle("Connexion");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Bienvenue");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBounds(120, 30, 200, 40);
        add(lblTitle);

        txtEmail = new JTextField();
        txtEmail.setBounds(50, 100, 300, 40);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        add(txtEmail);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 160, 300, 40);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createTitledBorder("Mot de passe"));
        add(txtPassword);

        btnLogin = new JButton("Connexion");
        btnLogin.setBounds(50, 220, 300, 45);
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        add(btnLogin);

        btnForgot = new JButton("Mot de passe oublié ?");
        btnForgot.setBounds(50, 280, 300, 30);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setBorderPainted(false);
        btnForgot.setForeground(Color.BLUE);
        add(btnForgot);

        btnRegister = new JButton("S’inscrire");
        btnRegister.setBounds(50, 320, 300, 45);
        btnRegister.setBackground(new Color(50, 200, 50));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        add(btnRegister);

        // Login action
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs");
                return;
            }

            try {
                Connection conn = Database.getConnection();
                AdminAuthService authService = new AdminAuthService(conn);

                String role = authService.login(email, password);

                switch (role) {
                    case "admin":
                        JOptionPane.showMessageDialog(this, "Bienvenue Admin");
                        SwingUtilities.invokeLater(() -> new AdminDashboardFrame().setVisible(true));
                        dispose();
                        break;
                    case "user":
                        JOptionPane.showMessageDialog(this, "Bienvenue " + Database.getUserName(email));
                        SwingUtilities.invokeLater(() -> new HomeFrame(email).setVisible(true));
                        dispose();
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Adresse e-mail ou mot de passe invalide");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données!");
            }
        });

        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });

        btnForgot.addActionListener(e -> {
            dispose();
            new ForgotPasswordFrame().setVisible(true);
        });

        setVisible(true);
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
