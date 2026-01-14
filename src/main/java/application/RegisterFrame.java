package application;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import com.example.jobs.maven_exemple.Database;

public class RegisterFrame extends JFrame {

    private JTextField txtFullName, txtEmail, txtCity, txtSkills;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnLogin, btnImportCV;
    private byte[] cvBytes = null; // Contenu du CV pour la BDD

    public RegisterFrame() {
        setTitle("Créer un compte");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Titre
        JLabel lblTitle = new JLabel("Créer un compte");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(100, 30, 250, 40);
        add(lblTitle);

        // Champs texte
        txtFullName = new JTextField();
        txtFullName.setBounds(50, 90, 300, 40);
        txtFullName.setBorder(BorderFactory.createTitledBorder("Nom complet"));
        add(txtFullName);

        txtEmail = new JTextField();
        txtEmail.setBounds(50, 150, 300, 40);
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        add(txtEmail);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 210, 300, 40);
        txtPassword.setBorder(BorderFactory.createTitledBorder("Mot de passe"));
        add(txtPassword);

        txtCity = new JTextField();
        txtCity.setBounds(50, 270, 300, 40);
        txtCity.setBorder(BorderFactory.createTitledBorder("Ville"));
        add(txtCity);

        txtSkills = new JTextField();
        txtSkills.setBounds(50, 330, 300, 40);
        txtSkills.setBorder(BorderFactory.createTitledBorder("Compétences"));
        add(txtSkills);

        // Bouton Import CV
        btnImportCV = new JButton("Importer CV (PDF ou Word)");
        btnImportCV.setBounds(50, 390, 300, 40);
        btnImportCV.setBackground(new Color(0, 150, 0));
        btnImportCV.setForeground(Color.WHITE);
        btnImportCV.setFocusPainted(false);
        add(btnImportCV);

        btnImportCV.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                String name = selectedFile.getName().toLowerCase();
                if(name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx")){
                    try {
                        cvBytes = Files.readAllBytes(selectedFile.toPath()); // lire le contenu du CV
                        JOptionPane.showMessageDialog(this, "CV chargé : " + selectedFile.getName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier !");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier PDF ou Word !");
                }
            }
        });

        // Bouton S'inscrire
        btnRegister = new JButton("S'inscrire");
        btnRegister.setBounds(50, 450, 300, 45);
        btnRegister.setBackground(new Color(0, 120, 215));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        add(btnRegister);

        btnRegister.addActionListener(e -> {
            String fullName = txtFullName.getText();
            String email = txtEmail.getText();
            String password = String.valueOf(txtPassword.getPassword());
            String city = txtCity.getText();
            String skills = txtSkills.getText();

            if(fullName.isBlank() || email.isBlank() || password.isBlank()){
                JOptionPane.showMessageDialog(this, "Veuillez remplir les champs obligatoires !");
                return;
            }

            if (Database.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "Ce compte existe déjà !");
            } else {
                Database.registerUser(fullName, email, password, city, skills, cvBytes); // envoyer le contenu du CV
                JOptionPane.showMessageDialog(this, "Inscription réussie !");
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // Bouton Connexion
        btnLogin = new JButton("Déjà un compte ? Connexion");
        btnLogin.setBounds(50, 510, 300, 35);
        btnLogin.setBackground(Color.GRAY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}
