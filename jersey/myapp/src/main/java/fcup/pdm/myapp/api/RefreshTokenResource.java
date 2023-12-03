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

@Path("/refresh")
public class RefreshTokenResource {
    private static final Logger logger = LogManager.getLogger(RefreshTokenResource.class);

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

            userAuthDAO.updateRefreshToken(username, newRefreshToken);
            logger.info("Refresh token updated in the database for user: {}", username);

            logger.info("New access and refresh tokens generated for user: {}", username);
            return Response.ok().entity("{\"accessToken\":\"" + newAccessToken + "\", \"refreshToken\":\"" + newRefreshToken + "\"}").build();
        } catch (Exception e) {
            logger.error("Error occurred while refreshing token for user: {}", refreshToken, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when tried to get a new token").build();
        }
    }
}
