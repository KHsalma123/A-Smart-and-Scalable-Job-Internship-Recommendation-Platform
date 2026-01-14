package application;

import javax.swing.*;
import java.awt.*;
import com.example.jobs.maven_exemple.Database;

public class ForgotPasswordCodeFrame extends JFrame {
    private JTextField txtCode;
    private JButton btnValidate, btnLogin;
    private String email;

    public ForgotPasswordCodeFrame(String email) {
        this.email = email;
        setTitle("Entrer le code");
        setSize(400, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("Entrer le code reçu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBounds(80, 20, 300, 30);
        add(lblTitle);

        txtCode = new JTextField();
        txtCode.setBounds(50, 70, 300, 40);
        txtCode.setBorder(BorderFactory.createTitledBorder("Code"));
        add(txtCode);

        btnValidate = new JButton("Valider");
        btnValidate.setBounds(50, 120, 300, 40);
        btnValidate.setBackground(new Color(50, 200, 50));
        btnValidate.setForeground(Color.WHITE);
        btnValidate.setFocusPainted(false);
        add(btnValidate);

        btnLogin = new JButton("Retour à la connexion");
        btnLogin.setBounds(50, 170, 300, 30);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setForeground(Color.BLUE);
        add(btnLogin);

        btnValidate.addActionListener(e -> {
            String code = txtCode.getText();
            if (Database.verifyResetCode(email, code)) {
                dispose();
                new ForgotPasswordNewPasswordFrame(email).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Code incorrect !");
            }
        });

        btnLogin.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}
