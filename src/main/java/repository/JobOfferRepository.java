package repository;

import java.io.*;
import java.util.*;

public class JobOfferRepository {

    public static List<String[]> load(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
