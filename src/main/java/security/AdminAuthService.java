package security;

import java.sql.*;

public class AdminAuthService {

    // Hardcoded admin credentials
    private static final String ADMIN_EMAIL = "admin@jobs.com";
    private static final String ADMIN_PASSWORD = "admin123";

    private Connection conn;

    // Constructor
    public AdminAuthService(Connection conn)
 {
        this.conn = conn;
    }

    /**
     * Returns:
     * "admin" -> if admin login
     * "user" -> if user login
     * null -> if login failed
     */
    public String login(String email, String password) {
        // 1️⃣ Check admin first
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            return "admin";
        }

        // 2️⃣ Check user in database
        try {
            String query = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password); // for production, hash passwords
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "user";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3️⃣ Login failed
        return null;
    }
}
