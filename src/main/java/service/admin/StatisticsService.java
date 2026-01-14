package service.admin;

import com.example.jobs.maven_exemple.Database;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatisticsService {
	public static Map<String, Integer> topCompaniesOverall(int limit) {
	    Map<String, Integer> result = new LinkedHashMap<>();

	    try (Connection conn = Database.getConnection()) {

	        // All tables
	        String[][] tables = {
	                {"jobzyn_offres", "entreprise", null},
	                {"rekrute_offres", "entreprise", null},
	                {"job_offers", "company", "emploi.ma"},
	                {"job_offers", "company", "MarocAnnonces"}
	        };

	        for (String[] t : tables) {
	            String table = t[0];
	            String column = t[1];
	            String source = t[2];

	            String query;
	            if (source == null) {
	                query = "SELECT " + column + " AS company, COUNT(*) AS count FROM " + table +
	                        " WHERE " + column + " IS NOT NULL AND TRIM(" + column + ") != '' " +
	                        "GROUP BY " + column;
	            } else {
	                query = "SELECT " + column + " AS company, COUNT(*) AS count FROM " + table +
	                        " WHERE source = ? AND " + column + " IS NOT NULL AND TRIM(" + column + ") != '' " +
	                        "GROUP BY " + column;
	            }

	            try (PreparedStatement stmt = conn.prepareStatement(query)) {
	                if (source != null) stmt.setString(1, source);
	                try (ResultSet rs = stmt.executeQuery()) {
	                    while (rs.next()) {
	                        String company = rs.getString("company").trim();
	                        int count = rs.getInt("count");
	                        result.put(company, result.getOrDefault(company, 0) + count); // sum counts if duplicate
	                    }
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // Sort descending and keep top N
	    return result.entrySet().stream()
	            .sorted((a, b) -> b.getValue() - a.getValue())
	            .limit(limit)
	            .collect(LinkedHashMap::new,
	                     (m, e) -> m.put(e.getKey(), e.getValue()),
	                     Map::putAll);
	}

	
	// In StatisticsService.java
	public static Map<String, Integer> topCompanies(int limit) {
	    Map<String, Integer> result = new LinkedHashMap<>();

	    try (Connection conn = Database.getConnection()) {

	        // Jobzyn
	        try (PreparedStatement stmt = conn.prepareStatement(
	                "SELECT entreprise AS company, COUNT(*) AS count FROM jobzyn_offres GROUP BY company ORDER BY count DESC LIMIT ?"
	        )) {
	            stmt.setInt(1, limit);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    result.put("Jobzyn.com - " + rs.getString("company"), rs.getInt("count"));
	                }
	            }
	        }

	        // Rekrute
	        try (PreparedStatement stmt = conn.prepareStatement(
	                "SELECT entreprise AS company, COUNT(*) AS count FROM rekrute_offres GROUP BY company ORDER BY count DESC LIMIT ?"
	        )) {
	            stmt.setInt(1, limit);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    result.put("Rekrute.com - " + rs.getString("company"), rs.getInt("count"));
	                }
	            }
	        }

	        // Emploi.ma
	        try (PreparedStatement stmt = conn.prepareStatement(
	                "SELECT company, COUNT(*) AS count FROM job_offers WHERE source='emploi.ma' GROUP BY company ORDER BY count DESC LIMIT ?"
	        )) {
	            stmt.setInt(1, limit);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    result.put("Emploi.ma - " + rs.getString("company"), rs.getInt("count"));
	                }
	            }
	        }

	        // MarocAnnonces
	        try (PreparedStatement stmt = conn.prepareStatement(
	                "SELECT company, COUNT(*) AS count FROM job_offers WHERE source='MarocAnnonces' GROUP BY company ORDER BY count DESC LIMIT ?"
	        )) {
	            stmt.setInt(1, limit);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    result.put("MarocAnnonces - " + rs.getString("company"), rs.getInt("count"));
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	
	// In StatisticsService.java
	public static Map<String, Map<String, Integer>> topCompaniesPerSite(int limit) {
	    Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

	    try (Connection conn = Database.getConnection()) {
	        // Sites: Jobzyn, Rekrute, Emploi.ma, MarocAnnonces
	        String[][] sites = {
	                {"Jobzyn.com", "jobzyn_offres", "entreprise"},
	                {"Rekrute.com", "rekrute_offres", "entreprise"},
	                {"Emploi.ma", "job_offers", "company"},
	                {"MarocAnnonces", "job_offers", "company"}
	        };

	        for (String[] siteData : sites) {
	            String siteName = siteData[0];
	            String table = siteData[1];
	            String column = siteData[2];
	            String query;

	            if (siteName.equals("Emploi.ma") || siteName.equals("MarocAnnonces")) {
	                query = "SELECT " + column + ", COUNT(*) AS count " +
	                        "FROM " + table + " WHERE source = ? GROUP BY " + column +
	                        " ORDER BY count DESC LIMIT " + limit;
	            } else {
	                query = "SELECT " + column + ", COUNT(*) AS count " +
	                        "FROM " + table + " GROUP BY " + column +
	                        " ORDER BY count DESC LIMIT " + limit;
	            }

	            try (PreparedStatement stmt = conn.prepareStatement(query)) {
	                if (siteName.equals("Emploi.ma") || siteName.equals("MarocAnnonces")) {
	                    stmt.setString(1, siteName.equals("Emploi.ma") ? "emploi.ma" : "MarocAnnonces");
	                }
	                try (ResultSet rs = stmt.executeQuery()) {
	                    Map<String, Integer> companies = new LinkedHashMap<>();
	                    while (rs.next()) {
	                        String company = rs.getString(column);
	                        int count = rs.getInt("count");
	                        if (company != null && !company.trim().isEmpty()) {
	                            companies.put(company, count);
	                        }
	                    }
	                    result.put(siteName, companies);
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

    // ===================== OFFERS BY SITE =====================
    public static Map<String, Integer> offersBySite() {
        Map<String, Integer> result = new LinkedHashMap<>();

        // Force all sites to appear
        result.put("Jobzyn", 0);
        result.put("Rekrute", 0);
        result.put("Emploi.ma", 0);
        result.put("MarocAnnonces", 0);

        try (Connection conn = Database.getConnection()) {

            // Jobzyn
            ResultSet rs1 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM jobzyn_offres");
            if (rs1.next()) result.put("Jobzyn", rs1.getInt(1));

            // Rekrute
            ResultSet rs2 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM rekrute_offres");
            if (rs2.next()) result.put("Rekrute", rs2.getInt(1));

            // Emploi.ma
            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM job_offers WHERE LOWER(source) = 'emploi.ma'");
            ResultSet rs3 = ps1.executeQuery();
            if (rs3.next()) result.put("Emploi.ma", rs3.getInt(1));

            // ✅ MarocAnnonces (FIXED 100%)
            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM job_offers WHERE LOWER(source) = 'marocannonces'");
            ResultSet rs4 = ps2.executeQuery();
            if (rs4.next()) result.put("MarocAnnonces", rs4.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // ===================== OFFERS BY CITY =====================
    public static Map<String, Integer> offersByCity() {
        Map<String, Integer> result = new LinkedHashMap<>();

        String sql = """
            SELECT 
                UPPER(TRIM(location)) AS city,
                COUNT(*) AS total
            FROM job_offers
            WHERE location IS NOT NULL
            GROUP BY city
            ORDER BY total DESC
            LIMIT 10
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("city"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // ===================== OFFERS OVER TIME =====================
    public static Map<String, Integer> offersOverTime() {
        Map<String, Integer> result = new LinkedHashMap<>();

        String sql = """
            SELECT DATE(created_at) AS day, COUNT(*) AS total
            FROM job_offers
            GROUP BY day
            ORDER BY day
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("day"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
