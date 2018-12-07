package model;

public class TicketType {

    private int typeId;
    private String type;
    private int price;

    public TicketType(String type, int price) {
        this.type = type;
        this.price = price;
    }

    public TicketType(int typeId, String type, int price) {
        this.typeId = typeId;
        this.type = type;
        this.price = price;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    
}
