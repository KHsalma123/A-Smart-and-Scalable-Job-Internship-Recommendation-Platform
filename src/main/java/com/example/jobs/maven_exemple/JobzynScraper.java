package com.example.jobs.maven_exemple;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class JobzynScraper {

    public static int mainScrape() {
        int addedCount = 0;

        // Path to ChromeDriver
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\SALMA.KH\\Downloads\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        try {
            String url = "https://www.jobzyn.com/fr/jobs/maroc";
            driver.get(url);

            // Wait for job elements
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a[href*='/jobs/'] h2")));

            // Scroll to load all offers
            JavascriptExecutor js = (JavascriptExecutor) driver;
            long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
            while (true) {
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(1500);
                long newHeight = (long) js.executeScript("return document.body.scrollHeight");
                if (newHeight == lastHeight) break;
                lastHeight = newHeight;
            }

            // Get all job elements
            List<WebElement> jobs = driver.findElements(By.cssSelector("a[href*='/jobs/']"));

            for (WebElement job : jobs) {
                try {
                    String titre = "";
                    try { 
                        titre = job.findElement(By.tagName("h2")).getText().trim(); 
                    } catch (Exception ignored) {}
                    if (titre.isEmpty() || titre.equalsIgnoreCase("Voir plus")) continue;

                    String lien = job.getAttribute("href");

                    String entreprise = "";
                    try { 
                        entreprise = job.findElement(By.cssSelector("div span")).getText().trim(); 
                    } catch (Exception ignored) {}
                    String lieu = "";
                    try { 
                        lieu = job.findElement(By.cssSelector("img[alt*='location'] + span")).getText().trim(); 
                    } catch (Exception ignored) {}

                    if (entreprise.isEmpty()) entreprise = "Non précisée";
                    if (lieu.isEmpty()) lieu = "Non précisé";

                    String typeContrat = "";
                    try { 
                        typeContrat = job.findElement(By.xpath(".//span[contains(text(),'Type de contrat')]"))
                                         .getText().replace("Type de contrat:", "").trim(); 
                    } catch (Exception ignored) {}

                    String experience = "";
                    try { 
                        experience = job.findElement(By.xpath(".//span[contains(text(),'Expérience')]"))
                                        .getText().replace("Expérience:", "").trim(); 
                    } catch (Exception ignored) {}

                    // Insert into DB, returns boolean if added
                    if (Database.insertJob(titre, lien, entreprise, lieu, typeContrat, experience)) {
                        addedCount++;
                        System.out.println("✅ New offer added: " + titre);
                    }

                } catch (Exception ignored) {}
            }

        } catch (Exception e) {
            System.err.println("❌ Error Jobzyn: " + e.getMessage());
        } finally {
            driver.quit();
        }

        System.out.println("🎉 Jobzyn scraping finished. New offers added: " + addedCount);
        return addedCount;
    }

    // Optional main for testing
    public static void main(String[] args) {
        int newOffers = mainScrape();
        System.out.println("Total new offers added: " + newOffers);
    }
}
