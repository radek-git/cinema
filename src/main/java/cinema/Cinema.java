package cinema;

import database.Database;
import database.SQLErrorCode;
import model.Employee;
import model.Movie;
import model.User;

import java.sql.SQLException;

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




    //todo kupno biletu bez logowania
    //todo kupno biletu po zalogowaniu
    //todo stworzyć nowy plik który będzie przetrzymywał historię zakupów biletów
    // todo dodawanie nowego pracownika przez właściciela
    // todo usuwanie seansów przez właściciela
}
