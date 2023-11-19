package fcup.pdm.myapp.dao;

        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.util.ArrayList;
        import java.util.List;

        import fcup.pdm.myapp.util.DBConnection;
        import fcup.pdm.myapp.model.Permission;

public class PermissionDAO {

    public List<Permission> getPermissionsByRoleId(int roleId) {
        List<Permission> permissions = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnection();
            String query = "SELECT p.* FROM PERMISSION p INNER JOIN ROLE_PERMISSION rp ON p.id = rp.permission_id WHERE rp.role_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("id"));
                permission.setName(rs.getString("name"));
                permissions.add(permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissions;
    }
}
