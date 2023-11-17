package fcup.pdm.myapp.api;

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
        // Validate the refresh token...
        // If valid, generate a new access token and optionally a new refresh token
        String newAccessToken = JwtUtil.generateToken(username);
        // Optionally, generate a new refresh token
        return Response.ok().entity("{\"accessToken\":\"" + newAccessToken + "\"}").build();
    }
}
