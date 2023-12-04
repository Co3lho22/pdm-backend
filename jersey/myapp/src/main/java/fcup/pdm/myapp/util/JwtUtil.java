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

public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "u7Y8n9C0q1S2t3V4w5X6z7G8h9J0k1L2m3N4o5P6q7R8s9T0v1U2w3Y4z5A6b7C8";
    private static final long EXPIRATION_TIME = 900_000; // 15 minutes in milliseconds
    private static final long REFRESH_TOKEN_VALIDITY = 604800000; // 7 days in milliseconds

    public static String generateToken(String username, List<String> roles) {
        String token = JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));

        logger.info("JWT token generated for user: {}", username);
        return token;
    }

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

    public static String generateRefreshToken(String username, List<String> roles) {
        String refreshToken = JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .sign(Algorithm.HMAC512(SECRET_KEY));
        logger.info("Refresh token generated for user: {}", username);
        return refreshToken;
    }

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
