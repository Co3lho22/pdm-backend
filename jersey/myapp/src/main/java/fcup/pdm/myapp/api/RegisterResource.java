package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.util.PasswordUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * This class defines a RESTful web service for user registration.
 */
@Path("/register")
public class RegisterResource {
    private static final Logger logger = LogManager.getLogger(RegisterResource.class);

    /**
     * Registers a new user with the provided user information.
     *
     * @param user The user object containing registration details.
     * @return A response indicating whether the registration was successful or failed.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {

        UserDAO userDao = new UserDAO();
        user.setHashedPassword(PasswordUtil.hashPassword(user.getPassword()));

        if(userDao.userExists(user)){
            return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
        }

        try {
            if (userDao.addUser(user)) {
                logger.info("Registration successful for user: " + user.getUsername());

                user.setId(userDao.getUserIDByUsername(user.getUsername()));

                String jsonResponse = String.format("{\"message\":\"Registration Successful\"," +
                        " \"userId\":%d}", user.getId());
                return Response.ok().entity(jsonResponse).build();
            } else {
                logger.info("Registration failed for user: " + user.getUsername());
                return Response.status(Response.Status.BAD_REQUEST).entity("Registration Failed").build();
            }
        }catch(Exception e){
            logger.warn(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when " +
                    "trying to register the user: " + user.getUsername()).build();
        }
    }
}
