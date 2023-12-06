package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The UserAuthDAO class provides methods to interact with the database for user authentication-related operations.
 * It handles validating refresh tokens, updating refresh tokens, and storing refresh tokens for users.
 */
public class UserAuthDAO {
    private static final Logger logger = LogManager.getLogger(UserAuthDAO.class);

    /**
     * Checks if a given refresh token is valid in the database.
     *
     * @param refreshToken The refresh token to validate.
     * @return true if the refresh token is valid; false otherwise.
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_AUTH WHERE refresh_token = ?")){
            ps.setString(1, refreshToken);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                try {
                    JwtUtil.verifyToken(refreshToken);
                    return true;
                } catch (Exception e) {
                    logger.warn("Refresh token not valid with refresh token: {}", refreshToken);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("Error validating refresh token with refresh token: {}", refreshToken, e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the refresh token associated with a user in the database.
     *
     * @param username       The username of the user.
     * @param newRefreshToken The new refresh token to update.
     * @return true if the refresh token is updated successfully; false otherwise.
     */
    public boolean updateRefreshToken(String username, String newRefreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE USER_AUTH SET refresh_token = ? WHERE " +
                     "user_id = (SELECT id FROM USERS WHERE username = ?)")){
            ps.setString(1, newRefreshToken);
            ps.setString(2, username);
            int rowsAffected = ps.executeUpdate();

            logger.info("Updated the refresh token successfully for user: {}", username);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error updating the refresh token for user: {}", username, e);
            return false;
        }
    }

    /**
     * Stores a refresh token for a user in the database or updates it if it already exists.
     *
     * @param userId        The ID of the user.
     * @param refreshToken  The refresh token to store or update.
     * @return true if the refresh token is stored or updated successfully; false otherwise.
     */
    public boolean storeRefreshToken(int userId, String refreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement( "INSERT INTO USER_AUTH (user_id, refresh_token) " +
                     "VALUES (?, ?) ON DUPLICATE KEY UPDATE refresh_token = ?")){

            ps.setInt(1, userId);
            ps.setString(2, refreshToken);
            ps.setString(3, refreshToken);

            int rowsAffected = ps.executeUpdate();

            logger.info("Stored the refresh token successfully for userId: {}", userId);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error storing the refresh token for userId: {}", userId, e);
            return false;
        }
    }
}
