package fcup.pdm.myapp.model;

public class TokenRequest {
    private String refreshToken;

    public TokenRequest() {
    }

    public String getRefreshToken() { return refreshToken; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
