package fcup.pdm.myapp.util;

import fcup.pdm.myapp.dao.RoleDAO;
import fcup.pdm.myapp.dao.PermissionDAO;
import fcup.pdm.myapp.model.Role;
import fcup.pdm.myapp.model.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RBACUtil {
    private static final Logger logger = LogManager.getLogger(RBACUtil.class);

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
