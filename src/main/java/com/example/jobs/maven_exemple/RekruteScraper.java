package com.example.jobs.maven_exemple;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class RekruteScraper {

    private static String valeurParDefaut(String value, String defaut) {
        return (value == null || value.trim().isEmpty()) ? defaut : value.trim();
    }

    public static int mainScrape() {
        int addedCount = 0;

        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\SALMA.KH\\Downloads\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 15);

        try {
            int page = 1;
            int maxPages = 158;

            while (page <= maxPages) {
                driver.get("https://www.rekrute.com/offres-emploi-maroc.html?p=" + page);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("post-data")));

                List<WebElement> jobs = driver.findElements(By.cssSelector("#post-data li.post-id"));

                for (WebElement job : jobs) {
                    try {
                        WebElement aTag = job.findElement(By.cssSelector("h2 a.titreJob"));
                        String fullTitle = aTag.getText();
                        String lien = aTag.getAttribute("href");

                        String titre = fullTitle;
                        String lieu = "Non précisé";
                        if (fullTitle != null && fullTitle.contains("|")) {
                            String[] parts = fullTitle.split("\\|");
                            titre = parts[0];
                            lieu = parts.length > 1 ? parts[1] : lieu;
                        }
                        titre = valeurParDefaut(titre, "Titre non précisé");
                        lieu = valeurParDefaut(lieu, "Non précisé");

                        String entreprise = "";
                        try {
                            WebElement logo = job.findElement(By.cssSelector("img.photo"));
                            entreprise = logo.getAttribute("alt");
                        } catch (Exception ignored) {}
                        entreprise = valeurParDefaut(entreprise, "Non précisée");

                        String description = "";
                        try {
                            WebElement descElem = job.findElement(By.cssSelector("div.info span"));
                            description = descElem.getText();
                        } catch (Exception ignored) {}
                        description = valeurParDefaut(description, "Non disponible");

                        String dateDebut = "";
                        String dateFin = "";
                        String postes = "";
                        try {
                            WebElement dateElem = job.findElement(By.cssSelector("em.date"));
                            List<WebElement> spans = dateElem.findElements(By.tagName("span"));
                            if (spans.size() >= 2) {
                                dateDebut = spans.get(0).getText();
                                dateFin = spans.get(1).getText();
                            }
                            postes = spans.get(spans.size() - 1).getText();
                        } catch (Exception ignored) {}

                        dateDebut = valeurParDefaut(dateDebut, "");
                        dateFin   = valeurParDefaut(dateFin, "");
                        postes    = valeurParDefaut(postes, "");

                        // Insert into DB, returns boolean if added
                        if (Database.insertRekrute(titre, lien, entreprise, lieu, description, dateDebut, dateFin, postes)) {
                            addedCount++;
                            System.out.println("✅ New offer added: " + titre);
                        }

                    } catch (Exception ignored) {}
                }

                page++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        System.out.println("🎉 Rekrute scraping finished. New offers added: " + addedCount);
        return addedCount;
    }

    // Optional main for testing
    public static void main(String[] args) {
        int newOffers = mainScrape();
        System.out.println("Total new offers added: " + newOffers);
    }
}
