package model;

import org.json.simple.JSONObject;

public class User extends Account {

    public User(int id, String username, String password) {
        super(id, username, password);
    }

    public User(String username, String password) {
        super(username, password);
    }

    public static User fromJSON(JSONObject userJSONObject) {
        return new User(
                ((Long) userJSONObject.get("id")).intValue(),
                (String) userJSONObject.get("username"),
                (String) userJSONObject.get("password")
        );
    }

    public void abc() {
        System.out.println("abc");

    }





}
