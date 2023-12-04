package fcup.pdm.myapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The PasswordUtil class provides methods for hashing and checking passwords using different algorithms.
 */
public class PasswordUtil {
    private static final Logger logger = LogManager.getLogger(PasswordUtil.class);

    /**
     * Hashes a password using BCrypt.
     *
     * @param plainPassword The plain password to be hashed.
     * @return The hashed password.
     */
    public static String hashPassword(String plainPassword) {
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        logger.info("Password hashed using BCrypt");
        return hashed;
    }

    /**
     * Checks if a plain password matches a hashed password using BCrypt.
     *
     * @param plainPassword   The plain password to be checked.
     * @param hashedPassword  The hashed password to compare against.
     * @return True if the plain password matches the hashed password, false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
        logger.info("Password check using BCrypt: {}", matches ? "Match" : "No Match");
        return matches;
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a hexadecimal string.
     */
    public static String hashPasswordSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            logger.info("Password hashed using SHA-256");
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error hashing password with SHA-256", e);
            return null;
        }
    }
}

