package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.model.Genre;
import fcup.pdm.myapp.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * The GenreDAO class provides methods to interact with the database for genre-related operations.
 * It handles retrieving genre information, verifying genre existence, and fetching genres associated with movies.
 */
public class GenreDAO {
    private static final Logger logger = LogManager.getLogger(GenreDAO.class);

    /**
     * Retrieves a list of all genres from the database.
     *
     * @return A list of Genre objects representing all genres.
     */
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String query = "SELECT * FROM GENRES";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(Integer.parseInt(rs.getString("id")));
                genre.setName(rs.getString("name"));
                genres.add(genre);
            }
        } catch (Exception e) {
            logger.error("Error returning all genres", e);
        }
        logger.info("Returned all genres successfully");
        return genres;
    }

    /**
     * Verifies if a genre with the given ID and name exists in the database.
     *
     * @param genre The Genre object to verify.
     * @return True if the genre exists; otherwise, false.
     */
    public boolean verifyGenreExists(Genre genre){
        String query = "SELECT * FROM GENRES WHERE id = ? AND name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, genre.getId());
            ps.setString(2, genre.getName());

            try (ResultSet rs = ps.executeQuery()) {
                logger.info("Successfully verified if genre exists with genre name: {}", genre.getName());
                return rs.next();
            }
        } catch (Exception e) {
            logger.error("Error verifying if genre exits with genre name: {}", genre.getName(), e);
        }
        return false;
    }

    /**
     * Retrieves the name of a genre by its ID.
     *
     * @param id The ID of the genre.
     * @return The name of the genre, or null if not found.
     */
    public String getGenreNameById(int id){
        String name = null;

        String query = "SELECT name FROM GENRES WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
                logger.info("Successfully got genre name by the genre id with genre name: {}", name);
                return name;
            }
        } catch (Exception e) {
            logger.error("Error retrieving the genre name by genre id with id: {}", id, e);
        }
        return name;
    }

    /**
     * Retrieves genres for a list of movie IDs.
     *
     * @param movieIds A list of movie IDs.
     * @return A map where keys are movie IDs, and values are lists of genre names associated with each movie.
     */
    public Map<Integer, List<String>> getGenresForMoviesId(List<Integer> movieIds) {
        Map<Integer, List<String>> movieGenresMap = new HashMap<>();
        if (movieIds == null || movieIds.isEmpty()) {
            return movieGenresMap;
        }

        String inSql = String.join(",", Collections.nCopies(movieIds.size(), "?"));
        String query = "SELECT mg.movie_id, g.name FROM GENRES g " +
                "JOIN MOVIE_GENRES mg ON g.id = mg.genre_id " +
                "WHERE mg.movie_id IN (" + inSql + ")";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            for (int i = 0; i < movieIds.size(); i++) {
                ps.setInt(i + 1, movieIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int movieId = rs.getInt("movie_id");
                    String genre = rs.getString("name");

                    movieGenresMap.computeIfAbsent(movieId, k -> new ArrayList<>()).add(genre);
                }
            }
        } catch (Exception e) {
            logger.error("Error retrieving genres from moviesIds with movieIds: {}", movieIds, e);
        }

        logger.info("Got genres from moviesIds successfully with movieIds: {}", movieIds);
        return movieGenresMap;
    }

}
