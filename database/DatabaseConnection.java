package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import model.User;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/kutamalo_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("avatar")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public static boolean register(String username, String password, String email, String phone) {
        String query = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Register error: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateProfile(User user) {
        String query = "UPDATE users SET email = ?, phone = ?, avatar = ? WHERE id = ?";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getAvatar());
            pstmt.setInt(4, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update profile error: " + e.getMessage());
        }
        return false;
    }
    public static boolean updatePassword(int userId, String oldPassword, String newPassword) {
        // First check if old password matches
        String checkQuery = "SELECT id FROM users WHERE id = ? AND password = ?";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, oldPassword);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return false; // Old password doesn't match
                }
            }
        } catch (SQLException e) {
            System.err.println("Password check error: " + e.getMessage());
            return false;
        }

        // Now update
        String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update password error: " + e.getMessage());
        }
        return false;
    }
}
