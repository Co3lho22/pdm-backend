package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.util.PasswordUtil;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/register")
public class RegisterResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        UserDAO userDao = new UserDAO();
        user.setHashedPassword(PasswordUtil.hashPassword(user.getPassword()));

        if (userDao.addUser(user)) {
            // Registration successful
            return Response.ok().entity("Registration Successful").build();
        } else {
            // Registration failed
            return Response.status(Response.Status.BAD_REQUEST).entity("Registration Failed").build();
        }
    }
}
