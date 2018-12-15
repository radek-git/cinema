package cinema;

import database.Database;
import database.SQLErrorCode;
import model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cinema {

    private Database database;

    public Cinema() {
        database = new Database();

    }

    public User register(String username, String password) {
        User user = null;

        try {
            user =  database.registerUser(username, password);
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLErrorCode.DUPLICATE_ENTRY) {

            } else {
                e.printStackTrace();
            }
        }
        return user;
    }

    public User loginUser(String username, String password) {
        return database.loginUser(username, password);
    }

    public Employee loginEmployee(String username, String password) {
        return database.loginEmployee(username, password);
    }

    public void addMovie(Movie movie) throws CinemaException {
        database.addMovie(movie);
    }

    public List<Seance> getSeancesFor(LocalDate date) {
        try {
            return database.getSeancesFor(date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Seance, Movie> getMovieDataForSeances(List<Seance> seances) {
        Map<Seance, Movie> map = new HashMap<>();

        seances.forEach(s -> {
            try {
                map.put(s, database.getMovie(s.getMovieId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    public List<Movie> getMovieList() {
        try {
            return database.getMovies();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addSeance(Seance seance) throws CinemaException {
        if (!database.areSeancesColliding(seance)) {
            database.addSeance(seance.getMovieId(), seance.getRoomId(), seance.getDateTime());
        } else {
            throw new CinemaException("Seanse ze soba koliduja");
        }
    }

    public List<Room> getRooms() {
        return database.getRooms();
    }

    public List<TicketType> getTicketTypes() {
        return database.getTicketTypes();
    }

    public List<Ticket> makeOrder(Order order) {
        return database.makeOrder(order);
    }

    public List<Seat> getTakenSeatsForSeance(Seance seance) {
        return database.getTakenSeatsForSeance(seance);
    }

    public List<Movie> getMovieListByGenre(int genre) {
        return database.getMovieListByGenre(genre);
    }

    public Room getRoom(int roomId) {
        return database.getRoom(roomId);
    }

}
