package database;

import cinema.CinemaException;
import model.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private String databaseURL;
    private String user;
    private String password;

    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql;

    public Database() {

        try {
            Class.forName(JDBC_DRIVER);

            // TODO: 14.12.2018 jednolinijkowiec
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("database.json"));
            JSONObject jsonObject = (JSONObject) obj;

            String ip = (String) jsonObject.get("ip");
            String port = (String) jsonObject.get("port");
            String database = (String) jsonObject.get("database");
            databaseURL = "jdbc:mysql://" + ip + ":" + port + "/" + database;

            user = (String) jsonObject.get("username");
            password = (String) jsonObject.get("password");

            con = DriverManager.getConnection(databaseURL, user, password);

        } catch (ClassNotFoundException | SQLException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (con != null) con.setAutoCommit(true);
            if (ps != null) ps.close();
            if (rs != null) rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User registerUser(String username, String password) throws SQLException {
        sql = "INSERT INTO users (ID_USER, USERNAME, PASSWORD) VALUES (NULL, ?, ?)";
        User user = null;

        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int userId = rs.getInt(1);
            user = new User(userId, username, password);
        }
        close();

        return user;
    }

    public User loginUser(String username, String password) {
        sql = "select ID_USER from users where USERNAME = ? and PASSWORD = ?";
        User user = null;

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("ID_USER");
                user = new User(userId, username, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return user;
    }

    public Employee registerEmployee(String username, String password) throws CinemaException {
        sql = "INSERT INTO employees (ID_EMPLOYEE, USERNAME, PASSWORD) VALUES (NULL, ?, ?)";
        Employee employee = null;

        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int employeeId = rs.getInt(1);
                employee = new Employee(employeeId, username, password);
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == SQLErrorCode.DUPLICATE_ENTRY) {
                throw new CinemaException("Uzytkownik jest juz w bazie");
            } else {
                e.printStackTrace();
            }

        } finally {
            close();
        }

        return employee;
    }

    public Employee loginEmployee(String username, String password) {
        sql = "select ID_EMPLOYEE from employees where USERNAME = ? and PASSWORD = ?";
        Employee employee = null;

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                int employeeId = rs.getInt("ID_EMPLOYEE");
                employee = new Employee(employeeId, username, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return employee;
    }

    public boolean addMovie(Movie movie) throws CinemaException {
        sql = "INSERT INTO movies (ID_MOVIE, TITLE, DESCRIPTION, DURATION) VALUES (NULL, ?, ?, ?)";

        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getDuration());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int movieId = rs.getInt(1);

                addMovieDirectors(movieId, movie.getDirectors());
                addMovieActors(movieId, movie.getActors());
                addMovieGenres(movieId, movie.getGenres());

                con.commit();
            } else {
                con.rollback();
            }

        } catch (SQLException e) {
            rollback();

            if (e.getErrorCode() == 1644) {
                throw new CinemaException(e.getMessage());
            } else {
                e.printStackTrace();
            }
        } finally {
            close();
        }

        return false;
    }

    private void addMovieDirectors(int movieId, List<Director> directors) throws SQLException {
        int directorId;
        for (Director d : directors) {
            if (isDirectorInDatabase(d)) {
                directorId = getDirectorId(d);
            } else {
                directorId = addDirector(d);
            }

            addDirectorToMovie(directorId, movieId);
        }
    }

    private boolean isDirectorInDatabase(Director d) throws SQLException {
        sql = "select ID_DIRECTOR from directors where NAME = ? and SURNAME = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, d.getName());
        ps.setString(2, d.getSurname());

        rs = ps.executeQuery();
        return rs.next();
    }

    private int getDirectorId(Director d) throws SQLException {
        sql = "select ID_DIRECTOR from directors where NAME = ? and SURNAME = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, d.getName());
        ps.setString(2, d.getSurname());

        rs = ps.executeQuery();
        return rs.next() ? rs.getInt("ID_DIRECTOR") : 0;
    }

    private void addDirectorToMovie(int directorId, int movieId) throws SQLException {
        sql = "INSERT INTO movie_directors (ID_MOVIE, ID_DIRECTOR) VALUES (?, ?)";

        ps = con.prepareStatement(sql);
        ps.setInt(1, movieId);
        ps.setInt(2, directorId);
        ps.executeUpdate();
    }

    private int addDirector(Director d) throws SQLException {
        sql = "INSERT INTO directors (ID_DIRECTOR, NAME, SURNAME) VALUES (NULL, ?, ?)";

        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, d.getName());
        ps.setString(2, d.getSurname());

        ps.executeUpdate();
        rs = ps.getGeneratedKeys();

        return rs.next() ? rs.getInt(1) : 0;
    }

    private void addMovieActors(int movieId, List<Actor> actors) throws SQLException {
        int actorId;
        for (Actor a : actors) {
            if (isActorInDatabase(a)) {
                actorId = getActorId(a);
            } else {
                actorId = addActor(a);
            }

            addActorToMovie(actorId, movieId);
        }
    }

    private boolean isActorInDatabase(Actor a) throws SQLException {
        sql = "select ID_ACTOR from actors where NAME = ? and SURNAME = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, a.getName());
        ps.setString(2, a.getSurname());

        rs = ps.executeQuery();
        return rs.next();
    }

    private int getActorId(Actor a) throws SQLException {
        sql = "select ID_ACTOR from actors where NAME = ? and SURNAME = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, a.getName());
        ps.setString(2, a.getSurname());

        rs = ps.executeQuery();
        return rs.next() ? rs.getInt("ID_ACTOR") : 0;
    }

    public int addActor(Actor a) throws SQLException {
        sql = "INSERT INTO actors (ID_ACTOR, NAME, SURNAME) VALUES (NULL, ?, ?)";

        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, a.getName());
        ps.setString(2, a.getSurname());

        ps.executeUpdate();
        rs = ps.getGeneratedKeys();

        return rs.next() ? rs.getInt(1) : 0;
    }

    private void addActorToMovie(int actorId, int movieId) throws SQLException {
        sql = "INSERT INTO movie_actors (ID_MOVIE, ID_ACTOR) VALUES (?, ?)";

        ps = con.prepareStatement(sql);
        ps.setInt(1, movieId);
        ps.setInt(2, actorId);
        ps.executeUpdate();
    }

    private void addMovieGenres(int movieId, List<String> genres) throws SQLException {
        int genreId;
        for (String s : genres) {
            if (isGenreInDatabase(s)) {
                genreId = getGenreId(s);
            } else {
                genreId = addGenre(s);
            }

            addGenreToMovie(genreId, movieId);
        }
    }

    private boolean isGenreInDatabase(String genre) throws SQLException {
        sql = "select ID_GENRE from genres where NAME = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, genre);

        rs = ps.executeQuery();
        return rs.next();
    }

    private int getGenreId(String genre) throws SQLException {
        sql = "select ID_GENRE from genres where NAME = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, genre);

        rs = ps.executeQuery();
        return rs.next() ? rs.getInt("ID_GENRE") : 0;
    }

    private int addGenre(String genre) throws SQLException {
        sql = "INSERT INTO genres (ID_GENRE, NAME) VALUES (NULL, ?)";

        ps = con.prepareStatement(sql);
        ps.setString(1, genre);

        ps.executeUpdate();
        rs = ps.getGeneratedKeys();

        return rs.next() ? rs.getInt(1) : 0;
    }

    private void addGenreToMovie(int genreId, int movieId) throws SQLException {
        sql = "INSERT INTO movie_genres (ID_MOVIE, ID_GENRE) VALUES (?, ?)";

        ps = con.prepareStatement(sql);
        ps.setInt(1, movieId);
        ps.setInt(2, genreId);
        ps.executeUpdate();
    }

    public void addSeance(int movieId, int roomId, LocalDateTime dt) {
        sql = "INSERT INTO `seances` (`ID_SEANCE`, `ID_MOVIE`, `ID_ROOM`, `DATETIME`) VALUES (NULL, ?, ?, ?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, movieId);
            ps.setInt(2, roomId);
            ps.setString(3, dt.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Movie> getMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();

        sql = "select * from movies";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            movies.add(new Movie(
                    rs.getInt("ID_MOVIE"),
                    rs.getString("TITLE"),
                    rs.getString("DESCRIPTION"),
                    rs.getInt("DURATION"))
            );
        }

        sql = "SELECT m.ID_MOVIE, d.ID_DIRECTOR, d.NAME, d.SURNAME FROM directors as d, movie_directors as md" +
                ", movies as m WHERE d.ID_DIRECTOR = md.ID_DIRECTOR AND md.ID_MOVIE = m.ID_MOVIE";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        for (Movie m : movies) {
            List<Director> directors = new ArrayList<>();

            while (rs.next()) {
                if (m.getId() == rs.getInt("ID_MOVIE")) {
                    directors.add(new Director(
                            rs.getInt("ID_DIRECTOR"),
                            rs.getString("NAME"),
                            rs.getString("SURNAME"))
                    );
                } else {
                    m.setDirectors(directors);
                    rs.previous();
                    break;
                }
            }

            m.setDirectors(directors);
        }

        sql = "SELECT m.ID_MOVIE, a.ID_ACTOR, a.NAME, a.SURNAME FROM actors as a, movie_actors as ma" +
                ", movies as m WHERE a.ID_ACTOR = ma.ID_ACTOR AND ma.ID_MOVIE = m.ID_MOVIE";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();


        for (Movie m : movies) {
            List<Actor> actors = new ArrayList<>();

            while (rs.next()) {
                if (m.getId() == rs.getInt("ID_MOVIE")) {
                    actors.add(new Actor(
                            rs.getInt("ID_ACTOR"),
                            rs.getString("NAME"),
                            rs.getString("SURNAME"))
                    );
                } else {
                    m.setActors(actors);
                    rs.previous();
                    break;
                }
            }

            m.setActors(actors);
        }


        sql = "SELECT m.ID_MOVIE, g.NAME FROM genres as g" +
                ", movies as m, movie_genres as mg WHERE g.ID_GENRE = mg.ID_GENRE AND mg.ID_MOVIE = m.ID_MOVIE";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();


        for (Movie m : movies) {
            List<String> genres = new ArrayList<>();

            while (rs.next()) {
                if (m.getId() == rs.getInt("ID_MOVIE")) {
                    genres.add( rs.getString("NAME"));
                } else {
                    m.setGenres(genres);
                    rs.previous();
                    break;
                }
            }

            m.setGenres(genres);
        }

        return movies;
    }

    public Movie getMovie(int id) throws SQLException {
        Movie movie = null;

        sql = "select * from movies where id_movie = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (rs.next()) {
            movie = new Movie(
                    rs.getInt("ID_MOVIE"),
                    rs.getString("TITLE"),
                    rs.getString("DESCRIPTION"),
                    rs.getInt("DURATION"));
        }

        sql = "SELECT m.ID_MOVIE, d.ID_DIRECTOR, d.NAME, d.SURNAME FROM directors as d, movie_directors as md" +
                ", movies as m WHERE d.ID_DIRECTOR = md.ID_DIRECTOR AND md.ID_MOVIE = m.ID_MOVIE";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        List<Director> directors = new ArrayList<>();

        while (rs.next()) {
            if (movie.getId() == rs.getInt("ID_MOVIE")) {
                directors.add(new Director(
                        rs.getInt("ID_DIRECTOR"),
                        rs.getString("NAME"),
                        rs.getString("SURNAME"))
                );
            } else {
                movie.setDirectors(directors);
                rs.previous();
                break;
            }
        }

        movie.setDirectors(directors);

        sql = "SELECT m.ID_MOVIE, a.ID_ACTOR, a.NAME, a.SURNAME FROM actors as a, movie_actors as ma" +
                ", movies as m WHERE a.ID_ACTOR = ma.ID_ACTOR AND ma.ID_MOVIE = m.ID_MOVIE";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();


        List<Actor> actors = new ArrayList<>();

        while (rs.next()) {
            if (movie.getId() == rs.getInt("ID_MOVIE")) {
                actors.add(new Actor(
                        rs.getInt("ID_ACTOR"),
                        rs.getString("NAME"),
                        rs.getString("SURNAME"))
                );
            } else {
                movie.setActors(actors);
                rs.previous();
                break;
            }
        }

        movie.setActors(actors);


        sql = "SELECT m.ID_MOVIE, g.NAME FROM genres as g" +
                ", movies as m, movie_genres as mg WHERE g.ID_GENRE = mg.ID_GENRE AND mg.ID_MOVIE = m.ID_MOVIE";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        List<String> genres = new ArrayList<>();

        while (rs.next()) {
            if (movie.getId() == rs.getInt("ID_MOVIE")) {
                genres.add( rs.getString("NAME"));
            } else {
                movie.setGenres(genres);
                rs.previous();
                break;
            }
        }

        movie.setGenres(genres);

        return movie;
    }

    public List<Seance> getSeancesFor(LocalDate date) throws SQLException {   // LocalDate zmieniona na LocalDateTime
        List<Seance> seances = new ArrayList<>();

        sql = "select * from seances where CAST(datetime as DATE) = ?";

        ps = con.prepareStatement(sql);
        ps.setString(1, date.toString());
        rs = ps.executeQuery();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (rs.next()) {
            seances.add(new Seance(
                    rs.getInt("ID_SEANCE"),
                    rs.getInt("ID_MOVIE"),
                    LocalDateTime.parse(rs.getString("DATETIME"), format),
                    rs.getInt("ID_ROOM")
            ));
        }

        return seances;
    }

    public List<Room> getRooms()  {
        List<Room> rooms = new ArrayList<>();

        try {
            sql = "select * from rooms";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("ID_ROOM"),
                        rs.getString("NAME"),
                        rs.getInt("ROWS"),
                        rs.getInt("SEATS_IN_ROW")
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return rooms;
    }


    public List<TicketType> getTicketTypes() {
        List<TicketType> ticketTypes = new ArrayList<>();

        sql = "select * from ticket_types";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ticketTypes.add(new TicketType(
                        rs.getInt("ID_TICKET_TYPE"),
                        rs.getString("TICKET_TYPE"),
                        rs.getInt("PRICE")
                ));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return ticketTypes;
    }


    public boolean areSeancesColliding(Seance seance) {
        sql = "select * from seances, movies where id_room = ? and not (? >= DATE_ADD(DATETIME, INTERVAL duration MINUTE) OR (? < DATETIME AND DATE_ADD(?, INTERVAL (select duration from movies where id_movie = ?) MINUTE) <= DATETIME))";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, seance.getRoomId());
            ps.setString(2, seance.getDateTime().toString());
            ps.setString(3, seance.getDateTime().toString());
            ps.setString(4, seance.getDateTime().toString());
            ps.setInt(5, seance.getMovieId());
            rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }

        return false;
    }

    public List<Ticket> makeOrder(Order order) {
        sql = "INSERT INTO `orders` (`ID_ORDER`, `ID_SEANCE`, `ID_USER`, `ID_EMPLOYEE`) VALUES (NULL, ?, ?, ?)";

        List<Ticket> tickets = new ArrayList<>();

        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getSeanceId());

            if (order.getUserId() == 0) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, order.getUserId());
            }

            if (order.getEmployeeId() == 0) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, order.getEmployeeId());
            }

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                for (Ticket t : order.getTickets()) {
                    sql = "INSERT INTO `tickets` (`ID_TICKET`, `ID_SEANCE`, `ID_TICKET_TYPE`, `ROW`, `SEAT`) VALUES (NULL, ?, ?, ?, ?)";

                    ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, order.getSeanceId());
                    ps.setInt(2, t.getTicketTypeId());
                    ps.setInt(3, t.getRow());
                    ps.setInt(4, t.getSeat());
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();

                    if (rs.next()) {
                        int ticketId = rs.getInt(1);

                        sql = "INSERT INTO `order_tickets` (`ID_ORDER`, `ID_TICKET`) VALUES (?, ?)";

                        ps = con.prepareStatement(sql);
                        ps.setInt(1, orderId);
                        ps.setInt(2, ticketId);
                        ps.executeUpdate();

                        t.setId(ticketId);
                        tickets.add(t);
                    } else {
                        System.out.println("blad");
                        con.rollback();
                    }
                }

                con.commit();

            } else {
                System.out.println("blad");
                con.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            rollback();

        } finally {
            close();
        }

        return tickets;
    }

    private void rollback() {
        try {
            if (con != null) con.rollback();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void addTickets(int seanceId, TicketType ticketType, int amount) throws SQLException {
        sql = "INSERT INTO `tickets` (`ID_TICKET`, `ID_SEANCE`, `ID_TICKET_TYPE`, `ROW`, `SEAT`) VALUES (NULL, ?, ?, ?, ?)";

        ps = con.prepareStatement(sql);
    }


    public List<Movie> getMovieListByGenre(int genre) {
        sql = "select m.title from movies as m, movie_genres as mg, genres as g where mg.ID_MOVIE = m.ID_MOVIE and mg.ID_GENRE=?";
        List<Movie> movies = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, genre);
            rs = ps.executeQuery();

                while (rs.next()) {
                    movies.add(new Movie(
                            rs.getInt("ID_MOVIE"),
                            rs.getString("TITLE"),
                            rs.getString("DESCRIPTION"),
                            rs.getInt("DURATION"))
                    );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Seat> getTakenSeatsForSeance(Seance seance) {
        List<Seat> seats = new ArrayList<>();
        sql = "select row, seat from tickets where id_seance=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, seance.getSeanceId());

            rs = ps.executeQuery();
            while (rs.next()) {
                seats.add(new Seat(
                        rs.getInt("row"),
                        rs.getInt("seat")

                ));
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return seats;
    }

    public Room getRoom(int roomId) {
        sql = "select * from rooms where id_room=?";
        Room room = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomId);

            rs = ps.executeQuery();

            if (rs.next()) {
                room = new Room(roomId, rs.getString("name"), rs.getInt("rows"),
                        rs.getInt("seats_in_row"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }

        return room;
    }
}
