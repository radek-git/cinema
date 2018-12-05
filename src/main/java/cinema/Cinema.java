package cinema;

import database.Database;
import database.SQLErrorCode;
import model.Employee;
import model.Movie;
import model.Seance;
import model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cinema {

    // JSON MODEL
    // http://www.objgen.com/json/models/v4Ubp

    private Database database;

    public Cinema() {
        database = new Database();

    }

    public User register(String username, String password) throws CinemaException {
        User user = null;

        try {
            user =  database.registerUser(username, password);
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLErrorCode.DUPLICATE_ENTRY) {
                throw new CinemaException("Uzytkownik jest juz w bazie");
            } else {
                e.printStackTrace();
            }
        }

        return user;
    }

    public User loginUser(String username, String password) throws CinemaException {

        return database.loginUser(username, password);
    }

    public Employee loginEmployee(String username, String password) throws CinemaException {

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


    //todo kupno biletu bez logowania
    //todo kupno biletu po zalogowaniu
    //todo stworzyć nowy plik który będzie przetrzymywał historię zakupów biletów
    // todo dodawanie nowego pracownika przez właściciela
    // todo usuwanie seansów przez właściciela
}
