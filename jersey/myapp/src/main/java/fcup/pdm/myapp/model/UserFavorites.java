package fcup.pdm.myapp.model;

/**
 * The UserFavorites class represents a relationship between a user and their favorite movies.
 */
public class UserFavorites {
    private int user_id;
    private int movie_id;

    /**
     * Default constructor for the UserFavorites class.
     */
    public UserFavorites(){}

    /**
     * Get the unique identifier of the user.
     *
     * @return The user's identifier.
     */
    public int getUser_id(){ return user_id; }

    /**
     * Set the unique identifier of the user.
     *
     * @param user_id The user's identifier to set.
     */
    public void setUser_id(int user_id){ this.user_id = user_id; }

    /**
     * Get the unique identifier of the favorite movie.
     *
     * @return The movie's identifier.
     */
    public int getMovie_id(){ return movie_id; }

    /**
     * Set the unique identifier of the favorite movie.
     *
     * @param movie_id The movie's identifier to set.
     */
    public void setMovie_id(int movie_id){ this.movie_id = movie_id; }
}
