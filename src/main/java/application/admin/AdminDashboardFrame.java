package application.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import application.admin.AdminScrapingFrame;

public class AdminDashboardFrame extends JFrame {

    public AdminDashboardFrame() {
        setTitle("Tableau de bord Admin");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Tableau de bord Admin", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Buttons
        JButton scrapeBtn = createButton("Lancer le scraping", new Color(0, 120, 215));
        JButton statsBtn = createButton("Voir les statistiques", new Color(50, 200, 50));
        JButton logoutBtn = createButton("Déconnexion", new Color(220, 50, 50));

        scrapeBtn.addActionListener(e -> new AdminScrapingFrame());
        statsBtn.addActionListener(e -> new AdminStatisticsMenuFrame());
        logoutBtn.addActionListener(e -> dispose());

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        panel.add(scrapeBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(statsBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(logoutBtn);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Helper method to create modern buttons
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboardFrame::new);
    }
}
