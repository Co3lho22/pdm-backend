package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.util.PasswordUtil;
import fcup.pdm.myapp.model.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/user")
public class UserResource {

    private UserDAO userDao = new UserDAO();
    private static final Logger logger = LogManager.getLogger(UserResource.class);

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        logger.info("Endpoint /register was called.");
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        boolean isRegistered = userDao.addUser(user);
        if (isRegistered) {
            return Response.ok().entity("User registered successfully!").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Registration failed!").build();
        }
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        logger.info("Endpoint /login was called.");
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        User existingUser = userDao.getUserByUsernameAndPassword(user.getUsername(), hashedPassword);
        if (existingUser != null) {
            return Response.ok().entity("User logged in successfully!").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials!").build();
        }
    }
}

