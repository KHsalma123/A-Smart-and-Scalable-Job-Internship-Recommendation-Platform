package com.example.jobs.maven_exemple;

import java.text.Normalizer;
import java.util.*;

public class JobMatcher {

	public static final List<String> ALL_SKILLS = List.of(
		    "java","python","c","c++","php","javascript","html","css",
		    "react","angular","vue","spring","django","flask",
		    "sql","mysql","mongodb","oracle","postgresql",
		    "git","docker","linux","aws","azure","laravel",
		    "informatique","developpement","fullstack","backend","frontend",
		    // Management / business
		    "management","gestion de projet","organisation","leadership","communication","analyse","strategie"
		);


    // Extraire skills depuis n'importe quel texte
    public static Set<String> extractSkills(String text) {
        Set<String> result = new HashSet<>();
        if (text == null || text.isEmpty()) return result;

        text = Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^a-z0-9+# ]", " ");

        for (String w : text.split("\\s+")) {
            if (ALL_SKILLS.contains(w)) result.add(w);
        }
        return result;
    }

    // Créer vecteur user + job
    public static double[] buildFeatureVector(Set<String> userSkills, Set<String> jobSkills) {
        double[] vals = new double[ALL_SKILLS.size() * 2];
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            String s = ALL_SKILLS.get(i);
            vals[i] = userSkills.contains(s) ? 1.0 : 0.0;
            vals[i + ALL_SKILLS.size()] = jobSkills.contains(s) ? 1.0 : 0.0;
        }
        return vals;
    }
}
