package application;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import com.example.jobs.maven_exemple.DatasetGenerator;
import weka.core.Instances;

public class Main {
    public static void main(String[] args) {

        try { UIManager.setLookAndFeel(new FlatLightLaf()); }
        catch (Exception ignored) {}

        try {
            Instances dataset = DatasetGenerator.generateDataset();
            DatasetGenerator.trainModel(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true));
    }
}
