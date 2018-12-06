package model;

import java.time.LocalDateTime;

public class Seance {

    private int seanceId;
    private int movieId;
    private LocalDateTime dateTime;
    private int roomId;
    private String startTime;

    public Seance(int seanceId, int movieId, LocalDateTime dateTime, int roomId, String startTime) {
        this(movieId, dateTime, roomId, startTime);

        this.seanceId = seanceId;
    }

    public Seance(int movieId, LocalDateTime dateTime, int roomId, String startTime) {
        this.movieId = movieId;
        this.dateTime = getDateTime();
        this.roomId = roomId;
        this.startTime = startTime;
    }

//     public JSONObject toJSON() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("seanceId", seanceId);
//        jsonObject.put("movieId", movieId);
//        jsonObject.put("roomId", roomId);
//        jsonObject.put("startTime", startTime);
//        jsonObject.put("tickets", 100);
//
//        return jsonObject;
//    }
//
//    public static Seance fromJSON(JSONObject jsonObject, int dayOfWeek) {
//        return new Seance(
//                ((Long) jsonObject.get("seanceId")).intValue(),
//                ((Long)jsonObject.get("movieId")).intValue(),
//                dayOfWeek,
//                (String)jsonObject.get("roomId"),
//                (String)jsonObject.get("startTime")
//        );
//    }


    public int getSeanceId() {
        return seanceId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getStartTime() {
        return startTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
