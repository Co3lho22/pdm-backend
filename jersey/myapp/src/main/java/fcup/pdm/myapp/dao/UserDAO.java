package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The UserDAO class provides methods to interact with the database for user-related operations.
 * It handles retrieving user information, user existence checks, adding users, removing users, and managing user roles.
 */
public class UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    /**
     * Retrieves a user by their username from the database.
     *
     * @param username The username of the user to retrieve.
     * @return The User object representing the retrieved user or null if not found.
     */
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
            logger.error("Error retrieving user by userName with userName: {}", username, e);
        }

        logger.info("Got user by userName successfully with userName: {}", username);
        return user;
    }

    /**
     * Retrieves a user by their username and hashed password from the database.
     *
     * @param username       The username of the user to retrieve.
     * @param hashedPassword The hashed password of the user.
     * @return The User object representing the retrieved user or null if not found.
     */
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
            logger.error("Error retrieving user by username and password " +
                    "with userName: {} | and hashedPassword: {}", username, hashedPassword, e);
        }
        logger.info("Got user by userName and password successfully " +
                "with userName: {} | and hashedPassword: {}", username, hashedPassword);
        return user;
    }

    /**
     * Retrieves the user ID by username from the database.
     *
     * @param username The username of the user to retrieve the ID for.
     * @return The user ID or -1 if not found.
     */
    public int getUserIDByUsername(String username) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT id FROM USERS WHERE username = ?")){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("User ID retrieved for username: {}", username);
                return Integer.parseInt(rs.getString("id"));
            }
        } catch (Exception e) {
            logger.error("Error retrieving user ID by username: {}", username, e);
        }
        return -1;
    }

    /**
     * Removes a user by their username from the database.
     *
     * @param username The username of the user to remove.
     * @return true if the user is removed successfully; false otherwise.
     */
    public boolean removeUserByUsername(String username) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM USERS WHERE username = ?")) {
            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("User removed successfully with username: {}", username);
                return true;
            } else {
                logger.warn("No user found to remove with username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error removing user by username: {}", username, e);
        }
        return false;
    }

    /**
     * Checks if a user already exists in the database by username or email.
     *
     * @param user The User object to check for existence.
     * @return true if the user exists; false otherwise.
     */
    public boolean userExists(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM USERS " +
                     "WHERE username = ? OR email = ?")){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                logger.info("User existence check for username: {} | " +
                        "email: {} | exists: {}", user.getUsername(), user.getEmail(), exists);
                return exists;
            }
        } catch (Exception e) {
            logger.error("Error checking user existence for username: {} " +
                    "| email: {}", user.getUsername(), user.getEmail(), e);
        }
        return false;
    }

    /**
     * Adds user permissions to a user in the database.
     *
     * @param user The User object to add permissions for.
     * @return true if permissions are added successfully; false otherwise.
     */
    public boolean addUserPerms(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO USER_ROLE (user_id, role_id) " +
                     "VALUES (?, (SELECT id FROM ROLE WHERE name = 'user'))")) {
            ps.setLong(1, user.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("User permissions added for user ID: {}", user.getId());
                return true;
            } else {
                logger.warn("Failed to add user permissions for user ID: {}", user.getId());
            }
        } catch (Exception e) {
            logger.error("Error adding user permissions for user ID: {}", user.getId(), e);
        }
        return false;
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The User object representing the user to add.
     * @return true if the user is added successfully; false otherwise.
     */
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
                logger.info("User added successfully with username: {}", user.getUsername());
                return addUserPerms(user);
            }else{
                logger.warn("Failed to add user with username: {}", user.getUsername());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error adding user with username: {}", user.getUsername(), e);
            return false;
        }
    }

    /**
     * Retrieves the roles associated with a user from the database.
     *
     * @param userId The ID of the user to retrieve roles for.
     * @return A list of role names associated with the user.
     */
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
                logger.info("Roles retrieved for user ID: {}", userId);
            }
        } catch (Exception e) {
            logger.error("Error retrieving roles for user ID: {}", userId, e);
            return roles;
        }

        return roles;
    }

    /**
     * Updates user data using settings.
     *
     * @param user The User object containing updated user data.
     * @return true if the user data is successfully updated, false otherwise.
     */
    public boolean updateUserDataSetting(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE USERS " +
                     "SET country = ?, phone = ?, hashed_password = ? WHERE username = ?")) {

            user.setHashedPassword(PasswordUtil.hashPassword(user.getPassword()));
            ps.setString(1, user.getCountry());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getHashedPassword());
            ps.setString(4, user.getUsername());


            int rowsAffected = ps.executeUpdate();
            logger.info("Success updating user data using settings with username: {}", user.getUsername());
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error updating user data using settings with username: {}", user.getUsername(), e);
            return false;
        }
    }

    /**
     * Retrieves user settings data based on userId.
     *
     * @param userId The ID of the user.
     * @return User object containing user data if found, null otherwise.
     */
    public User getUserSettingData(int userId) {
        User user = null;
        String query = "SELECT username, email, country, phone FROM USERS WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCountry(rs.getString("country"));
                user.setPhone(rs.getString("phone"));
                logger.info("Success getting user settings data for userId: {}", userId);
            }
        } catch (Exception e) {
            logger.error("Error retrieving user settings data for userId: {}", userId, e);
        }
        return user;
    }
}

