package model;

public class Ticket {
    private int id;
    private int seanceId;
    private int ticketTypeId;
    private int row;
    private int seat;

    public Ticket(int id, int seanceId, int ticketTypeId, int row, int seat) {
        this.id = id;
        this.seanceId = seanceId;
        this.ticketTypeId = ticketTypeId;
        this.row = row;
        this.seat = seat;
    }

    public Ticket(int seanceId, int ticketTypeId, int row, int seat) {
        this.seanceId = seanceId;
        this.ticketTypeId = ticketTypeId;
        this.row = row;
        this.seat = seat;
    }
}
