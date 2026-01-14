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

public class Emploimascrapper {

    private static final String BASE_URL = "https://www.emploi.ma";
    private static final int MAX_PAGES = 29;

    // ✅ Main scraping method returns number of new offers added
    public static int mainScrape() {
        Set<String> existingLinks = new HashSet<>();
        int addedCount = 0;

        try {
            for (int page = 0; page < MAX_PAGES; page++) {
                String url = BASE_URL + "/recherche-jobs-maroc?page=" + page;
                System.out.println("🔍 Scraping page: " + url);

                // Fetch the page
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.5845.97 Safari/537.36")
                        .timeout(15000)
                        .get();

                Elements jobs = doc.select("div.card.card-job");
                if (jobs.isEmpty()) {
                    System.out.println("🚫 No job offers found – stopping pagination");
                    break;
                }

                for (Element job : jobs) {
                    try {
                        // ===== TITLE + LINK =====
                        Element titleEl = job.selectFirst("h3 a");
                        if (titleEl == null) continue;

                        String href = titleEl.attr("href");
                        String link = href.startsWith("http") ? href : BASE_URL + href;

                        // Skip duplicates in the same scrape
                        if (existingLinks.contains(link)) continue;
                        existingLinks.add(link);

                        String fullTitle = titleEl.text().trim();
                        String title = fullTitle;
                        String location = "Non spécifié";

                        if (fullTitle.contains(" - ")) {
                            int lastDash = fullTitle.lastIndexOf(" - ");
                            title = fullTitle.substring(0, lastDash).trim();
                            location = fullTitle.substring(lastDash + 3).trim();
                        }

                        // ===== COMPANY =====
                        String company = "Non spécifiée";
                        Element companyEl = job.selectFirst(".company-name");
                        if (companyEl != null && !companyEl.text().trim().isEmpty())
                            company = companyEl.text().trim();

                        // ===== DESCRIPTION =====
                        String description = "Non spécifié";
                        Element descEl = job.selectFirst(".card-job-description p");
                        if (descEl != null && !descEl.text().trim().isEmpty())
                            description = descEl.text().trim();

                        String source = "Emploi.ma";

                        // Insert into database and count if new
                        if (insertJobOffer(title, company, location, description, link, source)) {
                            addedCount++;
                        }

                        // Small pause to avoid IP block
                        Thread.sleep(300);

                    } catch (Exception e) {
                        System.err.println("⚠️ Skipped a job due to error: " + e.getMessage());
                    }
                }

                // Pause between pages
                Thread.sleep(800);
            }

            System.out.println("🎉 Emploi.ma scraping finished. New offers added: " + addedCount);

        } catch (Exception e) {
            System.err.println("❌ Error scraping Emploi.ma at page: " + e.getMessage());
        }

        return addedCount;
    }

    // ===== Database insertion method returns true if offer is new =====
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
            System.out.println("✅ New offer added: " + title);
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Already exists in DB
            System.out.println("⏭️ Offer already exists: " + link);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Optional main for testing =====
    public static void main(String[] args) {
        int newOffers = mainScrape();
        System.out.println("Total new offers added: " + newOffers);
    }
}
