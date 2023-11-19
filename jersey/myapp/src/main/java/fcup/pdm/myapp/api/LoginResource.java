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


@Path("/login")
public class LoginResource {
    private static final Logger logger = LogManager.getLogger(RegisterResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyLogin(User user) {
        UserDAO userDao = new UserDAO();
        User foundUser = userDao.getUserByUsername(user.getUsername());
        try{
            if (foundUser != null && PasswordUtil.checkPassword(user.getPassword(), foundUser.getHashedPassword())) {
                // Login successful
                List<String> roles = new ArrayList<>();
                roles.add(AppConstants.ROLE_USER);

                String accessToken = JwtUtil.generateToken(user.getUsername(), roles);
                String refreshToken = JwtUtil.generateRefreshToken(user.getUsername(), roles);

                // Store the refresh token in the database
                UserAuthDAO userAuthDAO = new UserAuthDAO();
                userAuthDAO.storeRefreshToken(foundUser.getId(), refreshToken);

                // Return both the access token and the refresh token
                return Response.ok().entity("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}").build();
            } else {
                // Login failed
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
            }
        } catch (Exception e){
            logger.warn(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when " +
                    "tried to verify the login").build();
        }
    }
}
