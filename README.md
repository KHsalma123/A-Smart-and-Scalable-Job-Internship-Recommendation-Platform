📌 Job & Internship Recommendation Platform (Java)

📖 Project Overview
This project is a Java-based platform designed to centralize, analyze, and recommend job and internship offers in Morocco.
The application automatically collects job offers from multiple Moroccan recruitment websites, stores them in a database, and provides personalized recommendations based on the user’s profile and CV using Machine Learning.

🌐 Scraped Job Sources
The platform scrapes job offers from four Moroccan websites:
Rekrute
🔗 https://www.rekrute.com/
Jobzyn
🔗 https://www.jobzyn.com/fr/jobs/maroc
Emploi.ma
🔗 https://www.emploi.ma/
MarocAnnonces
🔗 https://www.marocannonces.com/categorie/309/Emploi/Offres-emploi.html
🔍 Scraping Strategy
The scraping approach depends on the website and its content type. For Rekrute and Jobzyn, Selenium with ChromeDriver is used because these sites load content dynamically with JavaScript, including pagination. For Emploi.ma and MarocAnnonces, which have static HTML pages, Jsoup is used for faster and lighter HTML parsing. This way, each tool is chosen according to the website’s structure for efficient data extraction.

🛠 Technologies Used
🔹 Backend & Core
Java (JDK 21) – Main programming language
Swing (Java GUI) – Desktop user interface
FlatLaf – Modern look and feel for Swing
JDBC – Database communication
Maven – Dependency and project management
🔹 Web Scraping
Selenium WebDriver
ChromeDriver
WebDriverManager
Jsoup
🔹 Database
MySQL
CVs stored as BLOB
Job offers unified from multiple sources
🔹 Machine Learning
Weka
Binary Classification
Random Forest algorithm
🔹 CV Processing
Apache PDFBox – Extract text from CVs (PDF)
🔹 Email Service
JavaMail API – Password recovery via email
🔹 Visualization
JFreeChart – Admin statistics
🔹 Logging
Logback

🗄 Database Integration
A MySQL database is used to store:
Users and profiles
CVs (as BLOB)
Job offers scraped from all sources
Offers from different websites are merged logically and treated as a unified dataset.
Each user can consult all offers and access the “Jobs for Me” feature.
lien drive: https://drive.google.com/drive/folders/1Y7SqeNgZDu-C-t1iQgQUD6_ElclkbEsf?usp=sharing

🤖 Machine Learning & Recommendation System
🔑 Objective
Provide personalized job recommendations based on the user’s:
Skills
Profile
Extracted CV content
⚙ How It Works
Skill Extraction
Skills are extracted from:
User profile
Uploaded CV (PDF)
Job descriptions
Implemented using JobMatcher.extractSkills()
Feature Vector Construction
Binary vectors represent:
User skills
Job skills
Implemented using JobMatcher.buildFeatureVector()
Model
Binary classification
Relevant (1)
Not relevant (0)
Random Forest (Weka)
Pre-trained model (model.model) using dataset.arff
Prediction
The model predicts the probability of compatibility
Only offers with probability ≥ 0.35 are displayed

🧠 Machine Learning Dataset
Training dataset: dataset.arff
Represents users and job offers as binary feature vectors
Used to train the Random Forest classifier in Weka

👤 User Features
Account creation & secure login
Profile and CV management
Job search and filtering
Personalized recommendations (“Jobs for Me”)
Direct redirection to external job offer links

👨‍💼 Admin Features
Secure admin login
Launch scraping processes
Update database with new offers
View statistics and charts

🚀 Conclusion
This project combines web scraping, database management, desktop UI, and machine learning to deliver a smart, scalable, and user-centered job recommendation system tailored to the Moroccan job market.
