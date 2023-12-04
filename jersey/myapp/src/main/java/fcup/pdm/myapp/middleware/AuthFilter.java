package fcup.pdm.myapp.middleware;

import fcup.pdm.myapp.dao.UserAuthDAO;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The AuthFilter class is a middleware filter responsible for handling authentication and authorization for incoming
 * HTTP requests. It checks the authorization header in the request and validates the provided token.
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {
    private static final Logger logger = LogManager.getLogger(AuthFilter.class);

    /**
     * Filters incoming HTTP requests and performs authentication and authorization checks.
     *
     * @param requestContext The container request context containing information about the incoming request.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        URI requestUri = requestContext.getUriInfo().getRequestUri();
        String path = requestUri.getPath();

        logger.info("Request URI: {}", path);

        if (path.endsWith("/login") || path.endsWith("/register") || path.endsWith("/refresh")) {
            logger.info("Skipping authentication for path: {}", path);
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header missing or invalid for path: {}", path);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Authorization" +
                    " header must be provided").build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            UserAuthDAO userAuthDAO = new UserAuthDAO();
            if (!userAuthDAO.isRefreshTokenValid(token)) {
                logger.warn("Invalid token provided for path: {}", path);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
            } else {
                logger.info("Token validated successfully for path: {}", path);
            }
        } catch (Exception e) {
            logger.error("Error validating token for path: {}", path, e);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
        }
    }
}
