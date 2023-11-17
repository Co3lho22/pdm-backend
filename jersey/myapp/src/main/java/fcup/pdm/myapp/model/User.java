package fcup.pdm.myapp.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private String hashedPassword;
    private String email;
    private String country;
    private String phone;
    private Timestamp dateCreated;

    // Default Constructor
    public User() {
    }

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

    public String getPassword(){return password;}

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

    public String getCountry(){return country;}

    public void setCountry(String country){this.country = country;}

    public String getPhone(){return phone;}

    public void setPhone(String phone){this.phone = phone;}

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
