package fcup.pdm.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import fcup.pdm.myapp.model.Film;
import fcup.pdm.myapp.util.DBConnection;



public class FilmDAO {

    public boolean addFilm(Film film) {
        String query = "INSERT INTO FILMS (title, genre, duration, rating, release_date, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, film.getTitle());
            ps.setString(2, film.getGenre());
            ps.setInt(3, film.getDuration());
            ps.setFloat(4, film.getRating());
            ps.setDate(5, new java.sql.Date(film.getRelease_date().getTime()));
            ps.setString(6, film.getDescription());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Film getFilmById(int id) {
        String query = "SELECT * FROM FILMS WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractFilmFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateFilm(Film film) {
        String query = "UPDATE FILMS SET title = ?, genre = ?, " +
                "duration = ?, rating = ?, release_date = ?, description = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, film.getTitle());
            ps.setString(2, film.getGenre());
            ps.setInt(3, film.getDuration());
            ps.setFloat(4, film.getRating());
            ps.setDate(5, new java.sql.Date(film.getRelease_date().getTime()));
            ps.setString(6, film.getDescription());
            ps.setInt(7, film.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFilm(int id) {
        String query = "DELETE FROM FILMS WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Film extractFilmFromResultSet(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setTitle(rs.getString("title"));
        film.setGenre(rs.getString("genre"));
        film.setDuration(rs.getInt("duration"));
        film.setRating(rs.getFloat("rating"));
        film.setRelease_date(rs.getDate("release_date"));
        film.setDescription(rs.getString("description"));
        return film;
    }

    public List<Film> getFilms(Optional<String> genre,
                               Optional<Float> minRating,
                               Optional<Float> maxRating,
                               Optional<Date> startDate,
                               Optional<Date> endDate,
                               Optional<Integer> limit) {

        List<Film> films = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM FILMS");

        List<Object> parameters = new ArrayList<>();
        boolean addAnd = false;

        // Genre condition
        if (genre.isPresent()) {
            queryBuilder.append(" WHERE genre = ?");
            parameters.add(genre.get());
            addAnd = true;
        }

        // Rating conditions
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

        // Release date range condition
        if (startDate.isPresent() && endDate.isPresent()) {
            queryBuilder.append(addAnd ? " AND " : " WHERE ");
            queryBuilder.append("release_date BETWEEN ? AND ?");
            parameters.add(new java.sql.Date(startDate.get().getTime()));
            parameters.add(new java.sql.Date(endDate.get().getTime()));
        }

        // Append LIMIT clause if present
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
                    films.add(extractFilmFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }
}