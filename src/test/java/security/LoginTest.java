package security;

import junit.framework.TestCase;
import java.sql.Connection;
import java.sql.SQLException;
import com.example.jobs.maven_exemple.Database;

public class LoginTest extends TestCase {

    /**
     * Test de connexion avec les identifiants de l'admin
     */
    public void testValidAdminLogin() throws SQLException {
        Connection conn = Database.getConnection();
        AdminAuthService authService = new AdminAuthService(conn);

        String role = authService.login("admin@jobs.com", "admin123");
        // Doit renvoyer "admin"
        assertEquals("admin", role);
    }

    /**
     * Test de connexion avec des identifiants inexistants
     */
    public void testInvalidLogin() throws SQLException {
        Connection conn = Database.getConnection();
        AdminAuthService authService = new AdminAuthService(conn);

        String role = authService.login("inconnu@gmail.com", "motdepasse");
        // Doit renvoyer null car l'utilisateur n'existe pas
        assertNull(role);
    }

    /**
     * Test de connexion avec un utilisateur réel existant dans la table users
     */
    public void testValidUserLogin() throws SQLException {
        Connection conn = Database.getConnection();
        AdminAuthService authService = new AdminAuthService(conn);

        // On utilise un email et mot de passe existants dans ta table
        String role = authService.login("khaliqisalma@gmail.com", "1234");
        // Doit renvoyer "user"
        assertEquals("user", role);
    }

    /**
     * Optionnel : Test d'un autre utilisateur
     */
    public void testAnotherValidUserLogin() throws SQLException {
        Connection conn = Database.getConnection();
        AdminAuthService authService = new AdminAuthService(conn);

        String role = authService.login("mery@gmail.com", "123");
        // Doit renvoyer "user"
        assertEquals("user", role);
    }
}
