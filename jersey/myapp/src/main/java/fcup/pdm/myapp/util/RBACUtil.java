package fcup.pdm.myapp.util;

import fcup.pdm.myapp.dao.RoleDAO;
import fcup.pdm.myapp.dao.PermissionDAO;
import fcup.pdm.myapp.model.Role;
import fcup.pdm.myapp.model.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The RBACUtil class provides methods for Role-Based Access Control (RBAC) checks.
 */
public class RBACUtil {
    private static final Logger logger = LogManager.getLogger(RBACUtil.class);

    /**
     * Checks if a user has a specific permission.
     *
     * @param userId            The ID of the user to check.
     * @param requiredPermission The required permission to check.
     * @return True if the user has the required permission, false otherwise.
     */
    public static boolean hasPermission(int userId, String requiredPermission) {
        RoleDAO roleDAO = new RoleDAO();
        PermissionDAO permissionDAO = new PermissionDAO();

        List<Role> roles = roleDAO.getRolesByUserId(userId);
        for (Role role : roles) {
            List<Permission> permissions = permissionDAO.getPermissionsByRoleId(role.getId());
            for (Permission permission : permissions) {
                if (permission.getName().equals(requiredPermission)) {
                    logger.info("User ID: {} has required permission: {}", userId, requiredPermission);
                    return true;
                }
            }
        }
        logger.warn("User ID: {} does not have required permission: {}", userId, requiredPermission);
        return false;
    }
}
