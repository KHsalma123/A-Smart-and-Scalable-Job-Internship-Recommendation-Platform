package application;

import javax.swing.*;
import java.awt.*;
import com.example.jobs.maven_exemple.Database;

public class ForgotPasswordNewPasswordFrame extends JFrame {
    private JPasswordField txtNewPassword;
    private JButton btnSubmit, btnLogin;
    private String email;

    public ForgotPasswordNewPasswordFrame(String email) {
        this.email = email;
        setTitle("Nouveau mot de passe");
        setSize(400, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("Entrer un nouveau mot de passe");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(50, 20, 300, 30);
        add(lblTitle);

        txtNewPassword = new JPasswordField();
        txtNewPassword.setBounds(50, 70, 300, 40);
        txtNewPassword.setBorder(BorderFactory.createTitledBorder("Nouveau mot de passe"));
        add(txtNewPassword);

        btnSubmit = new JButton("Modifier");
        btnSubmit.setBounds(50, 120, 300, 40);
        btnSubmit.setBackground(new Color(50, 200, 50));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        add(btnSubmit);

        btnLogin = new JButton("Retour à la connexion");
        btnLogin.setBounds(50, 170, 300, 30);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setForeground(Color.BLUE);
        add(btnLogin);

        btnSubmit.addActionListener(e -> {
            String newPassword = String.valueOf(txtNewPassword.getPassword());
            Database.updatePassword(email, newPassword);
            JOptionPane.showMessageDialog(this, "Mot de passe modifié avec succès !");
            dispose();
            new LoginFrame().setVisible(true);
        });

        btnLogin.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}
