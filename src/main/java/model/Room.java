package model;

public class Room {
    private int id;
    private String name;
    private int rows;
    private int seatsInRow;

    public Room(int id, String name, int rows, int seatsInRow) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsInRow = seatsInRow;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getSeats() {
        return seatsInRow;
    }
}
