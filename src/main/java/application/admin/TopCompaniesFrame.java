package application.admin;

import service.admin.StatisticsService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TopCompaniesFrame extends JFrame {

    public TopCompaniesFrame() {
        setTitle("Meilleures entreprises selon le nombre d’offres");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Dataset =====
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Get top companies across all sites (limit top 10 per site)
        Map<String, Map<String, Integer>> companiesBySite = StatisticsService.topCompaniesPerSite(10);

        // Add data to dataset, filtering out unspecified, anonymous, or empty companies
        companiesBySite.forEach((site, companies) -> {
            companies.forEach((company, count) -> {
                if (company != null) {
                    String cleanName = company.trim();
                    if (!cleanName.isEmpty() &&
                        !cleanName.equalsIgnoreCase("non précisée") &&
                        !cleanName.equalsIgnoreCase("anonyme") &&
                        !cleanName.equalsIgnoreCase("non spécifié")&&
                        !cleanName.equalsIgnoreCase("Confidentiel")) {
                        dataset.addValue(count, site, cleanName);
                    }
                }
            });
        });

        // ===== Chart =====
        JFreeChart chart = ChartFactory.createBarChart(
                "Meilleures entreprises selon le nombre d’offres",
                "Entreprises",
                "Nombre d’offres",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,   // legend
                true,   // tooltips
                false   // URLs
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        add(new ChartPanel(chart));
        setVisible(true);
    }

    // ===== Test =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TopCompaniesFrame::new);
    }
}
