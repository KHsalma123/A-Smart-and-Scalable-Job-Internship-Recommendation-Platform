package application.admin;

import service.admin.StatisticsService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class OffersBySiteFrame extends JFrame {

    public OffersBySiteFrame() {
        setTitle("Offres d’emploi par site");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // ===== Dataset =====
        DefaultPieDataset dataset = new DefaultPieDataset();

        // ✅ CORRECT METHOD
        Map<String, Integer> data = StatisticsService.offersBySite();

        // ✅ FORCE 4 SITES (ALWAYS SHOWN)
        dataset.setValue("Jobzyn", data.getOrDefault("Jobzyn", 0));
        dataset.setValue("Rekrute", data.getOrDefault("Rekrute", 0));
        dataset.setValue("Emploi.ma", data.getOrDefault("Emploi.ma", 0));
        dataset.setValue("MarocAnnonces", data.getOrDefault("MarocAnnonces", 0));

        // ===== Chart =====
        JFreeChart chart = ChartFactory.createPieChart(
                "Offres d’emploi par site",
                dataset,
                true,   // legend
                true,   // tooltips
                false
        );

        // ===== Style =====
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelFont(new Font("Segoe UI", Font.BOLD, 12));
        plot.setSimpleLabels(true);
        plot.setCircular(true);

        // Custom colors
        plot.setSectionPaint("Jobzyn", new Color(0, 153, 204));
        plot.setSectionPaint("Rekrute", new Color(255, 153, 51));
        plot.setSectionPaint("Emploi.ma", new Color(102, 204, 0));
        plot.setSectionPaint("MarocAnnonces", new Color(204, 0, 102));

        add(new ChartPanel(chart));
        setVisible(true);
    }

    // ===== Test =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(OffersBySiteFrame::new);
    }
}
