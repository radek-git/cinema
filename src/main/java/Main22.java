import database.Database;
import model.Seance;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main22 {

    public static void main(String[] args) {
        Database database = new Database();


//        List<Director> directors = new ArrayList<>();
//        directors.add(new Director("Adam", "Spadam"));
//        directors.add(new Director("Marian", "Szpadel"));
//        directors.add(new Director("Jan", "Nezbędnny"));
//
//        List<String> genres = new ArrayList<>();
//        genres.add("Cartoon");
//
//
//        List<Actor> actors = new ArrayList<>();
//        actors.add(new Actor("Ktos", "tam"));
//        actors.add(new Actor("Rysiek", "Z Klanu"));
//        actors.add(new Actor("Pies", "Reksio"));
//
//        Movie movie = new Movie(
//                "Reksio", "hgghjhgfjg", 120, directors, genres, actors);
//
//        try {
//            database.addMovie(movie);
//        } catch (CinemaException e) {
//            e.printStackTrace();
//        }



//        LocalDateTime dt = LocalDateTime.of(2018, 11, 29, 10, 0);
//        database.addSeance(6, 3, dt);


//        try {
//            List<Movie> list = database.getMovies();
//
//            for (Movie m : list) {
//                System.out.println(m.getId());
//                for (Director d : m.getDirectors()) {
//                    System.out.println(d.getName() + " " + d.getSurname());
//                }
//
//                System.out.println();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        try {
            List<Seance> seances = database.getSeancesFor(LocalDate.now());

            seances.forEach(s -> System.out.println(s.getSeanceId()));

//            for (Seance s : seances) {
//                System.out.println(s.getSeanceId());
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
