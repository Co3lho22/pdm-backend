package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.model.User;

public class UserDAO {

    public User getUserByUsername(String username) {
        User user = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM USERS WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setHashedPassword(rs.getString("hashed_password"));
                    user.setEmail(rs.getString("email"));
                    user.setDateCreated(rs.getTimestamp("date_created"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByUsernameAndPassword(String username, String hashedPassword) {
        User user = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM USERS " +
                     "WHERE username = ? AND hashed_password = ?")){
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setHashedPassword(rs.getString("hashed_password"));
                user.setEmail(rs.getString("email"));
                user.setDateCreated(rs.getTimestamp("date_created"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public int getUserIDByUsername(String username) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT id FROM USERS WHERE username = ?")){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Integer.parseInt(rs.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean removeUserByUsername(String username) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM USERS WHERE username = ?")){
            ps.setString(1, username);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean userExists(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM USERS " +
                     "WHERE username = ? OR email = ?")){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUserPerms(User user) {
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USER_ROLE (user_id, role_id) " +
                    "VALUES (?, (SELECT id FROM ROLE WHERE name = 'user'))")){
            ps.setLong(1, user.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO USERS " +
                     "(username, hashed_password, email, country, phone) VALUES (?, ?, ?, ?, ?)")){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getHashedPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getCountry());
            ps.setString(5, user.getPhone());

            int rowsAffected = ps.executeUpdate();
            int userId = getUserIDByUsername(user.getUsername());
            if(userId != -1){
                user.setId(userId);
            }else{
                removeUserByUsername(user.getUsername());
                return false;
            }

            if(rowsAffected > 0){
                return addUserPerms(user);
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getUserRoles(int userId) {
        List<String> roles = new ArrayList<>();

        try  (Connection connection = DBConnection.getConnection();
              PreparedStatement ps = connection.prepareStatement("SELECT r.name FROM ROLE r " +
                      "INNER JOIN USER_ROLE ur ON r.id = ur.role_id WHERE ur.user_id = ?")){

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return roles;
        }

        return roles;
    }
}

