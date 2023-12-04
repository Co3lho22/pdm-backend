package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fcup.pdm.myapp.util.DBConnection;
import fcup.pdm.myapp.model.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The PermissionDAO class provides methods to interact with the database for permission-related operations.
 * It handles retrieving a list of permissions associated with a specific role.
 */
public class PermissionDAO {
    private static final Logger logger = LogManager.getLogger(PermissionDAO.class);

    /**
     * Retrieves a list of permissions associated with a specific role from the database.
     *
     * @param roleId The ID of the role to retrieve permissions for.
     * @return A list of Permission objects representing the permissions associated with the role.
     */
    public List<Permission> getPermissionsByRoleId(int roleId) {
        List<Permission> permissions = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT p.* FROM PERMISSION p " +
                     "INNER JOIN ROLE_PERMISSION rp ON p.id = rp.permission_id WHERE rp.role_id = ?")){
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("id"));
                permission.setName(rs.getString("name"));
                permissions.add(permission);
            }
        } catch (Exception e) {
            logger.error("Error getting permission by roleId with roleId: {}", roleId, e);
        }
        logger.info("Got permission by roleId successfully with roleId: {}", roleId);
        return permissions;
    }
}
