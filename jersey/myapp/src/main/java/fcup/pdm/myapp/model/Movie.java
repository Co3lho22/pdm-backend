package fcup.pdm.myapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Movie class represents a movie entity with various attributes such as ID, title, genre, duration, rating,
 * release date, and description.
 */
public class Movie {
    private int id;
    private String title;
    private int duration;  // In minutes
    private float rating;
    private Date release_date;
    private String description;

    /**
     * Default constructor for the Movie class.
     */
    public Movie() {
        this.id = -1;
        this.title = null;
        this.duration = -1;
        this.rating = -1.0f;
        this.release_date = null;
        this.description = null;
    }

    /**
     * Get the title of the movie.
     *
     * @return The title of the movie.
     */
    public String getTitle() { return title; }

    /**
     * Set the title of the movie.
     *
     * @param title The title of the movie.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Get the ID of the movie.
     *
     * @return The unique identifier of the movie.
     */
    public int getId(){ return id; }

    /**
     * Set the ID of the movie.
     *
     * @param id The unique identifier of the movie.
     */
    public void setId(int id){ this.id = id; }

    /**
     * Get the duration of the movie in minutes.
     *
     * @return The duration of the movie.
     */
    public int getDuration(){ return duration; }

    /**
     * Set the duration of the movie in minutes.
     *
     * @param duration The duration of the movie.
     */
    public void setDuration(int duration){ this.duration = duration; }

    /**
     * Get the rating of the movie.
     *
     * @return The rating of the movie.
     */
    public float getRating(){ return rating; }

    /**
     * Set the rating of the movie.
     *
     * @param rating The rating of the movie.
     */
    public void setRating( float rating){ this.rating = rating; }

    /**
     * Get the release date of the movie.
     *
     * @return The release date of the movie.
     */
    public Date getRelease_date(){ return release_date; }

    /**
     * Set the release date of the movie.
     *
     * @param release_date The release date of the movie.
     */
    public void setRelease_date(Date release_date){ this.release_date = release_date; }

    /**
     * Get the description of the movie.
     *
     * @return A brief description of the movie.
     */
    public String getDescription(){ return description; }

    /**
     * Set the description of the movie.
     *
     * @param description A brief description of the movie.
     */
    public void setDescription(String description){ this.description = description; }

    /**
     * Checks if the movie object is complete with valid attributes.
     *
     * @return True if the movie is complete and valid, false otherwise.
     */
    public boolean isMovieCompleteToAdd(){
        if (title == null || title.isEmpty()) return false;
        if (duration <= 0) return false;
        if (rating < 0) return false;
        if (release_date == null) return false;
        if (description == null || description.isEmpty()) return false;
        return true;
    }

    public boolean isMovieCompleteToUpdate(){
        if (id < 0) return false;
        if (title == null || title.isEmpty()) return false;
        if (duration <= 0) return false;
        if (rating < 0) return false;
        if (release_date == null) return false;
        if (description == null || description.isEmpty()) return false;
        return true;
    }
}
