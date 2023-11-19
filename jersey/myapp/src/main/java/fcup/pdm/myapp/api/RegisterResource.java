package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.util.PasswordUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/register")
public class RegisterResource {
    private static final Logger logger = LogManager.getLogger(RegisterResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {

        // logger.debug("Attempting to register user: " + user.getUsername());

        UserDAO userDao = new UserDAO();
        user.setHashedPassword(PasswordUtil.hashPassword(user.getPassword()));

        if(userDao.userExists(user.getUsername())){
            return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
        }

        try {
            if (userDao.addUser(user)) {
                // Registration successful
                logger.info("Registration successful for user: " + user.getUsername());
                return Response.ok().entity("Registration Successful").build();
            } else {
                // Registration failed
                logger.info("Registration successful for user: " + user.getUsername());
                return Response.status(Response.Status.BAD_REQUEST).entity("Registration Failed").build();
            }
        }catch(Exception e){
            logger.warn(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when " +
                    "trying to register the user: " + user.getUsername()).build();
        }
    }
}
