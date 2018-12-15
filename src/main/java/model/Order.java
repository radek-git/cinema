package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int seanceId;
    private int userId;
    private int employeeId;

    private List<Ticket> tickets;

    public Order(int id, int seanceId, int userId, int employeeId, List<Ticket> tickets) {
        this.id = id;
        this.seanceId = seanceId;
        this.userId = userId;
        this.employeeId = employeeId;
        this.tickets = tickets;
    }

    public Order(int seanceId, int userId, int employeeId) {
        this.seanceId = seanceId;
        this.userId = userId;
        this.employeeId = employeeId;
        this.tickets = new ArrayList<>();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeanceId() {
        return seanceId;
    }

    public int getUserId() {
        return userId;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }


}
