package fcup.pdm.myapp.model;

import java.sql.Timestamp;

/**
 * The User class represents a user entity with various attributes.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String hashedPassword;
    private String email;
    private String country;
    private String phone;
    private Timestamp dateCreated;

    /**
     * Default constructor for the User class.
     */
    public User() {
    }

    /**
     * Get the unique user identifier.
     *
     * @return The user's identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the unique user identifier.
     *
     * @param id The user's identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the user's username.
     *
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the user's username.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password of the user.
     *
     * @return The hashed password.
     */
    public String getPassword(){return password;}

    /**
     * Set the password of the user.
     *
     * @param password The hashed password to set.
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Get the hashed password of the user.
     *
     * @return The hashed password.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Set the hashed password of the user.
     *
     * @param hashedPassword The hashed password to set.
     */
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    /**
     * Get the user's email address.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email address.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's country.
     *
     * @return The user's country.
     */
    public String getCountry(){return country;}

    /**
     * Set the user's country.
     *
     * @param country The country to set.
     */
    public void setCountry(String country){this.country = country;}

    /**
     * Get the user's phone number.
     *
     * @return The user's phone number.
     */
    public String getPhone(){return phone;}

    /**
     * Set the user's phone number.
     *
     * @param phone The phone number to set.
     */
    public void setPhone(String phone){this.phone = phone;}


    /**
     * Get the timestamp of when the user was created.
     *
     * @return The timestamp of user creation.
     */
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    /**
     * Set the timestamp of when the user was created.
     *
     * @param dateCreated The timestamp to set.
     */
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
