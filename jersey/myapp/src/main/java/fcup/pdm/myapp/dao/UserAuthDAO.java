package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.util.JwtUtil;

public class UserAuthDAO {

    public boolean isRefreshTokenValid(String refreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_AUTH WHERE refresh_token = ?")){
            ps.setString(1, refreshToken);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                try {
                    JwtUtil.verifyToken(refreshToken);
                    return true; // Token is valid
                } catch (Exception e) {
                    return false; // Token is invalid or expired
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateRefreshToken(String username, String newRefreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE USER_AUTH SET refresh_token = ? WHERE " +
                     "user_id = (SELECT id FROM USERS WHERE username = ?)")){
            ps.setString(1, newRefreshToken);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void storeRefreshToken(int userId, String refreshToken) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement( "INSERT INTO USER_AUTH (user_id, refresh_token) " +
                     "VALUES (?, ?) ON DUPLICATE KEY UPDATE refresh_token = ?")){

            ps.setInt(1, userId);
            ps.setString(2, refreshToken);
            ps.setString(3, refreshToken);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
