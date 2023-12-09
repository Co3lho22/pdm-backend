package fcup.pdm.myapp.model;


/**
 * The MovieLink class represents a link related to a movie, with attributes such as the link URL, resolution,
 * format, and hash value.
 */
public class MovieLink {
    private int movieId;
    private String link;
    private String resolution;
    private String format;
    private String hash;

    public MovieLink() {
    }

    /**
     * Constructor for the MovieLink class with parameters.
     *
     * @param link       The URL link associated with the movie.
     * @param resolution The resolution of the video link.
     * @param format     The format of the video link.
     * @param hash       The hash value of the video link.
     */
    public MovieLink(String link, String resolution, String format, String hash) {
        this.movieId = movieId;
        this.link = link;
        this.resolution = resolution;
        this.format = format;
        this.hash = hash;
    }

    /**
     * Set the movieId associated with the movie link.
     *
     * @param movieId The movieId for this link to set.
     */
    public void setMovieId(int movieId ) {
        this.movieId = movieId;
    }

    /**
     * Get the movieId associated with the movie link.
     *
     * @return The movieId for the link.
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Set the URL link associated with the movie.
     *
     * @param link The URL link to set.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Get the URL link associated with the movie.
     *
     * @return The URL link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Set the resolution of the video link.
     *
     * @param resolution The resolution to set.
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * Get the resolution of the video link.
     *
     * @return The resolution of the video.
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Set the format of the video link.
     *
     * @param format The format to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Get the format of the video link.
     *
     * @return The format of the video.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the hash value of the video link.
     *
     * @param hash The hash value to set.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get the hash value of the video link.
     *
     * @return The hash value of the video.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Override of the toString method to provide a string representation of the MovieLink object.
     *
     * @return A string representation of the MovieLink object.
     */
    @Override
    public String toString() {
        return "MovieLink{" +
                "link='" + link + '\'' +
                ", resolution='" + resolution + '\'' +
                ", format='" + format + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
