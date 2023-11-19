package fcup.pdm.myapp.util;

import fcup.pdm.myapp.dao.RoleDAO;
import fcup.pdm.myapp.dao.PermissionDAO;
import fcup.pdm.myapp.model.Role;
import fcup.pdm.myapp.model.Permission;

import java.util.List;

public class RBACUtil {

    public static boolean hasPermission(int userId, String requiredPermission) {
        RoleDAO roleDAO = new RoleDAO();
        PermissionDAO permissionDAO = new PermissionDAO();

        List<Role> roles = roleDAO.getRolesByUserId(userId);
        for (Role role : roles) {
            List<Permission> permissions = permissionDAO.getPermissionsByRoleId(role.getId());
            for (Permission permission : permissions) {
                if (permission.getName().equals(requiredPermission)) {
                    return true;
                }
            }
        }
        return false;
    }
}
