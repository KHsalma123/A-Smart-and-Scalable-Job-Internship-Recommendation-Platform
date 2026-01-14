package application.admin;

import service.admin.AdminScrapingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminScrapingFrame extends JFrame {

    private JComboBox<String> siteCombo;

    public AdminScrapingFrame() {

        setTitle("Gestion du scraping");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ===== Title =====
        JLabel lblTitle = new JLabel("Gestion du scraping", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JLabel lblSelect = new JLabel("Choisir le site à scraper");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSelect.setAlignmentX(Component.CENTER_ALIGNMENT);

        siteCombo = new JComboBox<>(new String[]{
                "rekrute.com",
                "marocannonces.com",
                "emploi.ma",
                "jobzyn.com",
                "All websites"
        });
        siteCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        siteCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        siteCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = createButton("Lancer le scraping", new Color(0, 120, 215));
        startBtn.addActionListener(e -> startScraping());

        JButton backBtn = createButton("Retour au tableau de bord", new Color(150, 150, 150));
        backBtn.addActionListener(e -> dispose());

        panel.add(lblSelect);
        panel.add(Box.createVerticalStrut(10));
        panel.add(siteCombo);
        panel.add(Box.createVerticalStrut(25));
        panel.add(startBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(backBtn);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void startScraping() {
        String selectedSite = (String) siteCombo.getSelectedItem();

        JOptionPane.showMessageDialog(this,
                "Lancement du scraping pour: " + selectedSite,
                "Scraping",
                JOptionPane.INFORMATION_MESSAGE
        );

        AdminScrapingService.startScraping(selectedSite);

        JOptionPane.showMessageDialog(this,
                "Scraping terminé avec succès!",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(color); }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminScrapingFrame::new);
    }
}
