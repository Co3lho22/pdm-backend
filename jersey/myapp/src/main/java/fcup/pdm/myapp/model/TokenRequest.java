package fcup.pdm.myapp.model;

/**
 * The TokenRequest class represents a token request object containing a refresh token.
 */
public class TokenRequest {
    private String refreshToken;

    /**
     * Default constructor for the TokenRequest class.
     */
    public TokenRequest() {
    }

    /**
     * Get the refresh token.
     *
     * @return The refresh token.
     */
    public String getRefreshToken() { return refreshToken; }

    /**
     * Set the refresh token.
     *
     * @param refreshToken The refresh token to set.
     */
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
