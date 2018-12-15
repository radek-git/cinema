import cinema.Cinema;
import cinema.CinemaException;
import model.*;
import util.TimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    enum Menu {
        MAIN, CLIENT, EMPLOYEE, USER
    }

    private static Scanner sc;
    private static Menu menu;

    private static Cinema cinema;
    private static Account currentUser;

    public static void main(String[] args) {

        sc = new Scanner(System.in);
        menu = Menu.MAIN;
        cinema = new Cinema();


        while (true) {
            fakeClearScreen();
            printASCIIArt();
            handleMenu();
        }
    }

    private static void fakeClearScreen() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    private static void printASCIIArt() {
        System.out.print(" _____ _                            \n" +
                "/  __ (_)                           \n" +
                "| /  \\/_ _ __   ___ _ __ ___   __ _ \n" +
                "| |   | | '_ \\ / _ \\ '_ ` _ \\ / _` |\n" +
                "| \\__/\\ | | | |  __/ | | | | | (_| |\n" +
                " \\____/_|_| |_|\\___|_| |_| |_|\\__,_|\n\n");
    }

    private static void handleMenu() {
        switch (menu) {
            case MAIN: handleMainMenu(); break;
            case CLIENT: handleClientMenu(); break;
            case EMPLOYEE: handleOwnerMenu(); break;
            case USER: handleUserMenu(); break;
            default: break;
        }
    }

    private static void handleMainMenu() {
        System.out.println("1. Jestem Klientem.");
        System.out.println("2. Jestem Wlascicielem.");
        System.out.println("3. Zakoncz.");

        switch (sc.nextInt()) {
            case 1: menu = Menu.CLIENT; break;
            case 2: handleLogin(Menu.EMPLOYEE); break;
            case 3: exit(); break;
            default: break;
        }
    }

    private static void handleClientMenu() {
        System.out.println("1. Zarejestruj sie.");
        System.out.println("2. Zaloguj sie.");
        System.out.println("3. Kup bilet na miejscu.");
        System.out.println("4. Wstecz.");

        switch (sc.nextInt()) {
            case 1: handleRegisterUser(); break;
            case 2: handleLogin(Menu.USER); break;
            case 3:
                // TODO: 13.12.2018 uzupełnić
                break;

            case 4: menu = Menu.MAIN; break;
            default: break;
        }
    }

    private static void handleOwnerMenu() {
        System.out.println("1. Dodaj nowy film.");
        System.out.println("2. Dodaj nowy seans.");
        System.out.println("3. Wyloguj.");

        switch (sc.nextInt()) {
            case 1: handleAddNewMovie(); break;
            case 2: handleAddNewSeance(); break;
            case 3: logout(); break;
            default: break;
        }
    }

    private static void handleUserMenu() {
        System.out.println("1. Repertuar.");
        System.out.println("2. Szukaj wg gatunku.");
        System.out.println("3. Wyloguj.");

        switch (sc.nextInt()) {
            case 1: handleCinemaSchedule(); break;
            case 2: handleShowMovieByGenre(); break;
            case 3: logout(); break;
            default: break;
        }
    }

    private static void handleLogin(Menu m) {
        System.out.print("Podaj login: ");
        String username = sc.next();

        System.out.print("Podaj haslo: ");
        String password = sc.next();

        if (m == Menu.USER) {
            cinema.loginUser(username, password);
        } else {
            currentUser = cinema.loginEmployee(username, password);
        }

        if (currentUser != null) {
            System.out.println("Zalogowano pomyslnie.");
            menu = Menu.USER;
        } else {
            System.out.println("Zle dane logowania");
        }

        TimeUtils.sleep(1);
    }

    private static void handleRegisterUser() {
        System.out.print("Podaj nowy login: ");
        String username = sc.next();

        System.out.print("Podaj nowe haslo: ");
        String password = sc.next();

        currentUser = cinema.register(username, password);
        if (currentUser != null) {
            System.out.println("Zarejestrowano pomyslnie.");
            menu = Menu.USER;
        } else {
            System.out.println("Uzytkownik juz istnieje");
        }

        TimeUtils.sleep(1);
    }

    private static void handleAddNewMovie() {
        System.out.print("Podaj tytul: ");
        String title = sc.next();

        List<Director> directors = new ArrayList<>();
        String[] directorData;
        while (true) {
            System.out.print("Podaj rezysera:");

            directorData = sc.nextLine().split(" ");
            directors.add(new Director(directorData[0], directorData[1]));

            System.out.println("wincyj? (t/n)");
            if (sc.next().charAt(0) == 'n') {
                break;
            }
        }

        List<Actor> actors = new ArrayList<>();
        String[] actorData;

        while (true) {
            System.out.print("Podaj obsadę:");
            actorData = sc.nextLine().split(" ");
            actors.add(new Actor(actorData[0], actorData[1]));
            System.out.println("wincyj? (t/n)");
            if (sc.next().charAt(0) == 'n') {
                break;
            }
        }

        List<String> genres = new ArrayList<>();
        System.out.print("Podaj gatunek: ");

        while (true) {
            System.out.print("Podaj gatunek:");
            genres.add(sc.nextLine());
            System.out.println("wincyj? (t/n)");
            if (sc.next().charAt(0) == 'n') {
                break;
            }
        }

        System.out.print("Podaj opis: ");
        String description = sc.next();

        System.out.print("Podaj czas trwania filmu: ");
        int duration = sc.nextInt();

        Movie movie = new Movie(title, description, duration, directors, genres, actors);
        try {
            cinema.addMovie(movie);
        } catch (CinemaException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleAddNewSeance() {
        List<Movie> movies = cinema.getMovieList();
        Optional<Movie> optionalMovie = Optional.empty();
        while (!optionalMovie.isPresent()) {
            movies.forEach(movie -> System.out.println(movie.getId() + " " + movie.getTitle()));

            System.out.println("Wybierz film: ");
            int movieId = sc.nextInt();

            optionalMovie = movies.stream().filter(movie -> movie.getId() == movieId).findFirst();
        }

        List<Room> rooms = cinema.getRooms();
        Optional<Room> optionalRoom = Optional.empty();
        while (!optionalRoom.isPresent()) {
            rooms.forEach(room -> System.out.println(room.getId()+" "+room.getName()));

            System.out.println("Podaj nr sali: ");
            int roomNr = sc.nextInt();

            optionalRoom = rooms.stream().filter(room -> room.getId() == roomNr).findFirst();
        }

        String datePattern = "dd.MM.yyyy";
        System.out.println("Podaj datę: " + datePattern);
        LocalDate date = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern(datePattern));

        String timePattern = "hh:mm";
        System.out.println("Podaj godzinę rozpoczęcia : "+ timePattern);
        LocalTime time = LocalTime.parse(sc.next(), DateTimeFormatter.ISO_LOCAL_TIME);

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        try {
            cinema.addSeance(new Seance(
                    optionalMovie.get().getId(),
                    dateTime,
                    optionalRoom.get().getId()
            ));

            System.out.println("Dodano seans");

        } catch (CinemaException e) {
            System.out.println(e.getMessage());
        }

        TimeUtils.sleep(1);
    }


    private static void handleCinemaSchedule() {
        System.out.print("1. Dziś");
        System.out.print("2. Jutro");
        System.out.print("3. Pojutrze");

        int userChoice = sc.nextInt();

        List<Seance> seances = cinema.getSeancesFor(LocalDate.now().plusDays(userChoice-1));
        Map<Seance, Movie> map = cinema.getMovieDataForSeances(seances);

        map.forEach((s, m) -> System.out.println(s.getSeanceId() + " " + m.getTitle() + " " + s.getDateTime()));

        Optional<Seance> optionalSeance = Optional.empty();
        while (!optionalSeance.isPresent()) {
            System.out.print("Wybierz id seansu: ");

            int seanceId = sc.nextInt();
            optionalSeance = seances.stream().filter(s -> s.getSeanceId() == seanceId).findFirst();
        }

        Order order = new Order(optionalSeance.get().getSeanceId(), currentUser.getId(), 0);// TODO: 14.12.2018

        List<TicketType> ticketTypes = cinema.getTicketTypes();
        ticketTypes.forEach(ticketType -> System.out.println(ticketType.getTypeId() + " " + ticketType.getType() + " " + ticketType.getPrice()));

        Room room = cinema.getRoom(optionalSeance.get().getRoomId());
        List<Seat> seatsTaken = cinema.getTakenSeatsForSeance(optionalSeance.get());

        char[][] seats = new char[room.getRows()][room.getSeats()];
        for (int i = 0; i < room.getRows(); i++) {
            for (int j = 0; j < room.getSeats(); j++) {
                seats[i][j] = ' ';
            }
        }

        for (Seat s : seatsTaken) {
            seats[s.getRow()-1][s.getSeat()-1] = 'Z'; // -1 ponieważ indeksowanie w tablicy jest od 0
        }

        Optional<TicketType> optionalTicketType;
        while (true) {
            optionalTicketType = Optional.empty();
            while (!optionalTicketType.isPresent()) {
                System.out.print("Podaj nr biletu: ");

                int ticketTypeNumber = sc.nextInt();
                optionalTicketType = ticketTypes.stream().filter(tt -> tt.getTypeId() == ticketTypeNumber).findFirst();
            }

            System.out.println("Wybierz miejsce:");


            System.out.print("  ");
            for (int i = 0; i < room.getSeats(); i++) {
                System.out.print(i + 1 + "   ");
            }

            System.out.println();

            for (int i = 0; i < room.getRows(); i++) {
                System.out.print(i+1 + " ");

                for (int j = 0; j < room.getSeats(); j++) {
                    System.out.print("[" + seats[i][j] + "] ");
                }

                System.out.println();
            }

            int row;
            int seat;
            while (true) {
                System.out.print("Wybierz miejsce (rzad, miejsce): ");
                row = sc.nextInt();
                seat = sc.nextInt();

                if (seats[row-1][seat-1] == 'Z') {
                    System.out.println("Miejsce jest zajete");
                } else {
                    seats[row-1][seat-1] = 'Z';
                    break;
                }
            }

            TicketType ticketType = optionalTicketType.get();
            order.addTicket(new Ticket(optionalSeance.get().getSeanceId(), ticketType.getTypeId(), row, seat));

            System.out.println("Czy chcesz kupic kolejny bilet? (t/n) ");
            if (sc.next().charAt(0) == 'n') break;
        }

        List<Ticket> tickets = cinema.makeOrder(order);
        tickets.forEach(t -> System.out.println(t.toString()));
    }

    // TODO: 14.12.2018
    private static void handleShowMovieByGenre() {
        System.out.println("1. Komedia.");
        System.out.println("2. Dramat.");
        System.out.println("3. Horror.");
        System.out.println("4. Thriller");

        showMovies(sc.nextInt());
    }

    private static void showMovies(int genre) {
        List<Movie> movies = cinema.getMovieListByGenre(genre);
    }

    private static void logout() {
        currentUser = null;
        menu = Menu.MAIN;
    }

    private static void exit() {
        System.exit(0);
    }
















}
