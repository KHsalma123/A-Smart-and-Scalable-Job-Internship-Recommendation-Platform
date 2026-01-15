package com.example.jobs.maven_exemple;

import java.sql.*;


public class Database {
	private static final String URL = "jdbc:mysql://localhost:3308/scrapingDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";     

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    

  // ---------------- JOB INSERTION ----------------
    public static boolean insertJob(String titre, String lien, String entreprise, String lieu,
                                    String typeContrat, String experience) {
        String sql = "INSERT INTO jobzyn_offres " +
                     "(titre, lien, entreprise, lieu, type_contrat, experience) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titre);
            stmt.setString(2, lien);
            stmt.setString(3, entreprise);
            stmt.setString(4, lieu);
            stmt.setString(5, typeContrat);
            stmt.setString(6, experience);
            stmt.executeUpdate();
            System.out.println("✅ Offre ajoutée : " + titre);
            return true; // ✅ inserted successfully
        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate
            System.out.println("⏭️ Offre déjà existante : " + lien);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertRekrute(String titre, String lien, String entreprise, String lieu, String description,
                                        String dateDebut, String dateFin, String postes) {
        String sql = "INSERT INTO rekrute_offres " +
                     "(titre, lien, entreprise, lieu, description ,date_debut, date_fin, postes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titre);
            stmt.setString(2, lien);
            stmt.setString(3, entreprise);
            stmt.setString(4, lieu);
            stmt.setString(5, description);
            stmt.setString(6, dateDebut);
            stmt.setString(7, dateFin);
            stmt.setString(8, postes);
            stmt.executeUpdate();
            System.out.println("Offre Rekrute ajoutée : " + titre);
            return true; // ✅ inserted successfully
        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate
            System.out.println("⏭️ Offre déjà existante : " + lien);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


 // ---------------- JOB_OFFERS ----------------
    public static void insertJobOffer(String title, String company, String location,
                                      String description, String link, String source) {

        String sql = "INSERT INTO job_offers " +
                     "(title, company, location, description, link, source) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, company);
            stmt.setString(3, location);
            stmt.setString(4, description);
            stmt.setString(5, link);
            stmt.setString(6, source);

            stmt.executeUpdate();
            System.out.println("✅ Offre enregistrée : " + title);

        } catch (SQLIntegrityConstraintViolationException e) {
            // lien déjà existant
            System.out.println("⏭️ Offre déjà existante : " + link);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- UTILISATEURS ----------------
    public static boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
    }

    // Utilisateur avec CV en BLOB
    public static void registerUser(String fullName, String email, String password, String city, String skills, byte[] cvBytes) {
    	
    	// 🔹 Validation email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null, "Adresse e-mail invalide !");
            return;
        }

        // 🔹 Validation mot de passe
        if (!password.matches("^(?=.[A-Za-z])(?=.\\d)[A-Za-z\\d]{6,}$")) {
            JOptionPane.showMessageDialog(null, "Mot de passe invalide ! Il doit contenir au moins 6 caractères, dont une lettre et un chiffre.");
            return;
        }

        // 🔹 Validation nom complet
        if (!fullName.matches("^[A-Za-z ]+$")) {
            JOptionPane.showMessageDialog(null, "Nom invalide !");
            return;
        }

        // 🔹 Validation ville
        if (!city.matches("^[A-Za-z ]+$")) {
            JOptionPane.showMessageDialog(null, "Ville invalide !");
            return;
        }

        // 🔹 Validation skills (ex: lettres, chiffres, virgules)
        if (!skills.matches("^[A-Za-z0-9, ]*$")) {
            JOptionPane.showMessageDialog(null, "Compétences invalides !");
            return;
        }
        String sql = "INSERT INTO users(full_name,email,password,city,skills,cv) VALUES (?,?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, city);
            stmt.setString(5, skills);
            if(cvBytes != null) {
                stmt.setBytes(6, cvBytes);
            } else {
                stmt.setNull(6, Types.BLOB);
            }
            stmt.executeUpdate();
            System.out.println("Utilisateur ajouté : " + fullName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean loginUser(String email, String password) {
    	
    	// 🔹 Validation email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Email invalide !");
            return false; // Arrête ici si email invalide
        }

        // 🔹 Validation mot de passe (exemple : min 6 caractères, au moins une lettre et un chiffre)
        if (!password.matches("^(?=.[A-Za-z])(?=.\\d)[A-Za-z\\d]{6,}$")) {
            System.out.println("Mot de passe invalide !");
            return false; // Arrête ici si mot de passe invalide
        }
    	
        String sql = "SELECT id FROM users WHERE email=? AND password=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("Mot de passe mis à jour pour : " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final java.util.Map<String, String> resetCodes = new java.util.HashMap<>();
    public static void generateResetCode(String email) {
        String code = String.valueOf((int)(Math.random() * 9000) + 1000); 
        resetCodes.put(email, code);
        EmailSender.sendEmail(email, code);
    }

    public static boolean verifyResetCode(String email, String code) {
        return resetCodes.containsKey(email) && resetCodes.get(email).equals(code);
    }

    // ---------------- PROFIL UTILISATEUR ----------------
    public static String getUserName(String email) {
        String sql = "SELECT full_name FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) return rs.getString("full_name");
        } catch (Exception e) { e.printStackTrace(); }
        return "Utilisateur";
    }

    public static String getUserCity(String email) {
        String sql = "SELECT city FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) return rs.getString("city");
        } catch (Exception e) { e.printStackTrace(); }
        return "";
    }

    public static String getUserSkills(String email) {
        String sql = "SELECT skills FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) return rs.getString("skills");
        } catch (Exception e) { e.printStackTrace(); }
        return "";
    }

