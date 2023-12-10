package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import fcup.pdm.myapp.model.Movie;
import fcup.pdm.myapp.model.Genre;

import fcup.pdm.myapp.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The MovieDAO class provides methods to interact with the database for movie-related operations.
 * It handles retrieving movie information by ID and fetching a list of movies based on various criteria.
 */
public class MovieDAO {
    private static final Logger logger = LogManager.getLogger(AdminDAO.class);

    /**
     * Fetches genres for a given movie ID.
     *
     * @param movieId The ID of the movie.
     * @return A list of Genre objects associated with the movie.
     */
    private List<Genre> getGenresForMovie(int movieId) {
        List<Genre> genres = new ArrayList<>();
        String query = "SELECT g.id, g.name FROM GENRES g INNER JOIN MOVIE_GENRES mg " +
                "ON g.id = mg.genre_id WHERE mg.movie_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Genre genre = new Genre();
                    genre.setId(rs.getInt("id"));
                    genre.setName(rs.getString("name"));
                    genres.add(genre);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching genres for movieId: {}", movieId, e);
        } catch (Exception e) {
            logger.error("Error retrieving all movies", e);
        }
        return genres;
    }

    /**
     * Retrieves all movies from the database.
     *
     * @return A list of Movie objects representing all movies in the database.
     */
    public List<Movie> getAllMovieData() {
        String query = "SELECT id, title, duration, rating, release_date, description FROM MOVIES";
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie movie = extractMovieFromResultSet(rs);
                movie.setGenres(getGenresForMovie(movie.getId()));
                movies.add(movie);
            }
            logger.info("Retrieved all movies successfully");
        } catch (Exception e) {
            logger.error("Error retrieving all movies", e);
        }
        return movies;
    }

    /**
     * Extracts a Movie object from the ResultSet.
     *
     * @param rs The ResultSet containing movie data.
     * @return A Movie object representing the extracted movie.
     * @throws SQLException If a database access error occurs.
     */
    private Movie extractMovieFromResultSet(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setDuration(rs.getInt("duration"));
        movie.setRating(rs.getFloat("rating"));
        movie.setRelease_date(rs.getDate("release_date"));
        movie.setDescription(rs.getString("description"));

        logger.info("Extracted movie from result set successfully with movie title: {}", movie.getTitle());

        return movie;
    }

    /**
     * Retrieves a movie by its ID from the database.
     *
     * @param id The ID of the movie to retrieve.
     * @return A Movie object representing the retrieved movie, or null if not found.
     */
    public Movie getMovieById(int id) {
        String query = "SELECT * FROM MOVIES WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Got movie by id successfully with movie id: {}", id);
                return extractMovieFromResultSet(rs);
            }
        } catch (Exception e) {
            logger.error("Error getting movie by id with movie id: {}", id, e);
        }
        return null;
    }

    /**
     * Retrieves a list of movies based on optional filter criteria.
     *
     * @param minRating  The minimum movie rating (optional).
     * @param maxRating  The maximum movie rating (optional).
     * @param startDate  The start date for movie release (optional).
     * @param endDate    The end date for movie release (optional).
     * @param limit      The maximum number of movies to retrieve (optional).
     * @return A list of Movie objects matching the specified criteria.
     */
    public List<Movie> getMovies(Optional<Float> minRating,
                                 Optional<Float> maxRating,
                                 Optional<Date> startDate,
                                 Optional<Date> endDate,
                                 Optional<Integer> limit) {

        List<Movie> movies = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM MOVIES");

        List<Object> parameters = new ArrayList<>();
        boolean addAnd = false;

        if (minRating.isPresent()) {
            queryBuilder.append(addAnd ? " AND " : " WHERE ");
            queryBuilder.append("rating >= ?");
            parameters.add(minRating.get());
            addAnd = true;
        }
        if (maxRating.isPresent()) {
            queryBuilder.append(addAnd ? " AND " : " WHERE ");
            queryBuilder.append("rating <= ?");
            parameters.add(maxRating.get());
            addAnd = true;
        }

        if (startDate.isPresent() && endDate.isPresent()) {
            queryBuilder.append(addAnd ? " AND " : " WHERE ");
            queryBuilder.append("release_date BETWEEN ? AND ?");
            parameters.add(new java.sql.Date(startDate.get().getTime()));
            parameters.add(new java.sql.Date(endDate.get().getTime()));
            addAnd = true;
        }

        if (limit.isPresent()) {
            queryBuilder.append(" LIMIT ?");
            parameters.add(limit.get());
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(extractMovieFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            logger.error("Error retrieving movies", e);
        }
        logger.info("Got movies successfully");
        return movies;
    }

    /**
     * Retrieves a movie by its ID from the database.
     *
     * @param id The ID of the movie to retrieve.
     * @return A Movie object representing the retrieved movie, or null if not found.
     */
    public boolean movieAlreadyExits(int id) {
        String query = "SELECT * FROM MOVIES WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Got movie by id successfully with movie id: {}", id);
                return true;
            }
        } catch (Exception e) {
            logger.error("Error getting movie by id with movie id: {}", id, e);
        }
        return false;
    }

}