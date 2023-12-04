package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserAuthDAO;
import fcup.pdm.myapp.model.TokenRequest;
import fcup.pdm.myapp.util.AppConstants;
import fcup.pdm.myapp.util.JwtUtil;
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
 * This class defines a RESTful web service for refreshing access tokens using a refresh token.
 */
@Path("/refresh")
public class RefreshTokenResource {
    private static final Logger logger = LogManager.getLogger(RefreshTokenResource.class);

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param request The token request containing the refresh token.
     * @return A response containing a new access token and refresh token if the refresh token is valid.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(TokenRequest request) {
        String refreshToken = request.getRefreshToken();

        UserAuthDAO userAuthDAO = new UserAuthDAO();

        try {
            if (!userAuthDAO.isRefreshTokenValid(refreshToken)) {
                logger.warn("Invalid refresh token for token: {}", refreshToken);
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid refresh token").build();
            }

            String username = JwtUtil.getUsernameFromToken(refreshToken);
            logger.info("Username extracted from refresh token: {}", username);

            List<String> roles = new ArrayList<>();
            roles.add(AppConstants.ROLE_USER);
            String newAccessToken = JwtUtil.generateToken(username, roles);

            String newRefreshToken = JwtUtil.generateRefreshToken(username, roles);

            if(userAuthDAO.updateRefreshToken(username, newRefreshToken)){

                logger.info("New access and refresh tokens generated for user: {}", username);
                return Response.ok().entity("{\"accessToken\":\"" + newAccessToken + "\", \"refreshToken\":\"" + newRefreshToken + "\"}").build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Was not able to update the refresh token in the db").build();
        } catch (Exception e) {
            logger.error("Error occurred while refreshing token for user: {}", refreshToken, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when tried to get a new token").build();
        }
    }
}
