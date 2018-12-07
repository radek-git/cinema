package model;

public class Room {
    private int id;
    private String name;
    private int seats;

    public Room(int id, String name, int seats) {
        this.id = id;
        this.name = name;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSeats() {
        return seats;
    }
}
