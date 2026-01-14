package com.example.jobs.maven_exemple;

import weka.core.*;
import weka.classifiers.trees.RandomForest;
import weka.core.converters.ArffSaver;
import java.io.File;
import java.sql.ResultSet;
import java.util.*;

public class DatasetGenerator {

    public static Instances generateDataset() throws Exception {

        ArrayList<Attribute> attrs = new ArrayList<>();

        // Attributs skills utilisateur
        for (String s : JobMatcher.ALL_SKILLS)
            attrs.add(new Attribute("user_" + s));

        // Attributs skills offre
        for (String s : JobMatcher.ALL_SKILLS)
            attrs.add(new Attribute("job_" + s));

        // Label
        List<String> labels = List.of("0", "1");
        attrs.add(new Attribute("label", labels));

        Instances data = new Instances("job_matching", attrs, 0);
        data.setClassIndex(data.numAttributes() - 1);

        ResultSet users = Database.getAllUsers();
        ResultSet jobs = Database.getAllJobs();

        while (users.next()) {

            String skills = users.getString("skills");
            String cvText = CVExtractor.extractText(Database.getUserCV(users.getString("email")));

            Set<String> userSkills = JobMatcher.extractSkills(skills + " " + cvText);

            jobs.beforeFirst();

            while (jobs.next()) {
                String titre = jobs.getString("titre");
                String entreprise = jobs.getString("entreprise");
                String lieu = jobs.getString("lieu");

                // Si colonne description existe (Rekrute)
                String description = "";
                try { description = jobs.getString("description"); } catch (Exception ignored){}

                String offerText = titre + " " + entreprise + " " + lieu + " " + description;
                Set<String> jobSkills = JobMatcher.extractSkills(offerText);

                double[] fv = JobMatcher.buildFeatureVector(userSkills, jobSkills);

                // Label = 1 si au moins 2 skills correspondent
                int matches = 0;
                for (String s : jobSkills) if (userSkills.contains(s)) matches++;
                String label = matches >= 2 ? "1" : "0";

                double[] vals = Arrays.copyOf(fv, fv.length + 1);
                vals[fv.length] = labels.indexOf(label);

                data.add(new DenseInstance(1.0, vals));
            }
        }

        // Sauvegarder dataset
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("dataset.arff"));
        saver.writeBatch();

        return data;
    }

    public static void trainModel(Instances data) throws Exception {
        RandomForest rf = new RandomForest();
        rf.setNumIterations(100);
        rf.buildClassifier(data);

        weka.core.SerializationHelper.write("model.model", rf);
        System.out.println("✅ Modèle ML entraîné");
    }
}
