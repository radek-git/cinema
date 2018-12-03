package model;

import org.json.simple.JSONObject;
import util.IdUtil;

public class Seance {

    private int seanceId;
    private int movieId;
    private int dayOfWeek;
    private String room;
    private String startTime;

    public Seance(int seanceId, int movieId, int dayOfWeek, String room, String startTime) {
        this.seanceId = seanceId;
        this.movieId = movieId;
        this.dayOfWeek = dayOfWeek;
        this.room = room;
        this.startTime = startTime;
    }

    public Seance(int id, int dayOfWeek, String room, String startTime) {
        this.seanceId = IdUtil.generateID(5);
        this.movieId = id;
        this.dayOfWeek = dayOfWeek;
        this.room = room;
        this.startTime = startTime;
    }

     public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seanceId", seanceId);
        jsonObject.put("movieId", movieId);
        jsonObject.put("room", room);
        jsonObject.put("startTime", startTime);
        jsonObject.put("tickets", 100);

        return jsonObject;
    }

    public static Seance fromJSON(JSONObject jsonObject, int dayOfWeek) {
        return new Seance(
                ((Long) jsonObject.get("seanceId")).intValue(),
                ((Long)jsonObject.get("movieId")).intValue(),
                dayOfWeek,
                (String)jsonObject.get("room"),
                (String)jsonObject.get("startTime")
        );
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}
