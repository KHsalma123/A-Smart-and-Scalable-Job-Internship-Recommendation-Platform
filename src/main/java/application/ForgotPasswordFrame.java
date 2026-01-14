package application;

import javax.swing.*;
import java.awt.*;
import com.example.jobs.maven_exemple.Database;

public class ForgotPasswordFrame extends JFrame {
    private JTextField txtEmail;
    private JButton btnSendCode, btnLogin;

    public ForgotPasswordFrame() {
        setTitle("Mot de passe oublié");
        setSize(400, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("Mot de passe oublié");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBounds(80, 20, 300, 30);
        add(lblTitle);

        txtEmail = new JTextField();
        txtEmail.setBounds(50, 70, 300, 40);
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        add(txtEmail);

        btnSendCode = new JButton("Envoyer le code");
        btnSendCode.setBounds(50, 120, 300, 40);
        btnSendCode.setBackground(new Color(0, 120, 215));
        btnSendCode.setForeground(Color.WHITE);
        btnSendCode.setFocusPainted(false);
        add(btnSendCode);

        btnLogin = new JButton("Retour à la connexion");
        btnLogin.setBounds(50, 170, 300, 30);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setForeground(Color.BLUE);
        add(btnLogin);

        btnSendCode.addActionListener(e -> {
            String email = txtEmail.getText();
            if (!Database.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "Email introuvable !");
                return;
            }
            Database.generateResetCode(email);
            JOptionPane.showMessageDialog(this, "Code envoyé à votre email (simulation)");
            dispose();
            new ForgotPasswordCodeFrame(email).setVisible(true);
        });

        btnLogin.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}
