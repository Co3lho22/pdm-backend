package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.util.PasswordUtil;
import fcup.pdm.myapp.util.JwtUtil;

import fcup.pdm.myapp.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/login")
public class LoginResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyLogin(User user) {
        UserDAO userDao = new UserDAO();
        User foundUser = userDao.getUserByUsername(user.getUsername());

        if (foundUser != null && PasswordUtil.checkPassword(user.getPassword(), foundUser.getHashedPassword())) {
            // Login successful
            String token = JwtUtil.generateToken(user.getUsername());
            return Response.ok().entity("{\"token\":\"" + token + "\"}").build();
        } else {
            // Login failed
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }
}
