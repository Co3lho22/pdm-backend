package fcup.pdm.myapp.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private String hashedPassword;
    private String email;
    private Timestamp dateCreated;

    // Default Constructor
    public User() {
    }

    // Constructor with password (unhashed)
    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hashedPassword = null;
    }


    // Constructor with hashedPassword
    public User(int id, String username, String hashedPassword, String email, Timestamp dateCreated) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.dateCreated = dateCreated;
        this.password = null;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
