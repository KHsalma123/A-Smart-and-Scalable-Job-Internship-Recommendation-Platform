package com.example.jobs.maven_exemple;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

public class MarocAnnoncesScraper {

    private static final String BASE_URL = "https://www.marocannonces.com/categorie/309/Emploi/Offres-emploi.html";
    private static final int MAX_PAGES = 20;

    public static int mainScrape() {
        Set<String> visitedLinks = new HashSet<>();
        int addedCount = 0;

        try {
            for (int page = 1; page <= MAX_PAGES; page++) {
                String url = page == 1 ? BASE_URL : BASE_URL.replace(".html", "/" + page + ".html");
                System.out.println("🔍 Scraping page MarocAnnonces: " + url);

                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();
                Elements jobLinks = doc.select("a[href*='/Offres-emploi/annonce/']");
                if (jobLinks.isEmpty()) break;

                for (Element linkEl : jobLinks) {
                    String link = "https://www.marocannonces.com/" + linkEl.attr("href");
                    if (visitedLinks.contains(link)) continue;
                    visitedLinks.add(link);

                    try {
                        Document jobDoc = Jsoup.connect(link).userAgent("Mozilla/5.0").timeout(10000).get();
                        String title = jobDoc.select("div.description.desccatemploi h1").text();
                        String description = jobDoc.select("div.block").text();
                        String location = jobDoc.select("ul.info-holder li:first-child a").text();
                        String company = jobDoc.select("ul.extraQuestionName li:nth-child(4) a").text();
                        String source = "MarocAnnonces";

                        if (title.isEmpty()) continue;
                        if (company.isEmpty()) company = "Non spécifié";
                        if (location.isEmpty()) location = "Non spécifié";
                        if (description.isEmpty()) description = "Non spécifié";

                        if (insertJobOffer(title, company, location, description, link, source)) {
                            addedCount++;
                        }

                        Thread.sleep(500);
                    } catch (Exception ignored) {}
                }

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("❌ Error MarocAnnonces: " + e.getMessage());
        }

        return addedCount;
    }

    private static boolean insertJobOffer(String title, String company, String location,
                                          String description, String link, String source) {
        String sql = "INSERT INTO job_offers (title, company, location, description, link, source) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, company);
            stmt.setString(3, location);
            stmt.setString(4, description);
            stmt.setString(5, link);
            stmt.setString(6, source);

            stmt.executeUpdate();
            System.out.println("✅ Offre enregistrée : " + title);
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⏭️ Offre déjà existante : " + link);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
