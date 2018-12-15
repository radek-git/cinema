package model;

import java.time.LocalDateTime;

public class Seance {

    private int seanceId;
    private int movieId;
    private LocalDateTime dateTime;
    private int roomId;


    public Seance(int seanceId, int movieId, LocalDateTime dateTime, int roomId) {
        this(movieId, dateTime, roomId);

        this.seanceId = seanceId;
    }

    public Seance(int movieId, LocalDateTime dateTime, int roomId) {
        this.movieId = movieId;
        this.dateTime = dateTime;
        this.roomId = roomId;

    }

    public int getSeanceId() {
        return seanceId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
