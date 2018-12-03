package model;

import org.json.simple.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Account {
    private int id;
    private String username;
    private String password;

    public Account(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;

        this.id = ThreadLocalRandom.current().nextInt(10000, 100000);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        return jsonObject;
    }
}