    // Récupérer CV depuis BLOB
    public static byte[] getUserCV(String email) {
        String sql = "SELECT cv FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) return rs.getBytes("cv");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Mettre à jour profil et CV
    public static void updateUserProfile(String email, String fullName, String city, String skills, byte[] cvBytes) {
        String sql = "UPDATE users SET full_name=?, city=?, skills=?, cv=? WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, city);
            stmt.setString(3, skills);
            if(cvBytes != null) {
                stmt.setBytes(4, cvBytes);
            } else {
                stmt.setNull(4, Types.BLOB);
            }
            stmt.setString(5, email);
            stmt.executeUpdate();
            System.out.println("Profil mis à jour pour " + email);
        } catch (Exception e){ e.printStackTrace(); }
    }

    // ---------------- RECHERCHE JOBS ----------------
    public static ResultSet searchAllJobs(String keyword) {
        String sql =
            "SELECT IFNULL(titre,'') AS titre, IFNULL(entreprise,'') AS entreprise, IFNULL(lieu,'') AS lieu, IFNULL(lien,'') AS lien FROM jobzyn_offres WHERE titre LIKE ? " +
            "UNION ALL " +
            "SELECT IFNULL(titre,'') AS titre, IFNULL(entreprise,'') AS entreprise, IFNULL(lieu,'') AS lieu, IFNULL(lien,'') AS lien FROM rekrute_offres WHERE titre LIKE ?"+
            "UNION ALL " +
            "SELECT IFNULL(title,'') AS titre, IFNULL(company,'') AS entreprise, IFNULL(location,'') AS lieu, IFNULL(link,'') AS lien FROM job_offers WHERE title LIKE ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);
            stmt.setString(3, kw);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ResultSet getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        return getConnection()
                .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                .executeQuery(sql);
    }
    


    // JOBS (FUSION)
    public static ResultSet getAllJobs() throws SQLException {
        String sql =
            "SELECT titre, entreprise, lieu, lien, '' AS description FROM jobzyn_offres " +
            "UNION ALL " +
            "SELECT titre, entreprise, lieu, lien, description FROM rekrute_offres " +
            "UNION ALL " +
            "SELECT title AS titre, company AS entreprise, location AS lieu, link AS lien, description FROM job_offers";

        return getConnection()
                .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                .executeQuery(sql);
    }


    









}
