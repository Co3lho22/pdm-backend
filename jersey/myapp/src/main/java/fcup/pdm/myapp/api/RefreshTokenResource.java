package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserAuthDAO;
import fcup.pdm.myapp.model.TokenRequest;
import fcup.pdm.myapp.util.JwtUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/refresh")
public class RefreshTokenResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(TokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Validate the refresh token
        UserAuthDAO userAuthDAO = new UserAuthDAO();
        if (!userAuthDAO.isRefreshTokenValid(refreshToken)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid refresh token").build();
        }

        // Extract username from the refresh token
        String username = JwtUtil.getUsernameFromToken(refreshToken);

        // Generate a new access token
        String newAccessToken = JwtUtil.generateToken(username);

        // Optionally, generate a new refresh token
        String newRefreshToken = JwtUtil.generateRefreshToken(username);

        // Update the refresh token in the database
        userAuthDAO.updateRefreshToken(username, newRefreshToken);

        // Return the new tokens
        return Response.ok().entity("{\"accessToken\":\"" + newAccessToken + "\", \"refreshToken\":\"" + newRefreshToken + "\"}").build();
    }
}
