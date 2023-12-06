package fcup.pdm.myapp.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * The JwtUtil class provides methods for generating and verifying JSON Web Tokens (JWTs).
 */
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "u7Y8n9C0q1S2t3V4w5X6z7G8h9J0k1L2m3N4o5P6q7R8s9T0v1U2w3Y4z5A6b7C8";
    private static final long EXPIRATION_TIME = 900_000; // 15 minutes in milliseconds
    private static final long REFRESH_TOKEN_VALIDITY = 604800000; // 7 days in milliseconds

    /**
     * Generates a JWT token for the given username and roles.
     *
     * @param username The username to include in the token.
     * @param roles    The roles to include in the token.
     * @return The generated JWT token.
     */
    public static String generateToken(int userId, String username, List<String> roles) {
        String token = JWT.create()
                .withClaim("userId", userId)
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));

        logger.info("JWT token generated for user: {}", username);
        return token;
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token to extract the username from.
     * @return The username extracted from the token.
     * @throws JWTVerificationException If the token cannot be verified.
     */
    public static String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token);
            logger.info("JWT token verified successfully for user: {}", jwt.getSubject());
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            logger.error("Error verifying JWT token", e);
            throw e;
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token to extract the username from.
     * @return The userID extracted from the token.
     * @throws JWTVerificationException If the token cannot be verified.
     */
    public static int getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token);
            logger.info("JWT token verified successfully for user: {}", jwt.getSubject());
            return jwt.getClaim("userId").asInt();
        } catch (JWTVerificationException e) {
            logger.error("Error verifying JWT token", e);
            throw e;
        }
    }

    /**
     * Generates a refresh token for the given username and roles.
     *
     * @param username The username to include in the refresh token.
     * @param roles    The roles to include in the refresh token.
     * @return The generated refresh token.
     */
    public static String generateRefreshToken(int userId, String username, List<String> roles) {
        String refreshToken = JWT.create()
                .withClaim("userId", userId)
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .sign(Algorithm.HMAC512(SECRET_KEY));
        logger.info("Refresh token generated for user: {}", username);
        return refreshToken;
    }

    /**
     * Verifies a JWT token and returns the username if the token is valid.
     *
     * @param token The JWT token to verify.
     * @return The username extracted from the token.
     * @throws JWTVerificationException If the token cannot be verified.
     */
    public static String verifyToken(String token) throws JWTVerificationException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            logger.info("JWT token verified successfully for user: {}", jwt.getSubject());
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            logger.error("Error verifying JWT token", e);
            throw e;
        }
    }
}
