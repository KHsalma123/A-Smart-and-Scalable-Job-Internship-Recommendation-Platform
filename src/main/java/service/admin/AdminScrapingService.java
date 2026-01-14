package service.admin;

import com.example.jobs.maven_exemple.Emploimascrapper;
import com.example.jobs.maven_exemple.JobzynScraper;
import com.example.jobs.maven_exemple.MarocAnnoncesScraper;
import com.example.jobs.maven_exemple.RekruteScraper;

public class AdminScrapingService {

    public static void startScraping(String site) {
        switch (site.toLowerCase()) {
            case "rekrute.com":
                scrapeRekrute();
                break;

            case "marocannonces.com":
                scrapeMarocAnnonces();
                break;

            case "emploi.ma":
                scrapeEmploi();
                break;

            case "jobzyn.com":
                scrapeJobzyn();
                break;

            case "all websites":
                int total = 0;
                total += scrapeRekrute();
                total += scrapeMarocAnnonces();
                total += scrapeEmploi();
                total += scrapeJobzyn();
                System.out.println("🎉 Total new offers added: " + total);
                break;

            default:
                System.out.println("❌ Unknown site: " + site);
        }
    }

    private static int scrapeRekrute() {
        System.out.println("🔹 Scraping Rekrute.com ...");
        int count = 0;
        try {
            count = RekruteScraper.mainScrape(); // <- new method returning # of new offers
        } catch (Exception e) {
            System.err.println("❌ Error scraping Rekrute: " + e.getMessage());
        }
        System.out.println("✅ Rekrute.com - new offers added: " + count);
        return count;
    }

    private static int scrapeMarocAnnonces() {
        System.out.println("🔹 Scraping MarocAnnonces.com ...");
        int count = 0;
        try {
            count = MarocAnnoncesScraper.mainScrape();
        } catch (Exception e) {
            System.err.println("❌ Error scraping MarocAnnonces: " + e.getMessage());
        }
        System.out.println("✅ MarocAnnonces.com - new offers added: " + count);
        return count;
    }

    private static int scrapeEmploi() {
        System.out.println("🔹 Scraping Emploi.ma ...");
        int count = 0;
        try {
            count = Emploimascrapper.mainScrape();
        } catch (Exception e) {
            System.err.println("❌ Error scraping Emploi.ma: " + e.getMessage());
        }
        System.out.println("✅ Emploi.ma - new offers added: " + count);
        return count;
    }

    private static int scrapeJobzyn() {
        System.out.println("🔹 Scraping Jobzyn.com ...");
        int count = 0;
        try {
            count = JobzynScraper.mainScrape();
        } catch (Exception e) {
            System.err.println("❌ Error scraping Jobzyn: " + e.getMessage());
        }
        System.out.println("✅ Jobzyn.com - new offers added: " + count);
        return count;
    }
}
