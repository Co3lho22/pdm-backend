package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.model.User;

public class UserDAO {

    public User getUserByUsername(String username) {
        User user = null;
        try {
            Connection connection = DBConnection.getConnection();
            String query = "SELECT * FROM USERS WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setHashedPassword(rs.getString("hashed_password"));
                user.setEmail(rs.getString("email"));
                user.setDateCreated(rs.getTimestamp("date_created"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByUsernameAndPassword(String username, String hashedPassword) {
        try {
            Connection connection = DBConnection.getConnection();
            String query = "SELECT * FROM USERS WHERE username = ? AND hashed_password = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setHashedPassword(rs.getString("hashed_password"));
                user.setEmail(rs.getString("email"));
                user.setDateCreated(rs.getTimestamp("date_created"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean userExists(String username) {
        try {
            Connection connection = DBConnection.getConnection();
            String query = "SELECT COUNT(*) FROM USERS WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUserPerms(Connection connection, User user) {
        try{
            String query = "INSERT INTO USER_ROLE (user_id, role_id) VALUES (?, (SELECT id FROM ROLE WHERE name = 'user'))";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, user.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUser(User user) {
        try {
            Connection connection = DBConnection.getConnection();
            String query = "INSERT INTO USERS (username, hashed_password, email, country, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getHashedPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getCountry());
            ps.setString(5, user.getPhone());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                return addUserPerms(connection, user);
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

