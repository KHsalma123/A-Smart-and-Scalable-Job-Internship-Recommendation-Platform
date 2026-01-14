package application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import com.example.jobs.maven_exemple.Database;

public class HomeFrame extends JFrame {

    private JTextField txtVille, txtSpecialite, txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cmbType;
    private Map<Integer, String> liensMap = new HashMap<>();

    public HomeFrame(String email) {

        setTitle("Opportunités d'emploi");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        getContentPane().setBackground(new Color(245,246,248)); 

        /* ================= HEADER ================= */
     // Bouton Offres pour moi
        JButton btnOpportunities = new JButton("Offres pour moi");
        btnOpportunities.setFocusPainted(false);
        btnOpportunities.setBackground(Color.WHITE);
        btnOpportunities.setForeground(new Color(10,102,194));
        btnOpportunities.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnOpportunities.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOpportunities.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(10,102,194), 2, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Hover effect
        btnOpportunities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOpportunities.setBackground(new Color(10,102,194));
                btnOpportunities.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOpportunities.setBackground(Color.WHITE);
                btnOpportunities.setForeground(new Color(10,102,194));
            }
        });

        // Action
        btnOpportunities.addActionListener(e -> {
            try {
                new OpportunitiesFrame(email).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors du chargement des opportunités : " + ex.getMessage());
            }
        });

        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(10,102,194));
        header.setBorder(new EmptyBorder(15,20,15,20));

        String nomUtilisateur = Database.getUserName(email);
        JLabel lblUser = new JLabel("Bonjour " + nomUtilisateur + " !");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Bouton Profil
        JButton btnProfile = new JButton("Mon profil");
        btnProfile.setFocusPainted(false);
        btnProfile.setBackground(Color.WHITE);
        btnProfile.setForeground(new Color(10,102,194));
        btnProfile.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProfile.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(10,102,194), 2, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProfile.setBackground(new Color(10,102,194)); 
                btnProfile.setForeground(Color.WHITE);          
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProfile.setBackground(Color.WHITE);          
                btnProfile.setForeground(new Color(10,102,194));
            }
        });
        btnProfile.addActionListener(e -> new ProfileFrame(email).setVisible(true));

        // Bouton Déconnexion
        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(Color.WHITE);           
        btnLogout.setForeground(new Color(10,102,194)); 
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(10,102,194), 2, true), 
                BorderFactory.createEmptyBorder(8, 20, 8, 20)  
        ));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(10,102,194)); 
                btnLogout.setForeground(Color.WHITE);          
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(Color.WHITE);          
                btnLogout.setForeground(new Color(10,102,194));
            }
        });
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        profilePanel.setBackground(new Color(10,102,194));
        profilePanel.add(btnOpportunities);
        profilePanel.add(btnProfile);
        profilePanel.add(btnLogout);

        header.add(lblUser, BorderLayout.WEST);
        header.add(profilePanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        /* ================= FILTERS ================= */
        JPanel filters = new JPanel(new GridLayout(1,6,10,10));
        filters.setBorder(new EmptyBorder(15,15,15,15));
        filters.setBackground(Color.WHITE);

        txtSearch = createField("Recherche");
        txtSpecialite = createField("Spécialité");
        txtVille = createField("Ville");

        cmbType = new JComboBox<>(new String[]{"Tous", "Emploi", "Stage"});
        cmbType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbType.setBackground(Color.WHITE);

        JButton btnSearch = createButton("Rechercher", new Color(10,102,194));
        JButton btnRefresh = createButton("Actualiser", new Color(50, 200, 50));

        filters.add(txtSearch);
        filters.add(txtSpecialite);
        filters.add(txtVille);
        filters.add(cmbType);
        filters.add(btnSearch);
        filters.add(btnRefresh);

        add(filters, BorderLayout.CENTER);

        /* ================= TABLE ================= */
        model = new DefaultTableModel(
                new String[]{"Titre", "Entreprise", "Lieu", "Type", "Plus d'infos"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220,235,252));
        table.setGridColor(new Color(220,220,220));
        table.setShowGrid(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setBackground(new Color(240,240,240));
        tableHeader.setForeground(Color.DARK_GRAY);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                "Toutes les opportunités",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14)
        ));

        add(scroll, BorderLayout.SOUTH);

        /* ================= EVENTS ================= */
        btnSearch.addActionListener(e -> loadJobs());
        btnRefresh.addActionListener(e -> loadJobs());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (col == 4) {
                    openLink(liensMap.get(row));
                }
            }
        });

        loadJobs();
    }

    /* ================= LOGIC ================= */
    private void loadJobs() {

        model.setRowCount(0);
        liensMap.clear();

        String villeFiltre = txtVille.getText().trim().toLowerCase();
        String specFiltre = txtSpecialite.getText().trim().toLowerCase();
        String keyword = txtSearch.getText().trim();
        String typeFiltre = cmbType.getSelectedItem().toString();

        try {
        	ResultSet rs = Database.searchAllJobs(keyword); 

        	while (rs != null && rs.next()) {
        	    String titre = rs.getString("titre");
        	    String entrepriseDB = rs.getString("entreprise");
        	    String lieuDB = rs.getString("lieu");
        	    String lien = rs.getString("lien");

        	    // Appliquer filtres ville et spécialité
        	    if (!txtVille.getText().trim().isEmpty() && !lieuDB.toLowerCase().contains(txtVille.getText().trim().toLowerCase()))
        	        continue;
        	    if (!txtSpecialite.getText().trim().isEmpty() && !titre.toLowerCase().contains(txtSpecialite.getText().trim().toLowerCase()))
        	        continue;

        	    String type = (titre.toLowerCase().contains("stage") || titre.toLowerCase().contains("stagiaire")) ? "Stage" : "Emploi";

        	    if (!cmbType.getSelectedItem().toString().equals("Tous") &&
        	        !type.equals(cmbType.getSelectedItem().toString()))
        	        continue;

        	    model.addRow(new Object[]{titre, entrepriseDB, lieuDB, type, "Plus d'infos"});
        	    liensMap.put(model.getRowCount() - 1, lien);
        	}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= UI HELPERS ================= */
    private JTextField createField(String title) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(Color.WHITE);
        return field;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Impossible d'ouvrir le lien");
        }
    }
}
