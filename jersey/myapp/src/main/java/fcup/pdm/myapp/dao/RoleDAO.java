package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoleDAO {
    private static final Logger logger = LogManager.getLogger(RoleDAO.class);

    public List<Role> getRolesByUserId(int userId) {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT r.* FROM ROLE r INNER JOIN USER_ROLE ur " +
                     "ON r.id = ur.role_id WHERE ur.user_id = ?")){
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }
        } catch (Exception e) {
            logger.error("Error retrieving roles by userId with userId: {}", userId, e);
        }
        logger.info("Got roles by userId successfully with userId: {}", userId);
        return roles;
    }
}
