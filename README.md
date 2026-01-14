# 📌 Job & Internship Recommendation Platform (Java)

## 📖 Project Overview
This project is a **Java-based platform** designed to centralize, analyze, and recommend job and internship offers in Morocco.  
The application automatically collects job offers from multiple Moroccan recruitment websites, stores them in a database, and provides personalized recommendations based on the user’s profile and CV using **Machine Learning**.

---

## 🌐 Scraped Job Sources
The platform scrapes job offers from four Moroccan websites:

- **Rekrute**  
  🔗 [https://www.rekrute.com/](https://www.rekrute.com/)

- **Jobzyn**  
  🔗 [https://www.jobzyn.com/fr/jobs/maroc](https://www.jobzyn.com/fr/jobs/maroc)

- **Emploi.ma**  
  🔗 [https://www.emploi.ma/](https://www.emploi.ma/)

- **MarocAnnonces**  
  🔗 [https://www.marocannonces.com/categorie/309/Emploi/Offres-emploi.html](https://www.marocannonces.com/categorie/309/Emploi/Offres-emploi.html)

---

## 🔍 Scraping Strategy
The scraping approach depends on the website and its content type:  

- **Rekrute** and **Jobzyn** → **Selenium + ChromeDriver** because these sites load content dynamically with JavaScript, including pagination.  
- **Emploi.ma** and **MarocAnnonces** → **Jsoup** for faster and lighter HTML parsing since the content is static.  

This hybrid approach ensures **efficient and reliable data extraction**.

---

## 🛠 Technologies Used

### 🔹 Backend & Core
- **Java (JDK 21)** – Main programming language  
- **Swing (Java GUI)** – Desktop user interface  
- **FlatLaf** – Modern look and feel for Swing  
- **JDBC** – Database communication  
- **Maven** – Dependency and project management  

### 🔹 Web Scraping
- Selenium WebDriver  
- ChromeDriver  
- WebDriverManager  
- Jsoup  

### 🔹 Database
- MySQL  
- CVs stored as **BLOB**  
- Job offers unified from multiple sources  

### 🔹 Machine Learning
- Weka  
- Binary Classification  
- Random Forest algorithm  

### 🔹 CV Processing
- Apache PDFBox – Extract text from CVs (PDF)  

### 🔹 Email Service
- JavaMail API – Password recovery via email  

### 🔹 Visualization
- JFreeChart – Admin statistics  

### 🔹 Logging
- Logback  

---

## 🗄 Database Integration
A **MySQL database** is used to store:  
- Users and profiles  
- CVs (as BLOB)  
- Job offers scraped from all sources  

Offers from different websites are merged logically into a **unified dataset**.  
Each user can consult all offers and access the **“Jobs for Me”** feature.  

**Drive link for database and project files:**  
🔗 [https://drive.google.com/drive/folders/1Y7SqeNgZDu-C-t1iQgQUD6_ElclkbEsf?usp=sharing](https://drive.google.com/drive/folders/1Y7SqeNgZDu-C-t1iQgQUD6_ElclkbEsf?usp=sharing)

---

## 🤖 Machine Learning & Recommendation System

### 🔑 Objective
Provide personalized job recommendations based on the user’s:  
- Skills  
- Profile  
- Extracted CV content  

### ⚙ How It Works
1. **Skill Extraction**  
   Skills are extracted from:  
   - User profile  
   - Uploaded CV (PDF)  
   - Job descriptions  
   Implemented using `JobMatcher.extractSkills()`

2. **Feature Vector Construction**  
   Binary vectors represent:  
   - User skills  
   - Job skills  
   Implemented using `JobMatcher.buildFeatureVector()`

3. **Model**  
   - Binary classification: Relevant (1) / Not relevant (0)  
   - **Random Forest** (Weka)  
   - Pre-trained model (`model.model`) using `dataset.arff`

4. **Prediction**  
   - Predicts probability of compatibility  
   - Only offers with probability ≥ 0.35 are displayed

### 🧠 Machine Learning Dataset
- Training dataset: `dataset.arff`  
- Represents users and job offers as binary feature vectors  
- Used to train the Random Forest classifier in Weka

---

## 👤 User Features
- Account creation & secure login  
- Profile and CV management  
- Job search and filtering  
- Personalized recommendations (“Jobs for Me”)  
- Direct redirection to external job offer links

---

## 👨‍💼 Admin Features
- Secure admin login  
- Launch scraping processes  
- Update database with new offers  
- View statistics and charts

---

## 📊 UML Diagrams
The system design diagrams are available via Google Drive:  

### Class Diagram
![Class Diagram](https://drive.google.com/uc?export=view&id=1cbUsxxDlKq49Cf6Nyinh5XiqL334E45N)

### Use Case Diagram
![Use Case Diagram](https://drive.google.com/uc?export=view&id=1bh5dg6ka2SpeXZqVPlOLjXhYXerxwnpY)

### Activity Diagram
![Activity Diagram](https://drive.google.com/uc?export=view&id=1OhMNetxVF4QAJKAYLQF-VYXjB8F0aI_U)

### Sequence Diagram
![Sequence Diagram](https://drive.google.com/uc?export=view&id=1wTy7S7IrPIT-KFgTumKFJZNUM5vYX2s1)

---

## 🚀 Conclusion
This project combines **web scraping, database management, desktop UI, and machine learning** to deliver a **smart, scalable, and user-centered job recommendation system** tailored to the Moroccan job market.
