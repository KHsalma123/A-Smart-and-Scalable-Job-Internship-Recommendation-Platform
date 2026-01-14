package application;

import com.example.jobs.maven_exemple.*;
import weka.classifiers.Classifier;
import weka.core.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.sql.ResultSet;
import java.util.*;

public class OpportunitiesFrame extends JFrame {

    private static final double THRESHOLD = 0.35;

    public OpportunitiesFrame(String email) throws Exception {

        setTitle("Opportunités ML");
        setSize(1000, 600); // un peu plus grand pour les filtres
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        add(mainPanel);

        // Panel filtres et boutons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        filterPanel.add(new JLabel("Ville:"));
        JTextField cityField = new JTextField(15);
        filterPanel.add(cityField);

        filterPanel.add(new JLabel("Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Tous", "Stage", "Emploi"});
        filterPanel.add(typeCombo);

        JButton searchBtn = new JButton("Rechercher");
        searchBtn.setBackground(new Color(10,102,194));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        filterPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.setBackground(new Color(50,200,50));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        filterPanel.add(refreshBtn);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel model = new DefaultTableModel(new String[]{"Titre","Entreprise","Lieu","Probabilité"},0);
        JTable table = new JTable(model);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Charger le modèle Weka
        Classifier clf = (Classifier) weka.core.SerializationHelper.read("model.model");
        Instances structure = new Instances(DatasetGenerator.generateDataset(), 0);

        Set<String> userSkills = JobMatcher.extractSkills(
                Database.getUserSkills(email) + " " +
                CVExtractor.extractText(Database.getUserCV(email))
        );

        System.out.println("👤 USER : " + email);
        System.out.println("📄 Skills extraits du CV : " + String.join(" | ", userSkills));

        // Fonction pour remplir la table avec filtres
        Runnable loadTable = () -> {
            try {
                model.setRowCount(0); // vider table
                ResultSet rs = Database.getAllJobs();
                Map<Integer,String> links = new HashMap<>();

                while (rs.next()) {

                    String titre = rs.getString("titre");
                    String entreprise = rs.getString("entreprise");
                    String lieu = rs.getString("lieu");
                    String lien = rs.getString("lien");

                    String description = "";
                    try { description = rs.getString("description"); } catch (Exception ignored){}

                    // FILTRES
                    String cityFilter = cityField.getText().trim().toLowerCase();
                    String typeFilter = typeCombo.getSelectedItem().toString().toLowerCase();

                    if(!cityFilter.isEmpty() && !lieu.toLowerCase().contains(cityFilter)) continue;
                    String titreLower = titre.toLowerCase();
                    boolean isStage = titreLower.contains("stage") || titreLower.contains("stagiaire");

                    if(!typeFilter.equals("tous")) {
                        if(typeFilter.equals("stage") && !isStage) continue;
                        if(typeFilter.equals("emploi") && isStage) continue;
                    }


                    String offerText = titre + " " + entreprise + " " + lieu + " " + description;
                    Set<String> jobSkills = JobMatcher.extractSkills(offerText);

                    double[] fv = JobMatcher.buildFeatureVector(userSkills, jobSkills);

                    double[] vals = Arrays.copyOf(fv, fv.length + 1);
                    vals[fv.length] = 0;

                    Instance inst = new DenseInstance(1.0, vals);
                    inst.setDataset(structure);

                    double prob = clf.distributionForInstance(inst)[1];

                    if (prob >= THRESHOLD) {
                        model.addRow(new Object[]{titre, entreprise, lieu, String.format("%.2f", prob)});
                        links.put(model.getRowCount()-1, lien);
                    }
                }

                // Clic sur table pour ouvrir lien
                table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        int r = table.getSelectedRow();
                        if (links.containsKey(r)) {
                            try { Desktop.getDesktop().browse(new URI(links.get(r))); } catch (Exception ignored){}
                        }
                    }
                });

            } catch (Exception ex) { ex.printStackTrace(); }
        };

        loadTable.run(); // charger au démarrage

        // Boutons actions
        searchBtn.addActionListener(e -> loadTable.run());
        refreshBtn.addActionListener(e -> {
            cityField.setText("");
            typeCombo.setSelectedIndex(0);
            loadTable.run();
        });

        setVisible(true);
    }
}
