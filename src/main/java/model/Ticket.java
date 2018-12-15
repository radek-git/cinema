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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketTypeId() {
        return ticketTypeId;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", seanceId=" + seanceId +
                ", ticketTypeId=" + ticketTypeId +
                ", row=" + row +
                ", seat=" + seat +
                '}';
    }
}
