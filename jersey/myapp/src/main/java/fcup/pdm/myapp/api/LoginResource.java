package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserAuthDAO;
import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.util.PasswordUtil;
import fcup.pdm.myapp.util.JwtUtil;
import fcup.pdm.myapp.util.AppConstants;

import fcup.pdm.myapp.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a RESTful web service for user login and authentication.
 */
@Path("/login")
public class LoginResource {
    private static final Logger logger = LogManager.getLogger(LoginResource.class);

    /**
     * Verifies user login credentials and generates access and refresh tokens upon successful login.
     *
     * @param user The user object containing username and password for login.
     * @return A response containing access and refresh tokens upon successful login or an error message for failed login.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyLogin(User user) {
        UserDAO userDao = new UserDAO();
        User foundUser = userDao.getUserByUsername(user.getUsername());
        try{
            if (foundUser != null && PasswordUtil.checkPassword(user.getPassword(), foundUser.getHashedPassword())) {

                List<String> roles = new ArrayList<>();
                roles.add(AppConstants.ROLE_USER);

                String accessToken = JwtUtil.generateToken(user.getId(), user.getUsername(), roles);
                String refreshToken = JwtUtil.generateRefreshToken(user.getUsername(), roles);

                UserAuthDAO userAuthDAO = new UserAuthDAO();
                userAuthDAO.storeRefreshToken(foundUser.getId(), refreshToken);

                logger.info("Login successful for user: {}", user.getUsername());

                return Response.ok().entity("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}").build();
            } else {
                logger.warn("Login failed for user: {}", user.getUsername());
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
            }
        } catch (Exception e){
            logger.error("Error verifying login for user: {}", user.getUsername(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when " +
                    "tried to verify the login").build();
        }
    }
}
