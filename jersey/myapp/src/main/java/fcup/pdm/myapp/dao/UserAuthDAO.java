package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserAuthDAO {
    private static final Logger logger = LogManager.getLogger(UserAuthDAO.class);


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
                    logger.info("Refresh token not valid with refresh token: {}", refreshToken);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("Error validating refresh token with refresh token: {}", refreshToken, e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRefreshToken(String username, String newRefreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE USER_AUTH SET refresh_token = ? WHERE " +
                     "user_id = (SELECT id FROM USERS WHERE username = ?)")){
            ps.setString(1, newRefreshToken);
            ps.setString(2, username);
            int rowsAffected = ps.executeUpdate();

            logger.info("Updated the refresh token successfully with refresh token: {}", newRefreshToken);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error updating the refresh token with refresh token: {}", newRefreshToken, e);
            return false;
        }
    }
    
    public boolean storeRefreshToken(int userId, String refreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement( "INSERT INTO USER_AUTH (user_id, refresh_token) " +
                     "VALUES (?, ?) ON DUPLICATE KEY UPDATE refresh_token = ?")){

            ps.setInt(1, userId);
            ps.setString(2, refreshToken);
            ps.setString(3, refreshToken);

            int rowsAffected = ps.executeUpdate();

            logger.info("Stored the refresh token successfully with refresh token: {}", refreshToken);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error storing the refresh token with refresh token: {}", refreshToken, e);
            return false;
        }
    }
}
