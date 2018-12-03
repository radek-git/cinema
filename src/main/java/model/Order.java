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

    public void addTickets(int ticketTypeId, int row, int seat) {
        tickets.add(new Ticket(seanceId, ticketTypeId, row, seat));
    }

}
