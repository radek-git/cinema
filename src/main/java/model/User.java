package model;

public class User extends Account {

    public User(int id, String username, String password) {
        super(id, username, password);
    }

    public User(String username, String password) {
        super(username, password);
    }

}
