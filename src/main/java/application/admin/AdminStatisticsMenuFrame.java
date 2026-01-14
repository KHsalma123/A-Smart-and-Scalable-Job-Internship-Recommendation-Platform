package application.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminStatisticsMenuFrame extends JFrame {

    public AdminStatisticsMenuFrame() {

        setTitle("Statistiques Admin – Scraping d’offres d’emploi");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ===== Title =====
        JLabel title = new JLabel("Statistiques et analyses", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        add(title, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 20, 120));

        // ===== Buttons =====
        JButton btnOffersBySite = createButton("Offres par site", new Color(0, 123, 255));
        JButton btnOffersByCity = createButton("Offres par ville", new Color(0, 123, 255));
        JButton btnTopCompanies = createButton("Top entreprises", new Color(0, 123, 255));

        // ===== Actions =====
        btnOffersBySite.addActionListener(e -> new OffersBySiteFrame());
        btnOffersByCity.addActionListener(e -> new OffersByCityFrame());
        btnTopCompanies.addActionListener(e -> new TopCompaniesFrame());

        // ===== Add buttons =====
        centerPanel.add(btnOffersBySite);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(btnOffersByCity);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(btnTopCompanies);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Bottom (Back) =====
        JButton backBtn = createButton("Retour", new Color(108, 117, 125));
        backBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ===== Button Factory =====
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    // ===== Test =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminStatisticsMenuFrame::new);
    }
}
