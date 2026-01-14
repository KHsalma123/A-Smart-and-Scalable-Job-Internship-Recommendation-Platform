package application;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import com.example.jobs.maven_exemple.Database;

public class ProfileFrame extends JFrame {

    private JTextField txtFullName, txtCity, txtSkills;
    private JLabel lblEmail, lblCV;
    private JButton btnSave, btnChangePassword, btnImportCV;
    private String email;
    private byte[] cvBytes = null; // stocker le CV en BLOB

    public ProfileFrame(String email) {
        this.email = email;

        setTitle("Mon profil");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Mon profil");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(120, 20, 200, 40);
        add(lblTitle);

        // Récupérer infos utilisateur
        String fullName = Database.getUserName(email);
        String city = Database.getUserCity(email);
        String skills = Database.getUserSkills(email);
        cvBytes = Database.getUserCV(email);

        lblEmail = new JLabel("Email : " + email);
        lblEmail.setBounds(50, 80, 300, 25);
        add(lblEmail);

        txtFullName = new JTextField(fullName);
        txtFullName.setBounds(50, 120, 300, 35);
        txtFullName.setBorder(BorderFactory.createTitledBorder("Nom complet"));
        add(txtFullName);

        txtCity = new JTextField(city);
        txtCity.setBounds(50, 170, 300, 35);
        txtCity.setBorder(BorderFactory.createTitledBorder("Ville"));
        add(txtCity);

        txtSkills = new JTextField(skills);
        txtSkills.setBounds(50, 220, 300, 35);
        txtSkills.setBorder(BorderFactory.createTitledBorder("Compétences"));
        add(txtSkills);

        lblCV = new JLabel("CV : " + (cvBytes != null ? "Déjà importé" : "Non précisé"));
        lblCV.setBounds(50, 270, 300, 25);
        add(lblCV);

        btnImportCV = new JButton("Importer CV");
        btnImportCV.setBounds(50, 300, 300, 35);
        btnImportCV.setBackground(new Color(0,150,0));
        btnImportCV.setForeground(Color.WHITE);
        btnImportCV.setFocusPainted(false);
        add(btnImportCV);

        btnImportCV.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                String name = file.getName().toLowerCase();
                if(name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx")){
                    try {
                        cvBytes = Files.readAllBytes(file.toPath());
                        lblCV.setText("CV : " + file.getName());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier !");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier PDF ou Word !");
                }
            }
        });

        btnChangePassword = new JButton("Modifier mot de passe");
        btnChangePassword.setBounds(50, 350, 300, 35);
        btnChangePassword.setBackground(new Color(255,140,0));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFocusPainted(false);
        add(btnChangePassword);
        btnChangePassword.addActionListener(e -> openChangePasswordDialog());

        btnSave = new JButton("Enregistrer");
        btnSave.setBounds(50, 400, 300, 40);
        btnSave.setBackground(new Color(10,102,194));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        add(btnSave);

        btnSave.addActionListener(e -> {
            Database.updateUserProfile(email, txtFullName.getText(), txtCity.getText(),
                    txtSkills.getText(), cvBytes);
            JOptionPane.showMessageDialog(this, "Profil mis à jour !");
            dispose();
        });
    }

    private void openChangePasswordDialog() {
        Database.generateResetCode(email);

        JDialog dialog = new JDialog(this, "Changer mot de passe", true);
        dialog.setSize(350, 250);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JLabel lblCode = new JLabel("Code reçu par email :");
        lblCode.setBounds(30, 30, 200, 25);
        dialog.add(lblCode);

        JTextField txtCode = new JTextField();
        txtCode.setBounds(30, 60, 280, 30);
        dialog.add(txtCode);

        JLabel lblNewPass = new JLabel("Nouveau mot de passe :");
        lblNewPass.setBounds(30, 100, 200, 25);
        dialog.add(lblNewPass);

        JPasswordField txtNewPass = new JPasswordField();
        txtNewPass.setBounds(30, 130, 280, 30);
        dialog.add(txtNewPass);

        JButton btnValidate = new JButton("Valider");
        btnValidate.setBounds(90, 170, 150, 30);
        btnValidate.setBackground(new Color(10,102,194));
        btnValidate.setForeground(Color.WHITE);
        btnValidate.setFocusPainted(false);
        dialog.add(btnValidate);

        btnValidate.addActionListener(e -> {
            String code = txtCode.getText().trim();
            String newPassword = new String(txtNewPass.getPassword());
            if(code.isBlank() || newPassword.isBlank()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs !");
                return;
            }
            if(Database.verifyResetCode(email, code)) {
                Database.updatePassword(email, newPassword);
                JOptionPane.showMessageDialog(dialog, "Mot de passe mis à jour !");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Code incorrect !");
            }
        });

        dialog.setVisible(true);
    }
}
