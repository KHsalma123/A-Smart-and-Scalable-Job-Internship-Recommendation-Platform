package application.admin;

import service.admin.StatisticsService;
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.Map;

public class OffersByCityFrame extends JFrame {

    public OffersByCityFrame() {
        setTitle("Offres par ville");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        StatisticsService.offersByCity()
                .entrySet()
                .stream()
                .limit(10)
                .forEach(e ->
                        dataset.addValue(e.getValue(), "Offres", e.getKey())
                );

        JFreeChart chart = ChartFactory.createBarChart(
                "Top villes",
                "Ville",
                "Offres",
                dataset
        );

        add(new ChartPanel(chart));
        setVisible(true);
    }
}
